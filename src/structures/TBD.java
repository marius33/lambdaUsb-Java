package structures;

import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

import java.util.List;

/**
 * Created by Marius on 24/07/2016.
 */
public class TBD {

    private TBD(){
    }

    private static boolean isInit = false;

    private static void init(){

        if(isInit == false) {
            LibUsb.init(null);
            isInit = true;
        }

    }

    public static UsbDeviceList getUsbDevices(){

        if(isInit==false)
            init();

        DeviceList list = new DeviceList();
        int size = LibUsb.getDeviceList(null, list);
        UsbDeviceList devList = new UsbDeviceList(size, list);
        LibUsb.freeDeviceList(list, false);
        return devList;

    }

    public static class ERROR_CODE{

        public static final int SUCCESS = 0;
        public static final int IO = -1;
        public static final int INVALID_PARAM = -2;
        public static final int ACCESS = -3;
        public static final int NO_DEVICE = -4;
        public static final int NOT_FOUND = -5;
        public static final int BUSY = -6;
        public static final int TIMEOUT = -7;
        public static final int OVERFLOW = -8;
        public static final int PIPE = -9;
        public static final int INTERRUPTED = -10;
        public static final int NO_MEM = -11;
        public static final int NOT_SUPPORTED = -12;
        public static final int OTHER = -99;

        static String getErrorDescription(int errCode){
            switch(errCode){
                case ERROR_CODE.IO: return "Input/Output error.";
                case ERROR_CODE.INVALID_PARAM: return "Invalid parameter.";
                case ERROR_CODE.ACCESS: return "Access denied (insufficient permissions).";
                case ERROR_CODE.NO_DEVICE: return "No such device (it may have been disconnected).";
                case ERROR_CODE.NOT_FOUND: return "Entity not found.";
                case ERROR_CODE.BUSY: return "Resource busy.";
                case ERROR_CODE.TIMEOUT: return "Operation timed out.";
                case ERROR_CODE.OVERFLOW: return "Overflow.";
                case ERROR_CODE.PIPE: return "Pipe error.";
                case ERROR_CODE.INTERRUPTED: return "System call interrupted (perhaps due to signal).";
                case ERROR_CODE.NO_MEM: return "Insufficient memory.";
                case ERROR_CODE.NOT_SUPPORTED: return "Operation not supported or unimplemented on this platform.";
                case ERROR_CODE.OTHER: return "Other error.";
                default: return "Error code outside of libusb.";
            }
        }

    }

    public static class DEVICE_CLASS{

        static String getUsbClassString(int classCode){
            switch(classCode){
                case LibUsb.CLASS_APPLICATION: return "Application";
                case LibUsb.CLASS_AUDIO: return "Audio";
                case LibUsb.CLASS_COMM:  return "Communication";
                case LibUsb.CLASS_CONTENT_SECURITY: return "Content Security";
                case LibUsb.CLASS_DATA: return "Data";
                case LibUsb.CLASS_DIAGNOSTIC_DEVICE: return "Diagnostic Device";
                case LibUsb.CLASS_HID: return "HID";
                case LibUsb.CLASS_HUB: return "Hub";
                case LibUsb.CLASS_IMAGE: return "Image";
                case LibUsb.CLASS_MASS_STORAGE: return "Mass Storage";
                case LibUsb.CLASS_PER_INTERFACE: return "Per Interface";
                case LibUsb.CLASS_PHYSICAL:  return "Physical";
                case LibUsb.CLASS_PRINTER: return "Printer";
                case LibUsb.CLASS_SMART_CARD: return "Smart Card";
                case LibUsb.CLASS_VIDEO: return "Video";
                case LibUsb.CLASS_WIRELESS: return "Wireless";
                case LibUsb.CLASS_PERSONAL_HEALTHCARE: return "Personal Healthcare";
                case LibUsb.CLASS_VENDOR_SPEC: return "Vendor Specific";
                default: return "Unknown";
            }
        }

    }



}
