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

    private LambdaUsb() {}

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

    public enum BosType {

        WirelessUsbDeviceCapability(CODE.WIRELESS_USB_DEVICE_CAPABILITY, "Wireless Usb Device Capability"),
        Usb20Extention(CODE.USB_2_0_EXTENSION, "Usb 2.0 Extension"),
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
            public static final int WIRELESS_USB_DEVICE_CAPABILITY = 1;
            public static final int USB_2_0_EXTENSION = 2;
            public static final int SS_USB_DEVICE_CAPABILITY = 3;
            public static final int CONTAINER_ID = 4;
        }
    }

    public enum Capability {

        HasCapability(CODE.HAS_CAPABILITY, "Has capability"),
        HasHotplug(CODE.HAS_HOTPLUG, "Has hotplug"),
        HasHidAccess(CODE.HAS_HID_ACCESS, "Has HID access"),
        SupportsDetachKernelDriver(CODE.SUPPORTS_DETACH_KERNEL_DRIVER, "Supports detaching of kernel driver");

        private int code;
        private String description;

        Capability(int code, String desc) {
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

        public static Capability getFromCode(int type) {
            for (Capability o : Capability.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {
            public static final int HAS_CAPABILITY = 0;
            public static final int HAS_HOTPLUG = 1;
            public static final int HAS_HID_ACCESS = 0x0100;
            public static final int SUPPORTS_DETACH_KERNEL_DRIVER = 0x0101;
        }
    }

    public enum DescriptorType {

        Device(CODE.DEVICE, "Device"),
        Config(CODE.CONFIG, "Configuration"),
        String(CODE.STRING, "String"),
        Interface(CODE.INTERFACE, "Interface"),
        Endpoint(CODE.ENDPOINT, "Endpoint"),
        Bos(CODE.BOS, "BOS"),
        DeviceCapability(CODE.DEVICE_CAPABILITY, "Device capability"),
        Hid(CODE.HID, "HID"),
        Report(CODE.REPORT, "Report"),
        Physical(CODE.PHYSICAL, "Physical"),
        Hub(CODE.HUB, "Hub"),
        SuperSpeedHub(CODE.SUPERSPEED_HUB, "SuperSpeed hub"),
        SuperSpeedEndpointCompanion(CODE.SS_ENDPOINT_COMPANION, "SuperSpeed endpoint companion");

        private int code;
        private String description;

        DescriptorType(int code, String desc) {
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

        public static DescriptorType getFromCode(int type) {
            for (DescriptorType o : DescriptorType.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {
            public static final int DEVICE = 0x01;
            public static final int CONFIG = 0x02;
            public static final int STRING = 0x03;
            public static final int INTERFACE = 0x04;
            public static final int ENDPOINT = 0x05;
            public static final int BOS = 0x0f;
            public static final int DEVICE_CAPABILITY = 0x10;
            public static final int HID = 0x21;
            public static final int REPORT = 0x22;
            public static final int PHYSICAL = 0x23;
            public static final int HUB = 0x29;
            public static final int SUPERSPEED_HUB = 0x2a;
            public static final int SS_ENDPOINT_COMPANION = 0x30;
        }
    }

    public enum EndpointDirection {

        In(CODE.IN, "EP-IN"),
        Out(CODE.OUT, "EP-OUT");

        private int code;
        private String description;

        EndpointDirection(int code, String desc) {
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

        public static EndpointDirection getFromCode(int type) {
            for (EndpointDirection o : EndpointDirection.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int IN = 0x80;
            public static final int OUT = 0x00;

        }
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

    public enum IsoSyncType{

        None(CODE.NONE, "None"),
        Async(CODE.ASYNC, "Asynchronous"),
        Adaptive(CODE.ADAPTIVE, "Adaptive"),
        Synchronous(CODE.SYNC, "Synchronous");


        private int code;
        private String description;

        IsoSyncType(int code, String desc) {
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

        public static IsoSyncType getFromCode(int code) {
            for (IsoSyncType o : IsoSyncType.values()) {
                if (o.valueOf() == code)
                    return o;
            }
            return null;
        }

        public static class CODE{
            public static final int NONE = 0;
            public static final int ASYNC = 1;
            public static final int ADAPTIVE = 2;
            public static final int SYNC = 3;
        }

    }

    public enum IsoUsageType{

        Data(CODE.DATA, "Data"),
        Feedback(CODE.FEEDBACK, "Feedback"),
        Implicit(CODE.IMPLICIT, "Implicit");

        private int code;
        private String description;

        IsoUsageType(int code, String desc){
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

        public static IsoUsageType getFromCode(int code){
            for(IsoUsageType o : IsoUsageType.values())
                if(o.valueOf() == code)
                    return o;

            return null;
        }

        public static class CODE{
            public static final int DATA = 0;
            public static final int FEEDBACK = 1;
            public static final int IMPLICIT = 2;
        }


    }

    public enum RequestRecipient {

        Device(CODE.DEVICE, "Device"),
        Interface(CODE.INTERFACE, "Interface"),
        Endpoint(CODE.ENDPOINT, "Endpoint"),
        Other(CODE.OTHER, "Other");

        private int code;
        private String description;

        RequestRecipient(int code, String desc) {
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

        public static RequestRecipient getFromCode(int type) {
            for (RequestRecipient o : RequestRecipient.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int DEVICE = 0x00;
            public static final int INTERFACE = 0x01;
            public static final int ENDPOINT = 0x02;
            public static final int OTHER = 0x03;

        }
    }

    public enum RequestType {

        Standard(CODE.STANDARD, "Standard"),
        Class(CODE.CLASS, "Class"),
        Vendor(CODE.VENDOR, "Vendor"),
        Reserved(CODE.RESERVED, "Reserved");

        private int code;
        private String description;

        RequestType(int code, String desc) {
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

        public static RequestType getFromCode(int type) {
            for (RequestType o : RequestType.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int STANDARD = 0x00;
            public static final int CLASS = 0x20;
            public static final int VENDOR = 0x40;
            public static final int RESERVED = 0x60;

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

    public enum SuperSpeedExtensionAttribute {

        LTMSupport(CODE.LTM_SUPPORT, "Latency Tolerance Messages support");

        private int code;
        private String description;

        SuperSpeedExtensionAttribute(int code, String desc) {
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

        public static SuperSpeedExtensionAttribute getFromCode(int type) {
            for (SuperSpeedExtensionAttribute o : SuperSpeedExtensionAttribute.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int LTM_SUPPORT = 2;


        }
    }

    public enum StandardRequest {

        GetStatus(CODE.GET_STATUS, "Get status"),
        ClearFeature(CODE.CLEAR_FEATURE, "Clear feature"),
        SetFeature(CODE.SET_FEATURE, "Set feature"),
        SetAddress(CODE.SET_ADDRESS, "Set address"),
        GetDescriptor(CODE.GET_DESCRIPTOR, "Get descriptor"),
        SetDescriptor(CODE.SET_DESCRIPTOR, "Set descriptor"),
        GetConfiguration(CODE.GET_CONFIGURATION, "Get configuration"),
        SetConfiguration(CODE.SET_CONFIGURATION, "Set configuration"),
        GetInterface(CODE.GET_INTERFACE, "Get interface"),
        SetInterface(CODE.SET_INTERFACE, "Set interface"),
        SynchFrame(CODE.SYNCH_FRAME, "Synch frame"),
        SetSel(CODE.SET_SEL, "Set selection"),
        SEtIsochDelay(CODE.SET_ISOCH_DELAY, "Set isochronous delay");


        private int code;
        private String description;

        StandardRequest(int code, String desc) {
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

        public static StandardRequest getFromCode(int type) {
            for (StandardRequest o : StandardRequest.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int GET_STATUS = 0x00;
            public static final int CLEAR_FEATURE = 0x01;
            public static final int SET_FEATURE = 0x03;
            public static final int SET_ADDRESS = 0x05;
            public static final int GET_DESCRIPTOR = 0x06;
            public static final int SET_DESCRIPTOR = 0x07;
            public static final int GET_CONFIGURATION = 0x08;
            public static final int SET_CONFIGURATION = 0x09;
            public static final int GET_INTERFACE = 0x0A;
            public static final int SET_INTERFACE = 0x0B;
            public static final int SYNCH_FRAME = 0x0C;
            public static final int SET_SEL = 0x30;
            public static final int SET_ISOCH_DELAY = 0x31;


        }
    }

    public enum SupportedSpeed {

        LowSpeed(CODE.LOW_SPEED_OPERATION, "Low speed operation"),
        FullSpeed(CODE.FULL_SPEED_OPERATION, "Full speed operation"),
        HighSpeed(CODE.HIGH_SPEED_OPERATION, "High speed operation"),
        SuperSpeedOperation(CODE.SUPER_SPEED_OPERATION, "SuperSpeed operation");

        private int code;
        private String description;

        SupportedSpeed(int code, String desc) {
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

        public static SupportedSpeed getFromCode(int type) {
            for (SupportedSpeed o : SupportedSpeed.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int LOW_SPEED_OPERATION = 1;
            public static final int FULL_SPEED_OPERATION = 2;
            public static final int HIGH_SPEED_OPERATION = 4;
            public static final int SUPER_SPEED_OPERATION = 8;

        }
    }

    public enum TransferFlag {

        ShortNOK(CODE.SHORT_NOT_OK, "Short NOT OK"),
        FreeBuffer(CODE.FREE_BUFFER, "Free buffer"),
        FreeTransfer(CODE.FREE_TRANSFER, "Free transfer"),
        AddZeroPacket(CODE.ADD_ZERO_PACKET, "Add zero packet");

        private int code;
        private String description;

        TransferFlag(int code, String desc) {
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

        public static TransferFlag getFromCode(int type) {
            for (TransferFlag o : TransferFlag.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int SHORT_NOT_OK = 0;
            public static final int FREE_BUFFER = 1;
            public static final int FREE_TRANSFER = 2;
            public static final int ADD_ZERO_PACKET = 4;

        }
    }

    public enum TransferStatus {

        Completed(CODE.COMPLETED, "Completed"),
        Error(CODE.ERROR, "Error"),
        TimedOut(CODE.TIMED_OUT, "Timed out"),
        Cancelled(CODE.CANCELLED, "Cancelled"),
        Stall(CODE.STALL, "Stall"),
        NoDevice(CODE.NO_DEVICE, "No device"),
        Overflow(CODE.OVERFLOW, "Overflow");

        private int code;
        private String description;

        TransferStatus(int code, String desc) {
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

        public static TransferStatus getFromCode(int type) {
            for (TransferStatus o : TransferStatus.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int COMPLETED = LibUsb.TRANSFER_COMPLETED;
            public static final int ERROR = LibUsb.TRANSFER_ERROR;
            public static final int TIMED_OUT = LibUsb.TRANSFER_TIMED_OUT;
            public static final int CANCELLED = LibUsb.TRANSFER_CANCELLED;
            public static final int STALL = LibUsb.TRANSFER_STALL;
            public static final int NO_DEVICE = LibUsb.TRANSFER_NO_DEVICE;
            public static final int OVERFLOW = LibUsb.TRANSFER_OVERFLOW;

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

    public enum Usb20ExtensionAttribute {

        LPMSupport(CODE.LPM_SUPPORT, "Link Power Management support");

        private int code;
        private String description;

        Usb20ExtensionAttribute(int code, String desc) {
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

        public static Usb20ExtensionAttribute getFromCode(int type) {
            for (Usb20ExtensionAttribute o : Usb20ExtensionAttribute.values())
                if (o.valueOf() == type)
                    return o;
            return null;
        }

        public static class CODE {

            public static final int LPM_SUPPORT = 2;


        }
    }
    
}

