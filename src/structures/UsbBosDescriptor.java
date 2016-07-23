package structures;

import org.usb4java.BosDescriptor;
import org.usb4java.BosDevCapabilityDescriptor;
import org.usb4java.LibUsb;

import java.util.List;

/**
 * Created by Marius on 23/07/2016.
 */
public class UsbBosDescriptor {

    protected BosDescriptor libusb_bosDesc;
    protected UsbBosDeviceCapability[] bosDeviceCapabilities;

    protected UsbBosDescriptor(BosDescriptor desc){

        libusb_bosDesc = desc;
        BosDevCapabilityDescriptor[] caps = desc.devCapability();
        bosDeviceCapabilities = new UsbBosDeviceCapability[desc.bNumDeviceCaps()];
        for(int i=0; i<desc.bNumDeviceCaps(); i++)
            bosDeviceCapabilities[i] = new UsbBosDeviceCapability(caps[i]);
    }

    public UsbBosDeviceCapability getCapability(int index){
        return bosDeviceCapabilities[index];
    }

    public int numDevCaps(){
        return bosDeviceCapabilities.length;
    }

    protected BosDescriptor toLibUsb(){
        return libusb_bosDesc;
    }

    protected void free(){
        LibUsb.freeBosDescriptor(libusb_bosDesc);
    }


}
