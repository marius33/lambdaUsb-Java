package structures;

import org.usb4java.*;
import org.usb4java.DeviceDescriptor;

import java.nio.ByteBuffer;

public class Device {

    protected org.usb4java.Device dev;
    protected DeviceDescriptor descriptor;
    protected DeviceHandle handle;

    public Device(){

    }

    public Device(org.usb4java.Device libusb_dev){
        dev = libusb_dev;
        descriptor = new DeviceDescriptor();
        int retCode = LibUsb.getDeviceDescriptor(dev, descriptor);
        LibUsb.open
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

    public Device getParent(){
        Device parent = new Device();
        parent.dev = LibUsb.getParent(dev);

    }


}
