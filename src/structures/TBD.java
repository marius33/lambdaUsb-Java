package structures;

import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

import java.util.List;

/**
 * Created by Marius on 24/07/2016.
 */
public class TBD {

    private boolean isInit = false;

    private void init(){

        if(isInit == false) {
            LibUsb.init(null);
            isInit = true;
        }

    }

    public UsbDeviceList getUsbDevices(){

        if(isInit==false)
            init();

        DeviceList list = new DeviceList();
        int size = LibUsb.getDeviceList(null, list);
        UsbDeviceList devList = new UsbDeviceList(size, list);
        LibUsb.freeDeviceList(list, false);
        return devList;

    }

}
