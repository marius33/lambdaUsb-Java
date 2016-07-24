package structures;

import org.usb4java.*;

import java.nio.IntBuffer;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbDeviceHandle{

    DeviceHandle libusb_devHandle;

    UsbDeviceHandle(Device dev){
        libusb_devHandle = new DeviceHandle();
        int retCode = LibUsb.open(dev, libusb_devHandle);
    }

    protected void close(){
        LibUsb.close(libusb_devHandle);
    }

    public int getConfiguration(){
        IntBuffer ib = IntBuffer.allocate(0);
        int retCode = LibUsb.getConfiguration(libusb_devHandle, ib);
        return ib.get();
    }

    public void setConfiguration(int conf){
        int retCode = LibUsb.setConfiguration(libusb_devHandle, conf);
    }

    public void claimInterface(int iface){
        int retCode = LibUsb.claimInterface(libusb_devHandle, iface);
    }

    public void releaseInterface(int iface){
        int retCode = LibUsb.releaseInterface(libusb_devHandle, iface);
    }

    public void setInterfaceAltSetting(int iface, int altSetting){
        int retCode = LibUsb.setInterfaceAltSetting(libusb_devHandle, iface, altSetting);
    }

    public void clearHalt(int endpoint){
        int retCode = LibUsb.clearHalt(libusb_devHandle, (byte) endpoint);
    }

    public void resetDevice(){
        int retCode = LibUsb.resetDevice(libusb_devHandle);
    }

    public boolean kernelDriverActive(int iface){
        int retCode = LibUsb.kernelDriverActive(libusb_devHandle, iface);
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
        int retCode = LibUsb.getBosDescriptor(libusb_devHandle, aux);
        bosDesc = new UsbBosDescriptor(aux);
        return bosDesc;
    }

}
