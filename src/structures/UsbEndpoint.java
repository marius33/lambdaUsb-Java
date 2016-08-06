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

    protected UsbEndpoint(EndpointDescriptor epDesc, UsbInterfaceAltSetting parent) {

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

    public int getNumber() {
        return desc.bEndpointAddress() & LibUsb.ENDPOINT_ADDRESS_MASK;
    }

    public boolean isEpIn() {
        int dir = desc.bEndpointAddress() & LibUsb.ENDPOINT_DIR_MASK;
        return dir == LibUsb.ENDPOINT_IN;
    }

    public byte bmAttributes() {
        return desc.bmAttributes();
    }

    public LambdaUsb.TransferType getTransferType() {
        return LambdaUsb.TransferType.getFromCode(bmAttributes());
    }

    public int getIsoSyncType() {
        int type = desc.bmAttributes() & LibUsb.ISO_SYNC_TYPE_MASK;
        type >>= 2;
        return type;
    }

    public int getIsoUsageType() {
        int type = desc.bmAttributes() & LibUsb.ISO_USAGE_TYPE_MASK;
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

    public byte[] transfer(LambdaBytes o) throws LambdaUsbException {
        return transferSync(o.toByteArray());
    }

    public byte[] transferSync(byte[] data) throws LambdaUsbException {
        if (getTransferType().equals(LambdaUsb.TransferType.Bulk)) {
            ByteBuffer buff = ByteBuffer.allocateDirect(data.length);
            buff.put(data);
            IntBuffer transferred = IntBuffer.allocate(1);
            int retCode = LibUsb.bulkTransfer(parent.parent.parent.parent.handle, bEndpointAddress(), buff, transferred, 2000);
            if (retCode == LambdaUsb.Error.Success.valueOf()) {
                int transf = transferred.get();
                for (int i = 0; i < transf; i++)
                    data[i] = buff.get(i);
                return data;
            } else
                throw new LambdaUsbException(retCode);
        }

        else if (getTransferType().equals(LambdaUsb.TransferType.Control)) {


        }

        else if (getTransferType().equals(LambdaUsb.TransferType.Interrupt)) {

        }

        return null;
    }

    public int getMaxPacketSize() {
        return LibUsb.getMaxPacketSize(parent.parent.parent.parent.dev, bEndpointAddress());
    }

    public int getMaxIsoPacketSize() {
        return LibUsb.getMaxIsoPacketSize(parent.parent.parent.parent.dev, bEndpointAddress());
    }

    public void clearHalt() {
        int retCode = LibUsb.clearHalt(parent.parent.parent.parent.handle, bEndpointAddress());
        if (retCode != LambdaUsb.Error.Success.valueOf())
            throw new LambdaUsbRuntimeException(retCode);
    }

    @Override
    public String toString() {
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Address", String.format("%02x", desc.bEndpointAddress()));
        sb.append("\tNumber", getNumber());
        sb.append("\tType", isEpIn() ? "EP-IN" : "EP-OUT");
        sb.append("Attributes", String.format("%02x", desc.bmAttributes()));
        sb.append("\tType", getTransferType().toString());
        if (getTransferType().equals(LambdaUsb.TransferType.Isochronous)) {
            sb.append("\tIso Sync Type", getIsoSyncType());
            sb.append("\tIso Usage Type", getIsoUsageType());
        } else
            sb.append("\tReserved", String.format("%02x", desc.bmAttributes() >> 2));

        sb.append("Max packet size", desc.wMaxPacketSize());
        sb.append("Polling interval", desc.bInterval());
        sb.append("Refresh rate (for audio)", desc.bRefresh());
        sb.append("Sync address (for audio)", desc.bSynchAddress());
        return sb.toString();
    }

}
