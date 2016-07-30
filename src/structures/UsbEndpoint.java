package structures;

import org.usb4java.EndpointDescriptor;
import org.usb4java.LibUsb;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbEndpoint {

    EndpointDescriptor desc;
    UsbInterfaceAltSetting parent;

    protected UsbEndpoint(EndpointDescriptor epDesc, UsbInterfaceAltSetting parent){

        this.desc = epDesc;
        this.parent = parent;
    }

    public byte bLength() {
        return desc.bLength();
    }

    public byte bDescriptorType() {
        return desc.bDescriptorType();
    }

    public byte bEndpointAddress() {
        return desc.bEndpointAddress();
    }

    public int getNumber(){
        return desc.bEndpointAddress()&LibUsb.ENDPOINT_ADDRESS_MASK;
    }

    public boolean isEpIn(){
        int dir = desc.bEndpointAddress()&LibUsb.ENDPOINT_DIR_MASK;
        return dir==LibUsb.ENDPOINT_IN;
    }

    public byte bmAttributes() {
        return desc.bmAttributes();
    }

    public TBD.TransferType getTransferType(){
        return TBD.TransferType.getFromCode(bmAttributes());
    }

    public int getIsoSyncType(){
        int type = desc.bmAttributes()&LibUsb.ISO_SYNC_TYPE_MASK;
        type >>= 2;
        return type;
    }

    public int getIsoUsageType(){
        int type = desc.bmAttributes()&LibUsb.ISO_USAGE_TYPE_MASK;
        type >>= 4;
        return type;
    }

    public short wMaxPacketSize() {
        return desc.wMaxPacketSize();
    }

    public byte bInterval() {
        return desc.bInterval();
    }

    public byte bRefresh() {
        return desc.bRefresh();
    }

    public byte bSynchAddress() {
        return desc.bSynchAddress();
    }

    public ByteBuffer extra() {
        return desc.extra();
    }

    public int extraLength() {
        return desc.extraLength();
    }

    public int[] readBulk(int len) throws TBDException {
        ByteBuffer buff = ByteBuffer.allocateDirect(len);
        int retLen = transferBulk(buff);
        int[] ret = new int[retLen];
        for(int i=0; i<retLen; i++)
            ret[i] = buff.get(i);
        return ret;
    }

    public void writeBulk(int[] data) throws TBDException {
        ByteBuffer buff = ByteBuffer.allocateDirect(data.length);
        int retLen = transferBulk(buff);
    }

    public int transferBulk(ByteBuffer data) throws TBDException {
        IntBuffer transferred = IntBuffer.allocate(1);
        int retCode = LibUsb.bulkTransfer(parent.parent.parent.parent.handle, bEndpointAddress(), data, transferred, 2000);
        if(retCode==TBD.ERROR_CODE.SUCCESS){
            return transferred.get();
        }
        else
            throw new TBDException(retCode);
    }

    public int getMaxPacketSize(){
        return LibUsb.getMaxPacketSize(parent.parent.parent.parent.dev, bEndpointAddress());
    }

    public int getMaxIsoPacketSize(){
        return LibUsb.getMaxIsoPacketSize(parent.parent.parent.parent.dev, bEndpointAddress());
    }

    public void clearHalt(){
        int retCode = LibUsb.clearHalt(parent.parent.parent.parent.handle, bEndpointAddress());
        if(retCode!=TBD.ERROR_CODE.SUCCESS)
            throw new TBDRuntimeException(retCode);
    }

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Address", String.format("%02x", desc.bEndpointAddress()));
        sb.append("\tNumber", getNumber());
        sb.append("\tType", isEpIn()? "EP-IN": "EP-OUT");
        sb.append("Attributes", String.format("%02x", desc.bmAttributes()));
        sb.append("\tType", getTransferType().toString());
        if(getTransferType().equals(TBD.TransferType.Isochronous)){
            sb.append("\tIso Sync Type", TBD.ISO_SYNC_TYPE.getString(getIsoSyncType()));
            sb.append("\tIso Usage Type", TBD.ISO_USAGE_TYPE.getString(getIsoUsageType()));
        }
        else
            sb.append("\tReserved", String.format("%02x", desc.bmAttributes()>>2));

        sb.append("Max packet size", desc.wMaxPacketSize());
        sb.append("Polling interval", desc.bInterval());
        sb.append("Refresh rate (for audio)", desc.bRefresh());
        sb.append("Sync address (for audio)", desc.bSynchAddress());
        return sb.toString();
    }

}
