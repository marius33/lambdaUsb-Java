package structures;

import org.usb4java.Interface;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 24/07/2016.
 */
public final class UsbInterface {

    UsbConfigDescriptor parent;
    UsbInterfaceAltSetting[] altSettings;
    Interface iface;

    private int currentSetting = 0;
    boolean isClaimed = false;

    protected UsbInterface(Interface iface, UsbConfigDescriptor parent) {
        this.parent = parent;
        this.iface = iface;
        altSettings = new UsbInterfaceAltSetting[iface.numAltsetting()];
        for (int i = 0; i < iface.numAltsetting(); i++)
            altSettings[i] = new UsbInterfaceAltSetting(iface.altsetting()[i], this);
    }

    public int getNumber() {
        return altSettings[currentSetting].bInterfaceNumber();
    }

    public void claim() throws TBDException {
        int retCode = LibUsb.claimInterface(parent.parent.handle, getNumber());
        if (retCode != TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);
        else
            isClaimed = true;
    }

    public void release() throws TBDException {
        int retCode = LibUsb.releaseInterface(parent.parent.handle, getNumber());
        if (retCode != TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);
        else
            isClaimed = false;
    }

    public boolean isClaimed(){
        return isClaimed;
    }

    public void setAltSetting(int value) throws TBDException {
        int index = -1;
        for (int i = 0; i < altSettings.length; i++) {
            if (altSettings[i].bAlternateSetting() == value) {
                index = i;
                break;
            }
        }
        if (index > 0) {
            int retCode = LibUsb.setInterfaceAltSetting(parent.parent.handle, getNumber(), value);
            if (retCode != TBD.ERROR_CODE.SUCCESS)
                throw new TBDException(retCode);
            currentSetting = index;
        } else
            throw new TBDException(TBD.ERROR_CODE.NOT_FOUND);
    }

    public int numAltSettings() {
        return iface.numAltsetting();
    }

    public UsbInterfaceAltSetting getAltSetting(int index) {
        return altSettings[index];
    }

    public boolean isKernelDriverActive() throws TBDException {
        int retCode = LibUsb.kernelDriverActive(parent.parent.handle, getNumber());
        if (retCode < TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);
        else
            return retCode == 1;
    }

    public void detachKernelDriver() throws TBDException {
        int retCode = LibUsb.detachKernelDriver(parent.parent.handle, getNumber());
        if (retCode != TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);
    }

    public void attachKernelDriver() throws TBDException {
        int retCode = LibUsb.attachKernelDriver(parent.parent.handle, getNumber());
        if (retCode != TBD.ERROR_CODE.SUCCESS)
            throw new TBDException(retCode);
    }

    @Override
    public String toString() {
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Num. of alt. settings", iface.numAltsetting());
        return sb.toString();

    }


}
