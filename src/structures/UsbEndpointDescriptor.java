package structures;

import org.usb4java.EndpointDescriptor;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbEndpointDescriptor {

    private EndpointDescriptor epDesc;

    protected UsbEndpointDescriptor(EndpointDescriptor epDesc){
        this.epDesc = epDesc;
    }

    public EndpointDescriptor toLibUsb(){
        return epDesc;
    }

    public int getAddress(){
        return epDesc.bEndpointAddress();
    }

    public int getNumber(){
        return epDesc.bEndpointAddress()&LibUsb.ENDPOINT_ADDRESS_MASK;
    }

    public boolean isEpIn(){
        int dir = epDesc.bEndpointAddress()&LibUsb.ENDPOINT_DIR_MASK;
        return dir==LibUsb.ENDPOINT_IN;
    }

    public int getTransferType(){
        return epDesc.bmAttributes()&LibUsb.TRANSFER_TYPE_MASK;
    }

    public int getIsoSyncType(){
        int type = epDesc.bmAttributes()&LibUsb.ISO_SYNC_TYPE_MASK;
        type >>= 2;
        return type;
    }

    public int getIsoUsageType(){
        int type = epDesc.bmAttributes()&LibUsb.ISO_USAGE_TYPE_MASK;
        type >>= 4;
        return type;
    }

    public int getMaximumPacketSize(){
        return epDesc.wMaxPacketSize();
    }

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Address", String.format("%02x", epDesc.bEndpointAddress()));
        sb.append("\tNumber", getNumber());
        sb.append("\tType", isEpIn()? "EP-IN": "EP-OUT");
        sb.append("Attributes", String.format("%02x", epDesc.bmAttributes()));
        sb.append("\tType", TBD.TRANSFER_TYPE.getString(getTransferType()));
        if(getTransferType()==TBD.TRANSFER_TYPE.ISOCHRONOUS){
            sb.append("\tIso Sync Type", TBD.ISO_SYNC_TYPE.getString(getIsoSyncType()));
            sb.append("\tIso Usage Type", TBD.ISO_USAGE_TYPE.getString(getIsoUsageType()));
        }
        else
            sb.append("\tReserved", String.format("%02x", epDesc.bmAttributes()>>2));

        sb.append("Max packet size", epDesc.wMaxPacketSize());
        sb.append("Polling interval", epDesc.bInterval());
        sb.append("Refresh rate (for audio)", epDesc.bRefresh());
        sb.append("Sync address (for audio)", epDesc.bSynchAddress());
        return sb.toString();
    }

}
