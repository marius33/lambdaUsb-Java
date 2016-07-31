package structures;

import org.usb4java.*;
import org.usb4java.ConfigDescriptor;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class UsbDevice {

    Device dev;
    DeviceHandle handle;
    UsbDeviceDescriptor desc;

    private int refCount = 1;

    UsbDevice(org.usb4java.Device dev){
        this.dev = dev;
    }

    public void open() throws Exception{
        handle = new DeviceHandle();
        int retCode = LibUsb.open(dev, handle);
        if(retCode == LambdaUsb.Error.Success.valueOf())
            refCount++;
        else{
            handle = null;
            throw new LambdaUsbException(retCode);
        }
    }

    public void close(){
        LibUsb.close(handle);
        handle = null;
        decrementRefCount();
    }

    public void resetDevice(){
        if(handle==null)
            return;
        int retCode = LibUsb.resetDevice(handle);
        if(retCode!= LambdaUsb.Error.Success.valueOf())
            throw new LambdaUsbRuntimeException(retCode);
    }

    public void ref(){
        dev = LibUsb.refDevice(dev);
        refCount++;
    }

    public void unref(){
        LibUsb.unrefDevice(dev);
        decrementRefCount();
    }

    private void decrementRefCount(){
        refCount--;
        if(refCount==0){
            dev = null;
            handle = null;
            desc = null;
        }
    }

    public boolean isOpen(){
        return handle!=null;
    }

    public boolean isActive(){
        return refCount>0;
    }

    public int getBusNumber(){
        return LibUsb.getBusNumber(dev);
    }

    public int getPortNumber(){
        return LibUsb.getPortNumber(dev);
    }

    public int[] getPortNumbers(){

        ByteBuffer bb = ByteBuffer.allocateDirect(7);
        int[] portNumbers = new int[LibUsb.getPortNumbers(dev, bb)];
        for(int i=0; i<portNumbers.length; i++)
            portNumbers[i] = bb.get(i);

        return portNumbers;

    }

    public UsbDevice getParent(){
        Device dev = LibUsb.getParent(this.dev);

        if(dev==null)
            return null;

        UsbDevice parent = new UsbDevice(dev);
        return parent;
    }

    public int getAddress(){
        return LibUsb.getDeviceAddress(dev);
    }

    public LambdaUsb.DeviceSpeed getSpeed(){
        return LambdaUsb.DeviceSpeed.getFromCode(LibUsb.getDeviceSpeed(dev));
    }

    public UsbConfigDescriptor getActiveConfigDescriptor() throws LambdaUsbException {
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getActiveConfigDescriptor(dev, confDesc);
        if(retCode!= LambdaUsb.Error.Success.valueOf())
            throw new LambdaUsbException(retCode);
        return new UsbConfigDescriptor(this);
    }

    public int getNumberOfConfigurations(){
        return desc.bNumConfigurations();
    }

    public UsbConfigDescriptor getConfigDescriptor(int index) throws LambdaUsbException {

        return new UsbConfigDescriptor(this, index);
    }

    public UsbConfigDescriptor getConfigDescriptorByValue(byte bConfigurationValue) throws LambdaUsbException {
        return new UsbConfigDescriptor(this, bConfigurationValue);
    }

    public int getConfiguration(){
        IntBuffer ib = IntBuffer.allocate(1);
        if(handle==null)
            return -1;
        int retCode = LibUsb.getConfiguration(handle, ib);
        if(retCode!= LambdaUsb.Error.Success.valueOf())
            throw new LambdaUsbRuntimeException(retCode);
        return ib.get();
    }

    public void setConfiguration(int conf){
        int retCode = LibUsb.setConfiguration(handle, conf);
        if(retCode!= LambdaUsb.Error.Success.valueOf())
            throw new LambdaUsbRuntimeException(retCode);
    }

    public UsbDeviceDescriptor getDeviceDescriptor(){
        return desc;
    }

    public LambdaUsb.DeviceClass getDeviceClass() {
        return desc.getDeviceClass();
    }

    public int vid() {
        return desc.idVendor();
    }

    public int pid() {
        return desc.idProduct();
    }

    public float getDevReleaseNumber() {
        return desc.getDevReleaseNumber();
    }

    public String manufacturer() {
        return desc.manufacturer();
    }

    public String product() {
        return desc.product();
    }

    public String serialNumber() {
        return desc.serialNumber();
    }

    public UsbBosDescriptor getBosDescriptor(){
        if(handle==null)
            return null;
        UsbBosDescriptor bosDesc;
        BosDescriptor aux = new BosDescriptor();
        int retCode = LibUsb.getBosDescriptor(handle, aux);
        if(retCode!= LambdaUsb.Error.Success.valueOf())
            throw new LambdaUsbRuntimeException(retCode);
        bosDesc = new UsbBosDescriptor(aux);
        return bosDesc;
    }

    public boolean setAutoDetachKernelDriver(boolean set){
        int retCode = LibUsb.setAutoDetachKernelDriver(handle, set);
        return retCode== LambdaUsb.Error.Success.valueOf();
    }

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Vendor id", desc.idVendor(), "%04x");
        sb.append("Product id", desc.idProduct(), "%04x");
        sb.append("Class");
        sb.append(desc.getDeviceClass());
        sb.append("Release nr", desc.getDevReleaseNumber());
        sb.append("USB spec", desc.getUsbSpecificationReleaseNumber());
        return sb.toString();
    }

}
