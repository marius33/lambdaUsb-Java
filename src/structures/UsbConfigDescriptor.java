package structures;

import org.usb4java.ConfigDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;

/**
 * Created by Marius on 23/07/2016.
 */
public class UsbConfigDescriptor {

    protected ConfigDescriptor confDesc;
    protected UsbInterface[] interfaces;
    private String description;

    protected UsbConfigDescriptor(ConfigDescriptor confDesc, DeviceHandle handle){

        this.confDesc = confDesc;

        if(handle!=null)
            description = LibUsb.getStringDescriptor(handle, confDesc.iConfiguration());

        interfaces = new UsbInterface[confDesc.bNumInterfaces()];
        for(int i = 0; i<confDesc.bNumInterfaces(); i++)
            interfaces[i] = new UsbInterface(confDesc.iface()[i], handle);


    }

    public UsbInterface getInterface(int index){
        return interfaces[index];
    }

    public ConfigDescriptor getLibusb(){
        return confDesc;
    }

    public int configValue(){
        return confDesc.bConfigurationValue();
    }

    public int configAttributes(){
        return confDesc.bmAttributes();
    }

    public String description(){
        return description;
    }

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Configuration value", confDesc.bConfigurationValue());
        sb.append("Config description", description);
        sb.append("Attributes", confDesc.bmAttributes()&0xFF, "%02x");
        sb.append("\tSelf-powered", (confDesc.bmAttributes()&0x40)!=0);
        sb.append("\tRemote Wakeup", (confDesc.bmAttributes()&0x20)!=0);
        sb.append("Max power", confDesc.bMaxPower()*2 + "mA");
        sb.append("Number of ifaces", confDesc.bNumInterfaces());
        return sb.toString();
    }



}
