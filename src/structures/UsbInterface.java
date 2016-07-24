package structures;

import org.usb4java.DeviceHandle;
import org.usb4java.Interface;
import org.usb4java.InterfaceDescriptor;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbInterface {

    private UsbInterfaceDescriptor[] altSettings;
    private Interface libusb_iface;

    protected UsbInterface(Interface iface, DeviceHandle dev){
        libusb_iface = iface;
        altSettings = new UsbInterfaceDescriptor[iface.numAltsetting()];
        for(int i = 0; i<iface.numAltsetting(); i++)
            altSettings[i] = new UsbInterfaceDescriptor(iface.altsetting()[i], dev);
    }

    public UsbInterfaceDescriptor getSetting(int index){
        return altSettings[index];
    }

    public Interface toLibUsb(){
        return libusb_iface;
    }


}
