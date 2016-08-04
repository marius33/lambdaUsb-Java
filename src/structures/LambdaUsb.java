package structures;

import org.usb4java.*;

import java.util.ArrayList;

/**
 * Created by Marius on 24/07/2016.
 */
public final class LambdaUsb {

    private static ArrayList<HotplugListener> listeners;
    private static HotplugCallback hotplugCallback;
    private static HotplugCallbackHandle hotplugCallbackHandle;

    static {

        LibUsb.init(null);
        listeners = new ArrayList();
        hotplugCallback = new HotplugCallback() {
            @Override
            public int processEvent(Context context, Device device, int event, Object o) {

                UsbDevice dev = new UsbDevice(device);

                for (HotplugListener listener : listeners) {
                    if (listener.matches(dev)) {
                        if (event == LibUsb.HOTPLUG_EVENT_DEVICE_ARRIVED)
                            listener.onPlug(new UsbDevice(device));
                        else
                            listener.onUnplug(new UsbDevice(device));
                    }
                }
                return 1;
            }
        };
        hotplugCallbackHandle = new HotplugCallbackHandle();

        int retCode = LibUsb.hotplugRegisterCallback(
                null,
                LibUsb.HOTPLUG_EVENT_DEVICE_ARRIVED | LibUsb.HOTPLUG_EVENT_DEVICE_LEFT,
                0,
                LibUsb.HOTPLUG_MATCH_ANY,
                LibUsb.HOTPLUG_MATCH_ANY,
                LibUsb.HOTPLUG_MATCH_ANY,
                hotplugCallback,
                null,
                hotplugCallbackHandle
        );

    }


    private LambdaUsb() {
    }

    public static UsbDeviceList getUsbDevices() {

        DeviceList list = new DeviceList();
        int size = LibUsb.getDeviceList(null, list);
        UsbDeviceList devList = new UsbDeviceList(size, list);
        LibUsb.freeDeviceList(list, false);
        return devList;

    }

    public static void registerHotplugListener(HotplugListener listener) {
        listeners.add(listener);
    }

    public static void deregisterHotplugListener(HotplugListener listener) {
        listeners.remove(listener);
    }

    public enum Error {

        Success(CODE.SUCCESS, "No error"),
        IO(CODE.IO, "Input/Output error"),
        InvalidParam(CODE.INVALID_PARAM, "Invalid parameter"),
        Access(CODE.ACCESS, "Access denied (insufficient permissions)"),
        NoDevice(CODE.NO_DEVICE, "No such device (it may have been disconnected)"),
        NotFound(CODE.NOT_FOUND, "Entity not found"),
        Busy(CODE.BUSY, "Resource busy"),
        Timeout(CODE.TIMEOUT, "Operation timed out"),
        Overflow(CODE.OVERFLOW, "Overflow"),
        Pipe(CODE.PIPE, "Pipe error"),
        Interrupted(CODE.INTERRUPTED, "System call interrupted (perhaps due to signal)"),
        NoMem(CODE.NO_MEM, "Insufficient memory"),
        NotSupported(CODE.NOT_SUPPORTED, "Operation not supported or unimplemented on this platform"),
        Other(CODE.OTHER, "Other error");


        private int code;
        private String description;

        Error(int code, String desc) {
            this.code = code;
            description = desc;
        }

        public int valueOf() {
            return code;
        }

        @Override
        public String toString() {
            return description;
        }

        public static Error getFromCode(int type) {
            for (Error err : Error.values())
                if (err.valueOf() == type)
                    return err;
            return null;
        }

        public static class CODE {
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
        }
    }
    
    public enum BosType {

	WirelessUsbDeviceCapability(CODE.WIRELESS_USB_DEVICE_CAPABILITY, "Wireless Usb Device Capability"),
	Usb20Extention(CODE.USB_2_0_EXTENTION, "Usb 2.0 Extension"),
	SuperSpeedUsbDeviceCapability(CODE.SS_USB_DEVICE_CAPABILITY, "SuperSpeed Usb Device Capability"),
	ContainerID(CODE.CONTAINER_ID, "Container ID");

        private int code;
        private String description;

        BosType(int code, String desc) {
            this.code = code;
            description = desc;
        }

        public int valueOf() {
            return code;
        }

        @Override
        public String toString() {
            return description;
        }

