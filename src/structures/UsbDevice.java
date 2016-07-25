package structures;

import org.usb4java.*;
import org.usb4java.ConfigDescriptor;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class UsbDevice {

    Device dev;
    DeviceDescriptor desc;
    DeviceHandle handle;
    private int refCount = 1;

    public UsbDevice(org.usb4java.Device dev){
        this.dev = dev;
        desc = new DeviceDescriptor();
        int retCode = LibUsb.getDeviceDescriptor(dev, desc);

    }

    public boolean open(){
        handle = new DeviceHandle();
        int retCode = LibUsb.open(dev, handle);
        if(retCode==0) {
            refCount++;
            return true;
        }
        else{
            handle = null;
            return false;
        }
    }

    public boolean isOpen(){
        return handle!=null;
    }

    public boolean isActive(){
        return refCount>0;
    }

    public void close(){
        LibUsb.close(handle);
        handle = null;
        decrementRefCount();
    }

    private void decrementRefCount(){
        refCount--;
        if(refCount==0){
            dev = null;
            handle = null;
            desc = null;
        }
    }

    public int vid(){
        return desc.idVendor();
    }

    public int pid(){
        return desc.idProduct();
    }

    public float getUsbSpecificationRelease(){
        int[] digits = new int[4];
        int bcdUSB = desc.bcdUSB();
        for(int i=0; i<4; i++)
            digits[i] = (bcdUSB>>(i*4))&0x0F;
        float version = (float) (digits[0]*0.01+digits[1]*0.1+digits[2]*1+digits[3]*10);
        return version;
    }

    public int getDevClass(){
        return desc.bDeviceClass();
    }

    public int getDevSubclass(){
        return desc.bDeviceSubClass();
    }

    public int getDevProtocol(){
        return desc.bDeviceProtocol();
    }

    public int getMaxPacketSize0(){
        return desc.bMaxPacketSize0();
    }

    public float getDevReleaseNumber(){
        int[] digits = new int[4];
        int bcdDev = desc.bcdDevice();
        for(int i=0; i<4; i++)
            digits[i] = (bcdDev >>(i*4))&0x0F;
        float version = (float) (digits[0]*0.01+digits[1]*0.1+digits[2]*1+digits[3]*10);
        return version;
    }

    public String manufacturer(){
        if(handle == null)
            throw new TBDException("Can't retrieve manufacturer string. Device not opened.");
        return LibUsb.getStringDescriptor(handle, desc.iManufacturer());
    }
    public String product(){
        if(handle == null)
            throw new TBDException("Can't retrieve product string. Device not opened.");
        return LibUsb.getStringDescriptor(handle, desc.iProduct());
    }

    public String serialNumber(){
        if(handle == null)
            throw new TBDException("Can't retrieve serial number. Device not opened.");
        return LibUsb.getStringDescriptor(handle, desc.iSerialNumber());
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
        Device dev = LibUsb.getParent(this.dev);

        if(dev==null)
            return null;

        UsbDevice parent = new UsbDevice(dev);
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
        refCount++;
    }

    public void unref(){
        LibUsb.unrefDevice(dev);
        decrementRefCount();
    }

    public ConfigDescriptor getActiveConfigDescriptor(){
        ConfigDescriptor confDesc = new ConfigDescriptor();
        int retCode = LibUsb.getActiveConfigDescriptor(dev, confDesc);
        return confDesc;
    }

    public int getNumberOfConfigurations(){
        return desc.bNumConfigurations();
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

    public UsbBosDescriptor getBosDescriptor(){
        UsbBosDescriptor bosDesc;
        BosDescriptor aux = new BosDescriptor();
        int retCode = LibUsb.getBosDescriptor(handle, aux);
        bosDesc = new UsbBosDescriptor(aux);
        return bosDesc;
    }

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Vendor id", desc.idVendor(), "%04x");
        sb.append("Product id", desc.idProduct(), "%04x");
        sb.append("Class");
        sb.append(getUsbClass(desc.bDeviceClass()));
        sb.append("Release nr", getDevReleaseNumber());
        sb.append("USB spec", getUsbSpecificationRelease());
        return sb.toString();
    }

    private String getUsbClass(int classCode){
        switch(classCode){
            case LibUsb.CLASS_APPLICATION: {
                return "Application";
            }
            case LibUsb.CLASS_AUDIO: {
                return "Audio";
            }
            case LibUsb.CLASS_COMM: {
                return "Communication";
            }
            case LibUsb.CLASS_CONTENT_SECURITY: {
                return "Content Security";
            }
            case LibUsb.CLASS_DATA: {
                return "Data";
            }
            case LibUsb.CLASS_DIAGNOSTIC_DEVICE: {
                return "Diagnostic Device";
            }
            case LibUsb.CLASS_HID: {
                return "HID";
            }
            case LibUsb.CLASS_HUB: {
                return "Hub";
            }
            case LibUsb.CLASS_IMAGE: {
                return "Image";
            }
            case LibUsb.CLASS_MASS_STORAGE: {
                return "Mass Storage";
            }
            case LibUsb.CLASS_PER_INTERFACE: {
                return "Per Interface";
            }
            case LibUsb.CLASS_PHYSICAL: {
                return "Physical";
            }
            case LibUsb.CLASS_PRINTER: {
                return "Printer";
            }
            case LibUsb.CLASS_SMART_CARD: {
                return "Smart Card";
            }
            case LibUsb.CLASS_VIDEO: {
                return "Video";
            }
            case LibUsb.CLASS_WIRELESS: {
                return "Wireless";
            }
            case LibUsb.CLASS_PERSONAL_HEALTHCARE: {
                return "Personal Healthcare";
            }
            case LibUsb.CLASS_VENDOR_SPEC: {
                return "Vendor Specific";
            }
            default:{
                return "";
            }
        }
    }

}
