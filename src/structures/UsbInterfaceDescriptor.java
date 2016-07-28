package structures;

import org.usb4java.DeviceHandle;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbInterfaceDescriptor {

    private InterfaceDescriptor ifaceDesc;
    private UsbEndpointDescriptor[] endpoints;
    private String description;

    protected UsbInterfaceDescriptor(InterfaceDescriptor ifaceDesc, DeviceHandle dev) {
        this.ifaceDesc = ifaceDesc;
        endpoints = new UsbEndpointDescriptor[ifaceDesc.bNumEndpoints()];
        for(int i=0; i<ifaceDesc.bNumEndpoints(); i++)
            endpoints[i] = new UsbEndpointDescriptor(ifaceDesc.endpoint()[i]);
        description = LibUsb.getStringDescriptor(dev, ifaceDesc.iInterface());
    }

    public InterfaceDescriptor toLibUsb(){
        return ifaceDesc;
    }

    public int getNumber(){
        return ifaceDesc.bInterfaceNumber();
    }

    public int getAltSettingValue(){
        return ifaceDesc.bAlternateSetting();
    }

    public int getIfClass(){
        return ifaceDesc.bInterfaceClass();
    }

    public int getIfSubClass(){
        return ifaceDesc.bInterfaceSubClass();
    }

    public int getIfProtocol(){
        return ifaceDesc.bInterfaceProtocol();
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

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Interface number", ifaceDesc.bInterfaceNumber());
        sb.append("Alt setting value", ifaceDesc.bAlternateSetting());
        sb.append("Number of endpoints", ifaceDesc.bNumEndpoints());
        sb.append("Class", TBD.DEVICE_CLASS.getString(ifaceDesc.bInterfaceClass()));
        sb.append("Subclass", TBD.DEVICE_CLASS.getString(ifaceDesc.bInterfaceSubClass()));
        sb.append("Protocol", ifaceDesc.bInterfaceProtocol());
        sb.append("Description", description);
        return sb.toString();
    }

}
