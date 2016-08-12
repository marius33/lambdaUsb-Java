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

    public byte address() {
        return desc.bEndpointAddress();
    }

    public byte number() {
        return (byte) (desc.bEndpointAddress() & LibUsb.ENDPOINT_ADDRESS_MASK);
    }

    public boolean isEpIn() {
        int dir = desc.bEndpointAddress() & LibUsb.ENDPOINT_DIR_MASK;
        return dir == LibUsb.ENDPOINT_IN;
    }

    public LambdaUsb.EndpointDirection direction() {

        return LambdaUsb.EndpointDirection.getFromCode(desc.bEndpointAddress());

    }

    public byte bmAttributes() {
        return desc.bmAttributes();
    }

    public LambdaUsb.TransferType transferType() {

        return LambdaUsb.TransferType.getFromCode(bmAttributes());

    }

    public LambdaUsb.IsoSyncType isoSyncType() {

        return LambdaUsb.IsoSyncType.getFromCode(bmAttributes());

    }

    public LambdaUsb.IsoUsageType isoUsageType() {

        return LambdaUsb.IsoUsageType.getFromCode(bmAttributes());

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
	
	public byte[] extra(){
		return desc.extra().array();
	}

    public byte[] transfer(LambdaBytes o) throws LambdaUsbException {
        return transferSync(o.toByteArray(), 0);
    }

    public byte[] transferSync(byte[] data, long timeout) throws LambdaUsbException {
        if (transferType().equals(LambdaUsb.TransferType.Bulk)) {
            ByteBuffer buff = ByteBuffer.allocateDirect(data.length);
            buff.put(data);
            IntBuffer transferred = IntBuffer.allocate(1);
            int retCode = LibUsb.bulkTransfer(parent.parent.parent.parent.handle, address(), buff, transferred, timeout);
            if (retCode == LambdaUsb.Error.Success.valueOf()) {
                int transf = transferred.get();
                for (int i = 0; i < transf; i++)
                    data[i] = buff.get(i);
                return data;
            } else
                throw new LambdaUsbException(retCode);
        }

        else if (transferType().equals(LambdaUsb.TransferType.Control)) {
            byte bmRequestType = data[0];
            byte bRequest = data[1];
            short wValue = data[2] |= (data[3] << 8);
            short wIndex = data[4] |= (data[5] << 8);
            ByteBuffer buff = ByteBuffer.allocateDirect(data.length - 6);
            int wLength = data.length - 4;
            int retCode = LibUsb.controlTransfer(parent.parent.parent.parent.handle,
                    bmRequestType, bRequest, wValue, wIndex, buff, timeout);
            if (retCode == LambdaUsb.Error.Success.valueOf()) {
                byte[] ret = new byte[wLength];
                for (int i = 0; i < wLength; i++)
                    ret[i] = buff.get(i);
                return ret;
            } else
                throw new LambdaUsbException(retCode);
        }

        else if (transferType().equals(LambdaUsb.TransferType.Interrupt)) {
            ByteBuffer buff = ByteBuffer.allocateDirect(data.length);
            buff.put(data);
            IntBuffer transferred = IntBuffer.allocate(1);
            int retCode = LibUsb.interruptTransfer(parent.parent.parent.parent.handle, address(), buff, transferred, timeout);
            if (retCode == LambdaUsb.Error.Success.valueOf()) {
                int transf = transferred.get();
                for (int i = 0; i < transf; i++)
                    data[i] = buff.get(i);
                return data;
            } else
                throw new LambdaUsbException(retCode);
        }

        return null;
    }
	
    public int maxPacketSize() {
        return LibUsb.getMaxPacketSize(parent.parent.parent.parent.dev, address());
    }

    public int maxIsoPacketSize() {
        return LibUsb.getMaxIsoPacketSize(parent.parent.parent.parent.dev, address());
    }

    public void clearHalt() {
        int retCode = LibUsb.clearHalt(parent.parent.parent.parent.handle, address());
        if (retCode != LambdaUsb.Error.Success.valueOf())
            throw new LambdaUsbRuntimeException(retCode);
    }

    @Override
    public String toString() {
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Address", String.format("%02x", address()));
        sb.append("\tNumber", number());
        sb.append("\tDirection", direction());
        sb.append("Attributes", String.format("%02x", bmAttributes()));
        sb.append("\tType", transferType());
        if (transferType().equals(LambdaUsb.TransferType.Isochronous)) {
            sb.append("\tIso Sync Type", getIsoSyncType());
            sb.append("\tIso Usage Type", getIsoUsageType());
        } else
            sb.append("\tReserved", String.format("%02x", desc.bmAttributes() >> 2));

        sb.append("Max packet size", maxPacketSize());
        sb.append("Polling interval", desc.bInterval());
        sb.append("Refresh rate (for audio)", desc.bRefresh());
        sb.append("Sync address (for audio)", desc.bSynchAddress());
        return sb.toString();
    }

}

