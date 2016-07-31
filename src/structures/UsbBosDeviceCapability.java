package structures;

import org.usb4java.BosDevCapabilityDescriptor;

/**
 * Created by Marius on 23/07/2016.
 */
public class UsbBosDeviceCapability {


    protected BosDevCapabilityDescriptor libusb_bosDevCapDesc;
    protected int type;
    protected int[] data;

    public UsbBosDeviceCapability(BosDevCapabilityDescriptor capability){
        libusb_bosDevCapDesc = capability;
        type = capability.bDevCapabilityType();
        data = new int[capability.bLength()-3];
        for(int i=0; i<data.length; i++)
            data[i] = capability.devCapabilityData().get(i);
    }

    public int type(){
        return type;
    }

    public int[] data() {
        return data;
    }

}
