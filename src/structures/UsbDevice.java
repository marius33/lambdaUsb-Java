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
        if(retCode==TBD.ERROR_CODE.SUCCESS)
            refCount++;
        else{
            handle = null;
            throw new TBDException(retCode);
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
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
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

    public int getSpeed(){
        return LibUsb.getDeviceSpeed(dev);
    }

    public UsbConfigDescriptor getActiveConfigDescriptor() throws TBDException {
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getActiveConfigDescriptor(dev, confDesc);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);
        return new UsbConfigDescriptor(confDesc, handle);
    }

    public int getNumberOfConfigurations(){
        return desc.bNumConfigurations();
    }

    public UsbConfigDescriptor getConfigDescriptor(int index){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptor(dev, (byte) index, confDesc);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
        return new UsbConfigDescriptor(confDesc, handle);
    }

    public UsbConfigDescriptor getConfigDescriptorByValue(int bConfigurationValue){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptorByValue(dev, (byte) bConfigurationValue, confDesc);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
        return new UsbConfigDescriptor(confDesc, handle);
    }

    public int getConfiguration(){
        IntBuffer ib = IntBuffer.allocate(0);
        if(handle==null)
            return -1;
        int retCode = LibUsb.getConfiguration(handle, ib);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
        return ib.get();
    }

    public void setConfiguration(int conf){
        int retCode = LibUsb.setConfiguration(handle, conf);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }

    public UsbDeviceDescriptor getDeviceDescriptor(){
        return desc;
    }

    public UsbBosDescriptor getBosDescriptor(){
        if(handle==null)
            return null;
        UsbBosDescriptor bosDesc;
        BosDescriptor aux = new BosDescriptor();
        int retCode = LibUsb.getBosDescriptor(handle, aux);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
        bosDesc = new UsbBosDescriptor(aux);
        return bosDesc;
    }


    //endpoint shit
    public int[] readBulk(int len, int endpoint) throws TBDException {
        ByteBuffer buff = ByteBuffer.allocateDirect(len);
        int retLen = transferBulk(buff, endpoint);
        int[] ret = new int[retLen];
        for(int i=0; i<retLen; i++)
            ret[i] = buff.get(i);
        return ret;
    }

    public void writeBulk(int[] data, int endpoint) throws TBDException {
        ByteBuffer buff = ByteBuffer.allocateDirect(data.length);
        int retLen = transferBulk(buff, endpoint);
    }

    public int transferBulk(ByteBuffer data, int endpoint) throws TBDException {
        IntBuffer transferred = IntBuffer.allocate(1);
        int retCode = LibUsb.bulkTransfer(handle, (byte) endpoint, data, transferred, 2000);
        if(retCode==TBD.ERROR_CODE.SUCCESS){
            return transferred.get();
        }
        else
            throw new TBDException(retCode);
    }

    public int getMaxPacketSize(int endpoint){
        return LibUsb.getMaxPacketSize(dev, (byte) endpoint);
    }

    public int getMaxIsoPacketSize(int endpoint){
        return LibUsb.getMaxIsoPacketSize(dev, (byte) endpoint);
    }

    public void clearHalt(int endpoint){
        if(handle==null)
            return;
        int retCode = LibUsb.clearHalt(handle, (byte) endpoint);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }


    //interface shit
    public void claimInterface(int iface){
        if(handle==null)
            return;
        int retCode = LibUsb.claimInterface(handle, iface);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }

    public void releaseInterface(int iface){
        if(handle==null)
            return;
        int retCode = LibUsb.releaseInterface(handle, iface);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }

    public void setInterfaceAltSetting(int iface, int altSetting){
        if(handle==null)
            return;
        int retCode = LibUsb.setInterfaceAltSetting(handle, iface, altSetting);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }

    public boolean kernelDriverActive(int iface){
        int retCode = LibUsb.kernelDriverActive(handle, iface);
        if(retCode==0)
            return false;
        else if(retCode==1)
            return true;
        else
            throw new TBDRuntimeException(retCode);
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
