package structures;

import org.usb4java.LibUsb;

/**
 * Created by Marius on 31/07/2016.
 */
public abstract class HotplugListener {

    private int vid;
    private int pid;
    private LambdaUsb.DeviceClass devClass;

    protected HotplugListener(int vid, int pid, LambdaUsb.DeviceClass devClass){
        this.vid = vid;
        this.pid = pid;
        this.devClass = devClass;
    }

    abstract public void onPlug(UsbDevice dev);

    abstract public void onUnplug(UsbDevice dev);

    boolean matches(HotplugListener o) {

        int toCheckVid = o.vid;
        int toCheckPid = o.pid;
        LambdaUsb.DeviceClass toCheckClass = o.devClass;

        if (vid != toCheckVid && vid != LibUsb.HOTPLUG_MATCH_ANY && toCheckVid != LibUsb.HOTPLUG_MATCH_ANY)
            return false;
        if (pid != toCheckPid && pid != LibUsb.HOTPLUG_MATCH_ANY && toCheckPid != LibUsb.HOTPLUG_MATCH_ANY)
            return false;
        if (!devClass.equals(toCheckClass) && !devClass.equals(LambdaUsb.DeviceClass.Any) && !toCheckClass.equals(LambdaUsb.DeviceClass.Any))
            return false;

        return true;

    }

    boolean matches(UsbDevice dev) {

        int toCheckVid = dev.getDeviceDescriptor().idVendor();
        int toCheckPid = dev.getDeviceDescriptor().idProduct();
        LambdaUsb.DeviceClass toCheckClass = dev.getDeviceDescriptor().getDeviceClass();

        if (vid != toCheckVid && vid != LibUsb.HOTPLUG_MATCH_ANY && toCheckVid != LibUsb.HOTPLUG_MATCH_ANY)
            return false;
        if (pid != toCheckPid && pid != LibUsb.HOTPLUG_MATCH_ANY && toCheckPid != LibUsb.HOTPLUG_MATCH_ANY)
            return false;
        if (!devClass.equals(toCheckClass) && !devClass.equals(LambdaUsb.DeviceClass.Any) && !toCheckClass.equals(LambdaUsb.DeviceClass.Any))
            return false;

        return true;

    }

}
