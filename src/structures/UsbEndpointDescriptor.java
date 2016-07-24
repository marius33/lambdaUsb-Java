package structures;

import org.usb4java.EndpointDescriptor;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbEndpointDescriptor {

    private EndpointDescriptor libusb_epDesc;

    protected UsbEndpointDescriptor(EndpointDescriptor epDesc){
        libusb_epDesc = epDesc;
    }

    public EndpointDescriptor toLibUsb(){
        return libusb_epDesc;
    }

    public int getAddress(){
        return libusb_epDesc.bEndpointAddress();
    }

    public int getNumber(){
        return libusb_epDesc.bEndpointAddress()&LibUsb.ENDPOINT_ADDRESS_MASK;
    }

    public boolean isEpIn(){
        int dir = libusb_epDesc.bEndpointAddress()&LibUsb.ENDPOINT_DIR_MASK;
        return dir==LibUsb.ENDPOINT_IN;
    }

    public int getTransferType(){
        return libusb_epDesc.bmAttributes()&LibUsb.TRANSFER_TYPE_MASK;
    }

    public int getIsoSyncType(){
        int type = libusb_epDesc.bmAttributes()&LibUsb.ISO_SYNC_TYPE_MASK;
        type >>= 2;
        return type;
    }

    public int getIsoUsageType(){
        int type = libusb_epDesc.bmAttributes()&LibUsb.ISO_USAGE_TYPE_MASK;
        type >>= 4;
        return type;
    }

    public int getMaximumPacketSize(){
        return libusb_epDesc.wMaxPacketSize();
    }

}
