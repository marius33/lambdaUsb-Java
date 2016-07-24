package structures;

import org.usb4java.ConfigDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 23/07/2016.
 */
public class UsbConfigDescriptor {

    protected ConfigDescriptor libusb_confDesc;
    protected UsbInterface[] interfaces;
    private String description;

    protected UsbConfigDescriptor(ConfigDescriptor confDesc, DeviceHandle dev){

        libusb_confDesc = confDesc;

        description = LibUsb.getStringDescriptor(dev, confDesc.iConfiguration());

        interfaces = new UsbInterface[confDesc.bNumInterfaces()];
        for(int i = 0; i<confDesc.bNumInterfaces(); i++)
            interfaces[i] = new UsbInterface(confDesc.iface()[i], dev);


    }

    public ConfigDescriptor getLibusb(){
        return libusb_confDesc;
    }

    public int configValue(){
        return libusb_confDesc.bConfigurationValue();
    }

    public int configAttributes(){
        return libusb_confDesc.bmAttributes();
    }

    public String description(){
        return description;
    }



}