        public static BosType getFromCode(int type) {
            for (BosType o : BosType.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {
            public static final int WIRELESS_USB_DEVICE_CAPABILITY = 1
            public static final int USB_2_0_EXTENSION = 2;
            public static final int SS_USB_DEVICE_CAPABILITY = 3;
            public static final int CONTAINER_ID = 4;
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

        TransferType(int code, String desc) {
            this.code = code;
            description = desc;
        }

        public int valueOf() {
            return code;
        }

        @Override
        public String toString() {
            return description;
        }

        public static TransferType getFromCode(int type) {
            for (TransferType transType : TransferType.values())
                if (transType.valueOf() == (type & LibUsb.TRANSFER_TYPE_MASK))
                    return transType;

            return null;
        }

        public static class CODE {
            public static final int CONTROL = 0;
            public static final int ISOCHRONOUS = 1;
            public static final int BULK = 2;
            public static final int INTERRUPT = 3;
            public static final int BULK_STREAM = 4;
        }

    }

    public enum DeviceSpeed {

        Unknown(CODE.UNKNOWN, "Unknown"),
        Low(CODE.LOW, "Low"),
        Full(CODE.FULL, "Full"),
        High(CODE.HIGH, "High"),
        Super(CODE.SUPER, "Super");

        private int code;
        private String description;

        DeviceSpeed(int code, String desc) {
            this.code = code;
            description = desc;
        }

        public int valueOf() {
            return code;
        }

        @Override
        public String toString() {
            return description;
        }

        public static DeviceSpeed getFromCode(int deviceSpeed) {
            for (DeviceSpeed devSpd : DeviceSpeed.values())
                if (devSpd.valueOf() == deviceSpeed)
                    return devSpd;

            return null;
        }

        public static class CODE {
            public static final int UNKNOWN = 0;
            public static final int LOW = 1;
            public static final int FULL = 2;
            public static final int HIGH = 3;
            public static final int SUPER = 4;
        }


    }

    public enum DeviceClass {

        PerInterface(CODE.PER_INTERFACE, "Per interface"),
        Audio(CODE.AUDIO, "Audio"),
        Comm(CODE.COMM, "Communications"),
        Hid(CODE.HID, "Human Interface Device"),
        Physical(CODE.PHYSICAL, "Physical"),
        Printer(CODE.PRINTER, "Printer"),
        PTP(CODE.PTP, "Picture Transfer Protocol"),
        Image(CODE.IMAGE, "Image"),
        MassStorage(CODE.MASS_STORAGE, "Mass storage"),
        Hub(CODE.HUB, "Hub"),
        Data(CODE.DATA, "Data"),
        SmartCard(CODE.SMART_CARD, "Smart card"),
        ContentSecurity(CODE.CONTENT_SECURITY, "Content security"),
        Video(CODE.VIDEO, "Video"),
        PersonalHealthcare(CODE.PERSONAL_HEALTHCARE, "Peronal Health care"),
        DiagnosticDevice(CODE.DIAGNOSTIC_DEVICE, "Diagnostic device"),
        Wireless(CODE.WIRELESS, "Wireless"),
        Application(CODE.APPLICATION, "Application"),
        VendorSpec(CODE.VENDOR_SPEC, "Vendor specific"),
        Any(CODE.ANY, "Any");

        private int code;
        private String description;

        DeviceClass(int code, String desc) {
            this.code = code;
            description = desc;
        }

        public int valueOf() {
            return code;
        }

        @Override
        public String toString() {
            return description;
        }

        public static DeviceClass getFromCode(int code) {
            for (DeviceClass devCls : DeviceClass.values()) {
                if (devCls.valueOf() == code)
                    return devCls;
            }
            return null;
        }

        public static class CODE {
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
            public static final int ANY = -1;
        }

    }

    public static class TRANSFER_TYPE {
        public static final int CONTROL = 0;
        public static final int ISOCHRONOUS = 1;
        public static final int BULK = 2;
        public static final int INTERRUPT = 3;
        public static final int BULK_STREAM = 4;

        static String getString(int code) {
            switch (code) {
                case CONTROL:
                    return "Control";
                case ISOCHRONOUS:
                    return "Isochronous";
                case BULK:
                    return "Bulk";
                case INTERRUPT:
                    return "Interrupt";
                case BULK_STREAM:
                    return "Bulk Stream";
                default:
                    return "Unknown";
            }
        }
    }

    public static class ISO_SYNC_TYPE {

        public static final int NONE = 0;
        public static final int ASYNC = 1;
        public static final int ADAPTIVE = 2;
        public static final int SYNC = 3;

        static String getString(int code) {
            switch (code) {
                case NONE:
                    return "None";
                case ASYNC:
                    return "Async";
                case ADAPTIVE:
                    return "Adaptive";
                case SYNC:
                    return "Sync";
                default:
                    return "Unknown";
            }
        }
    }

    public static class ISO_USAGE_TYPE {

        public static final int DATA = 0;
        public static final int FEEDBACK = 1;
        public static final int IMPLICIT = 2;

        static String getString(int code) {
            switch (code) {
                case DATA:
                    return "Data";
                case FEEDBACK:
                    return "Feedback";
                case IMPLICIT:
                    return "Implicit";
                default:
                    return "Unknown";
            }
        }
    }


}
