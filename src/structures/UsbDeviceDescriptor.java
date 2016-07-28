package structures;

import org.usb4java.DeviceDescriptor;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 28/07/2016.
 */
public class UsbDeviceDescriptor {

    UsbDevice parent;
    DeviceDescriptor desc;

    UsbDeviceDescriptor(UsbDevice dev){
        parent = dev;
        desc = new DeviceDescriptor();
        int retCode = LibUsb.getDeviceDescriptor(dev.dev, desc);
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }


    public int bLength(){
        return desc.bLength();
    }

    public int bDescriptorType(){
        return desc.bDescriptorType();
    }

    public int bcdUSB(){
        return desc.bcdUSB();
    }

    public float getUsbSpecificationReleaseNumber(){
        int[] digits = new int[4];
        int bcdUSB = desc.bcdUSB();
        for(int i=0; i<4; i++)
            digits[i] = (bcdUSB>>(i*4))&0x0F;
        float version = (float) (digits[0]*0.01+digits[1]*0.1+digits[2]*1+digits[3]*10);
        return version;
    }

    public int bDeviceClass(){
        return desc.bDeviceClass();
    }

    public TBD.DeviceClass getDeviceClass(){
        return TBD.DeviceClass.getFromCode(desc.bDeviceClass());
    }

    public int bDeviceSubclass(){
        return desc.bDeviceSubClass();
    }

    public int bDeviceProtocol(){
        return desc.bDeviceProtocol();
    }

    public int bMaxPacketSize0(){
        return desc.bMaxPacketSize0();
    }

    public int idVendor(){
        return desc.idVendor();
    }

    public int idProduct(){
        return desc.idProduct();
    }

    public int bcdDevice(){
        return desc.bcdDevice();
    }

    public float getDevReleaseNumber(){
        int[] digits = new int[4];
        int bcdDev = desc.bcdDevice();
        for(int i=0; i<4; i++)
            digits[i] = (bcdDev >>(i*4))&0x0F;
        float version = (float) (digits[0]*0.01+digits[1]*0.1+digits[2]*1+digits[3]*10);
        return version;
    }

    public int iManufacturer(){
        return desc.iManufacturer();
    }

    public String manufacturer(){
        if(parent.handle == null)
            throw new TBDRuntimeException(TBD.ERROR_CODE.OTHER, "Can't retrieve manufacturer string. Device not opened.");
        return LibUsb.getStringDescriptor(parent.handle, desc.iManufacturer());
    }

    public int iProduct(){
        return desc.iProduct();
    }

    public String product(){
        if(parent.handle == null)
            throw new TBDRuntimeException(TBD.ERROR_CODE.OTHER, "Can't retrieve product string. Device not opened.");
        return LibUsb.getStringDescriptor(parent.handle, desc.iProduct());
    }

    public int iSerialNumber(){
        return desc.iSerialNumber();
    }

    public String serialNumber(){
        if(parent.handle == null)
            throw new TBDRuntimeException(TBD.ERROR_CODE.OTHER, "Can't retrieve serial number. Device not opened.");
        return LibUsb.getStringDescriptor(parent.handle, desc.iSerialNumber());
    }

    public int bNumConfigurations(){
        return desc.bNumConfigurations();
    }

}
