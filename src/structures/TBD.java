package structures;

import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

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

    public enum TransferType {

        Control(CODE.CONTROL, "Control"),
        Isochronous(CODE.ISOCHRONOUS, "Isochronous"),
        Bulk(CODE.BULK, "Bulk"),
        Interrupt(CODE.INTERRUPT, "Interrupt"),
        BulkStream(CODE.BULK_STREAM, "Bulk stream");

        private int code;
        private String description;

        TransferType(int code, String desc){
            this.code = code;
            description = desc;
        }

        public int valueOf(){
            return code;
        }

        @Override
        public String toString(){
            return description;
        }

        public static TransferType getFromCode(int type) {
            for(TransferType transType : TransferType.values())
                if(transType.valueOf()==(type&LibUsb.TRANSFER_TYPE_MASK))
                    return transType;

            return null;
        }

        public static class CODE{
            public static final int CONTROL = 0;
            public static final int ISOCHRONOUS = 1;
            public static final int BULK = 2;
            public static final int INTERRUPT = 3;
            public static final int BULK_STREAM = 4;
        }

    }

    public enum DeviceSpeed{

        Unknown(CODE.UNKNOWN, "Unknown"),
        Low(CODE.LOW, "Low"),
        Full(CODE.FULL, "Full"),
        High(CODE.HIGH, "High"),
        Super(CODE.SUPER, "Super");

        private int code;
        private String description;

        DeviceSpeed(int code, String desc){
            this.code = code;
            description = desc;
        }

        public int valueOf(){
            return code;
        }

        @Override
        public String toString(){
            return description;
        }

        public static DeviceSpeed getFromCode(int deviceSpeed) {
            for(DeviceSpeed devSpd : DeviceSpeed.values())
                if(devSpd.valueOf()==deviceSpeed)
                    return devSpd;

            return null;
        }

        public static class CODE{
            public static final int UNKNOWN = 0;
            public static final int LOW = 1;
            public static final int FULL = 2;
            public static final int HIGH = 3;
            public static final int SUPER = 4;
        }


    }

    public enum DeviceClass{

        PerInterface(Code.PER_INTERFACE, "Per interface"),
        Audio(Code.AUDIO, "Audio"),
        Comm(Code.COMM, "Communications"),
        Hid(Code.HID, "Human Interface Device"),
        Physical(Code.PHYSICAL, "Physical"),
        Printer(Code.PRINTER, "Printer"),
        PTP(Code.PTP, "Picture Transfer Protocol"),
        Image(Code.IMAGE, "Image"),
        MassStorage(Code.MASS_STORAGE, "Mass storage"),
        Hub(Code.HUB, "Hub"),
        Data(Code.DATA, "Data"),
        SmartCard(Code.SMART_CARD, "Smart card"),
        ContentSecurity(Code.CONTENT_SECURITY, "Content security"),
        Video(Code.VIDEO, "Video"),
        PersonalHealthcare(Code.PERSONAL_HEALTHCARE, "Peronal Health care"),
        DiagnosticDevice(Code.DIAGNOSTIC_DEVICE, "Diagnostic device"),
        Wireless(Code.WIRELESS, "Wireless"),
        Application(Code.APPLICATION, "Application"),
        VendorSpec(Code.VENDOR_SPEC, "Vendor specific");

        private int code;
        private String description;

        DeviceClass(int code, String desc){
            this.code = code;
            description = desc;
        }

        public int valueOf(){
            return code;
        }

        @Override
        public String toString(){
            return description;
        }

        public static DeviceClass getFromCode(int code){
            for(DeviceClass devCls : DeviceClass.values()){
                if(devCls.valueOf()==code)
                    return devCls;
            }
            return null;
        }

        public static class Code{
            public static final int PER_INTERFACE = 0;
            public static final int AUDIO = 1;
            public static final int COMM = 2;
            public static final int HID = 3;
            public static final int PHYSICAL = 5;
            public static final int PRINTER = 7;
            public static final int PTP = 6;
            public static final int IMAGE = 6;
            public static final int MASS_STORAGE = 8;
            public static final int HUB = 9;
            public static final int DATA = 10;
            public static final int SMART_CARD = 0x0b;
            public static final int CONTENT_SECURITY = 0x0d;
            public static final int VIDEO = 0x0e;
            public static final int PERSONAL_HEALTHCARE = 0x0f;
            public static final int DIAGNOSTIC_DEVICE = 0xdc;
            public static final int WIRELESS = 0xe0;
            public static final int APPLICATION = 0xfe;
            public static final int VENDOR_SPEC = 0xff;
        }

    }

    public static class TRANSFER_TYPE{
        public static final int CONTROL = 0;
        public static final int ISOCHRONOUS = 1;
        public static final int BULK = 2;
        public static final int INTERRUPT = 3;
        public static final int BULK_STREAM = 4;

        static String getString(int code){
            switch(code){
                case CONTROL: return "Control";
                case ISOCHRONOUS: return "Isochronous";
                case BULK: return "Bulk";
                case INTERRUPT: return "Interrupt";
                case BULK_STREAM: return "Bulk Stream";
                default: return "Unknown";
            }
        }
    }

    public static class ISO_SYNC_TYPE{

        public static final int NONE = 0;
        public static final int ASYNC = 1;
        public static final int ADAPTIVE = 2;
        public static final int SYNC = 3;

        static String getString(int code){
            switch(code){
                case NONE: return "None";
                case ASYNC: return "Async";
                case ADAPTIVE: return "Adaptive";
                case SYNC: return "Sync";
                default: return "Unknown";
            }
        }
    }

    public static class ISO_USAGE_TYPE{

        public static final int DATA = 0;
        public static final int FEEDBACK = 1;
        public static final int IMPLICIT = 2;

        static String getString(int code){
            switch(code){
                case DATA: return "Data";
                case FEEDBACK: return "Feedback";
                case IMPLICIT: return "Implicit";
                default: return "Unknown";
            }
        }
    }



}
