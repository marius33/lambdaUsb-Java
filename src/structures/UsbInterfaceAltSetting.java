package structures;

import org.usb4java.EndpointDescriptor;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

import java.nio.ByteBuffer;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbInterfaceAltSetting {

    private InterfaceDescriptor desc;
    private UsbEndpoint[] endpoints;
    UsbInterface parent;

    protected UsbInterfaceAltSetting(InterfaceDescriptor ifaceDesc, UsbInterface parent) {
        desc = ifaceDesc;
        endpoints = new UsbEndpoint[ifaceDesc.bNumEndpoints()];
        for(int i=0; i<ifaceDesc.bNumEndpoints(); i++)
            endpoints[i] = new UsbEndpoint(ifaceDesc.endpoint()[i], this);
    }

    public int bLength(){
        return desc.bLength();
    }

    public byte bDescriptorType() {
        return desc.bDescriptorType();
    }

    public byte bInterfaceNumber() {
        return desc.bInterfaceNumber();
    }

    public byte bAlternateSetting() {
        return desc.bAlternateSetting();
    }

    public byte bNumEndpoints() {
        return desc.bNumEndpoints();
    }

    public byte bInterfaceClass() {
        return desc.bInterfaceClass();
    }

    public TBD.DeviceClass getDeviceClass(){
        return TBD.DeviceClass.getFromCode(bInterfaceClass());
    }

    public byte bInterfaceSubClass() {
        return desc.bInterfaceSubClass();
    }

    public byte bInterfaceProtocol() {
        return desc.bInterfaceProtocol();
    }

    public byte iInterface() {
        return desc.iInterface();
    }

    public EndpointDescriptor[] endpoint() {
        return desc.endpoint();
    }

    public ByteBuffer extra() {
        return desc.extra();
    }

    public int extraLength() {
        return desc.extraLength();
    }

    public String getDescription(){

        return LibUsb.getStringDescriptor(parent.parent.parent.handle, iInterface());

    }

    public int getNumberOfEndpoints(){
        return endpoints.length;
    }

    public UsbEndpoint getEndpoint(int index){
        return endpoints[index];
    }

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Interface number", desc.bInterfaceNumber());
        sb.append("Alt setting value", desc.bAlternateSetting());
        sb.append("Number of endpoints", desc.bNumEndpoints());
        sb.append("Class", getDeviceClass().toString());
        sb.append("Subclass", desc.bInterfaceSubClass());
        sb.append("Protocol", desc.bInterfaceProtocol());
        sb.append("Description", getDescription());
        return sb.toString();
    }

}
