package structures;

import org.usb4java.*;
import org.usb4java.ConfigDescriptor;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class UsbDevice {

    Device libusb_dev;
    protected UsbDeviceDescriptor descriptor;
    protected UsbDeviceHandle handle;
    private int refCount = 1;

    public UsbDevice(org.usb4java.Device dev){
        this.libusb_dev = dev;
        descriptor = new UsbDeviceDescriptor(dev);
    }

    public boolean open(){
        handle = new UsbDeviceHandle(libusb_dev);
        refCount++;
        return true;
    }

    public boolean isOpen(){
        return handle!=null;
    }

    public boolean isActive(){
        return refCount>0;
    }

    public void close(){
        handle.close();
        handle = null;
        decrementRefCount();

    }

    private void decrementRefCount(){
        refCount--;
        if(refCount==0){
            libusb_dev = null;
            handle = null;
            descriptor = null;
        }
    }

    public String manufacturer(){
        return LibUsb.getStringDescriptor(handle.libusb_devHandle, descriptor.libusb_devDesc.iManufacturer());
    }
    public String product(){
        return LibUsb.getStringDescriptor(handle.libusb_devHandle, descriptor.libusb_devDesc.iProduct());
    }

    public String serialNumber(){
        return LibUsb.getStringDescriptor(handle.libusb_devHandle, descriptor.libusb_devDesc.iSerialNumber());
    }

    public UsbDeviceDescriptor devDescriptor(){
        return descriptor;
    }

    public int getBusNumber(){
        return LibUsb.getBusNumber(libusb_dev);
    }

    public int getPortNumber(){
        return LibUsb.getPortNumber(libusb_dev);
    }

    public int[] getPortNumbers(){

        ByteBuffer bb = ByteBuffer.allocateDirect(7);
        int[] portNumbers = new int[LibUsb.getPortNumbers(libusb_dev, bb)];
        for(int i=0; i<portNumbers.length; i++)
            portNumbers[i] = bb.get(i);

        return portNumbers;

    }

    public UsbDevice getParent(){
        Device dev = LibUsb.getParent(libusb_dev);

        if(dev==null)
            return null;

        UsbDevice parent = new UsbDevice(dev);
        return parent;
    }

    public int getAddress(){
        return LibUsb.getDeviceAddress(libusb_dev);
    }

    public int getSpeed(){
        return LibUsb.getDeviceSpeed(libusb_dev);
    }

    public int getMaxPacketSize(int endpoint){
        return LibUsb.getMaxPacketSize(libusb_dev, (byte) endpoint);
    }

    public int getMaxIsoPacketSize(int endpoint){
        return LibUsb.getMaxIsoPacketSize(libusb_dev, (byte) endpoint);
    }

    public void ref(){
        libusb_dev = LibUsb.refDevice(libusb_dev);
        refCount++;
    }

    public void unref(){
        LibUsb.unrefDevice(libusb_dev);
        decrementRefCount();
    }

    public ConfigDescriptor getActiveConfigDescriptor(){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getActiveConfigDescriptor(libusb_dev, confDesc);
        return confDesc;
    }

    public ConfigDescriptor getConfigDescriptor(int index){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptor(libusb_dev, (byte) index, confDesc);
        return confDesc;
    }

    public ConfigDescriptor getConfigDescriptorByValue(int bConfigurationValue){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptorByValue(libusb_dev, (byte) bConfigurationValue, confDesc);
        return confDesc;
    }

}
