package structures;

import org.usb4java.*;
import org.usb4java.ConfigDescriptor;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class UsbDevice {

    protected Device dev;
    protected DeviceDescriptor descriptor;
    protected DeviceHandle handle;

    public UsbDevice(){

    }

    public UsbDevice(org.usb4java.Device libusb_dev){
        dev = libusb_dev;
        descriptor = new DeviceDescriptor();
        int retCode = LibUsb.getDeviceDescriptor(dev, descriptor);
    }

    public boolean open(){
        int retCode = LibUsb.open(dev, handle);
        if(retCode == 0) {
            LibUsb.getDevice(handle);
            return true;
        }
        else
            return false;
    }

    public boolean close(){
        return true;
    }

    public String iManufacturer(){
        return LibUsb.getStringDescriptor(handle, descriptor.iManufacturer());
    }

    public String iProduct(){
        return LibUsb.getStringDescriptor(handle, descriptor.iProduct());
    }

    public String iSerialNumber(){
        return LibUsb.getStringDescriptor(handle, descriptor.iSerialNumber());
    }

    public DeviceDescriptor devDescriptor(){
        return descriptor;
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
        UsbDevice parent = new UsbDevice();
        parent.dev = LibUsb.getParent(dev);
        return parent;
    }

    public int getAddress(){
        return LibUsb.getDeviceAddress(dev);
    }

    public int getSpeed(){
        return LibUsb.getDeviceSpeed(dev);
    }

    public int getMaxPacketSize(int endpoint){
        return LibUsb.getMaxPacketSize(dev, (byte) endpoint);
    }

    public int getMaxIsoPacketSize(int endpoint){
        return LibUsb.getMaxIsoPacketSize(dev, (byte) endpoint);
    }

    public void ref(){
        dev = LibUsb.refDevice(dev);
    }

    public void unref(){
        LibUsb.unrefDevice(dev);
    }

    private Device getDev(){
        return LibUsb.getDevice(handle);
    }

    public int getConfiguration(){
        IntBuffer ib = IntBuffer.allocate(0);
        int retCode = LibUsb.getConfiguration(handle, ib);
        return ib.get();
    }

    public void setConfiguration(int conf){
        int retCode = LibUsb.setConfiguration(handle, conf);
    }

    public void claimInterface(int iface){
        int retCode = LibUsb.claimInterface(handle, iface);
    }

    public void releaseInterface(int iface){
        int retCode = LibUsb.releaseInterface(handle, iface);
    }

    public void setInterfaceAltSetting(int iface, int altSetting){
        int retCode = LibUsb.setInterfaceAltSetting(handle, iface, altSetting);
    }

    public void clearHalt(int endpoint){
        int retCode = LibUsb.clearHalt(handle, (byte) endpoint);
    }

    public void resetDevice(){
        int retCode = LibUsb.resetDevice(handle);
    }

    public boolean kernelDriverActive(int iface){
        int retCode = LibUsb.kernelDriverActive(handle, iface);
        if(retCode==0)
            return false;
        else if(retCode==1)
            return true;
        else
            throw new LibUsbException(retCode);
    }

    public ConfigDescriptor getActiveConfigDescriptor(){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getActiveConfigDescriptor(dev, confDesc);
        return confDesc;
    }

    public ConfigDescriptor getConfigDescriptor(int index){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptor(dev, (byte) index, confDesc);
        return confDesc;
    }

    public ConfigDescriptor getConfigDescriptorByValue(int bConfigurationValue){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptorByValue(dev, (byte) bConfigurationValue, confDesc);
        return confDesc;
    }

    public BosDescriptor getBosDescriptor(){
        BosDescriptor bosDesc = new BosDescriptor();
        int retCode = LibUsb.getBosDescriptor(handle, bosDesc);
        return bosDesc;
    }




}
