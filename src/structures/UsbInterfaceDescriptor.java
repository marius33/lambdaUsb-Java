package structures;

import org.usb4java.DeviceHandle;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbInterfaceDescriptor {

    private InterfaceDescriptor libusb_ifaceDesc;
    private UsbEndpointDescriptor[] endpoints;
    private String description;

    protected UsbInterfaceDescriptor(InterfaceDescriptor ifaceDesc, DeviceHandle dev) {
        libusb_ifaceDesc = ifaceDesc;
        endpoints = new UsbEndpointDescriptor[ifaceDesc.bNumEndpoints()];
        for(int i=0; i<ifaceDesc.bNumEndpoints(); i++)
            endpoints[i] = new UsbEndpointDescriptor(ifaceDesc.endpoint()[i]);
        description = LibUsb.getStringDescriptor(dev, ifaceDesc.iInterface());
    }

    public InterfaceDescriptor toLibUsb(){
        return libusb_ifaceDesc;
    }

    public int getNumber(){
        return libusb_ifaceDesc.bInterfaceNumber();
    }

    public int getAltSettingValue(){
        return libusb_ifaceDesc.bAlternateSetting();
    }

    public int getIfClass(){
        return libusb_ifaceDesc.bInterfaceClass();
    }

    public int getIfSubClass(){
        return libusb_ifaceDesc.bInterfaceSubClass();
    }

    public int getIfProtocol(){
        return libusb_ifaceDesc.bInterfaceProtocol();
    }

    public String getDescription(){
        return description;
    }

    public int getNumberOfEndpoints(){
        return endpoints.length;
    }

    public UsbEndpointDescriptor getEndpoint(int index){
        return endpoints[index];
    }

}
