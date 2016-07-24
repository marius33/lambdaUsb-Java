package structures;

import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbDeviceDescriptor {

    DeviceDescriptor libusb_devDesc;

    UsbDeviceDescriptor(Device dev){
        libusb_devDesc = new DeviceDescriptor();
        int retCode = LibUsb.getDeviceDescriptor(dev, libusb_devDesc);
    }
}
