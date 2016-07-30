package structures;

import org.usb4java.ConfigDescriptor;
import org.usb4java.LibUsb;

import java.nio.ByteBuffer;

/**
 * Created by Marius on 23/07/2016.
 */
public final class UsbConfigDescriptor {

    UsbDevice parent;
    ConfigDescriptor desc;
    UsbInterface[] interfaces;


    UsbConfigDescriptor(UsbDevice parent, int index) throws TBDException {

        this.parent = parent;
        desc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptor(parent.dev, (byte) index, desc);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);

        interfaces = new UsbInterface[desc.bNumInterfaces()];
        for (int i = 0; i < desc.bNumInterfaces(); i++)
            interfaces[i] = new UsbInterface(desc.iface()[i], this);

    }

    UsbConfigDescriptor(UsbDevice parent, byte value) throws TBDException {

        this.parent = parent;
        desc = new ConfigDescriptor();
        int retCode = LibUsb.getConfigDescriptorByValue(parent.dev, value, desc);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);

        interfaces = new UsbInterface[desc.bNumInterfaces()];
        for (int i = 0; i < desc.bNumInterfaces(); i++)
            interfaces[i] = new UsbInterface(desc.iface()[i], this);

    }

    UsbConfigDescriptor(UsbDevice parent) throws TBDException {

        this.parent = parent;
        desc = new ConfigDescriptor();
        int retCode = LibUsb.getActiveConfigDescriptor(parent.dev, desc);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);

        interfaces = new UsbInterface[desc.bNumInterfaces()];
        for (int i = 0; i < desc.bNumInterfaces(); i++)
            interfaces[i] = new UsbInterface(desc.iface()[i], this);

    }

    public int bLength() {
        return desc.bLength();
    }

    public int bDescriptorType() {
        return desc.bDescriptorType();
    }

    public int wTotalLength() {
        return desc.wTotalLength();
    }

    public int bNumInterfaces() {
        return desc.bNumInterfaces();
    }

    public UsbInterface getInterface(int index){
        return interfaces[index];
    }

    public int bConfigurationValue() {
        return desc.bConfigurationValue();
    }

    public int iConfiguration() {
        return desc.iConfiguration();
    }

    public String description() {
        if (parent.handle == null)
            throw new TBDRuntimeException(TBD.ERROR_CODE.OTHER, "Device must be opened before retrieving a string.");
        return LibUsb.getStringDescriptor(parent.handle, desc.iConfiguration());
    }

    public int bmAttributes() {
        return desc.bmAttributes();
    }

    public boolean isSelfPowered() {
        return (desc.bmAttributes() & 0x40) != 0;
    }

    public boolean hasRemoteWakeup() {
        return (desc.bmAttributes() & 0x20) != 0;
    }

    public int bMaxPower() {
        return desc.bMaxPower();
    }

    public int getMaxPower() {
        if (parent.getSpeed().valueOf() <= TBD.DeviceSpeed.CODE.HIGH)
            return desc.bMaxPower() * 2;
        else
            return desc.bMaxPower() * 8;
    }

    public byte[] extra(){
        return desc.extra().array();
    }

    public void setActive(){
        int retCode = LibUsb.setConfiguration(parent.handle, desc.bConfigurationValue());
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }

    @Override
    public String toString() {
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Configuration value", desc.bConfigurationValue());
        sb.append("Config description", description());
        sb.append("Attributes", desc.bmAttributes() & 0xFF, "%02x");
        sb.append("\tSelf-powered", (desc.bmAttributes() & 0x40) != 0);
        sb.append("\tRemote Wakeup", (desc.bmAttributes() & 0x20) != 0);
        sb.append("Max power", desc.bMaxPower() * 2 + "mA");
        sb.append("Number of ifaces", desc.bNumInterfaces());
        return sb.toString();
    }


}
