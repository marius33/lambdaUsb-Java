package structures;

import org.usb4java.DeviceHandle;
import org.usb4java.Interface;

/**
 * Created by Marius on 24/07/2016.
 */
public class UsbInterface {

    private UsbInterfaceDescriptor[] altSettings;
    private Interface iface;

    protected UsbInterface(Interface iface, DeviceHandle dev){
        this.iface = iface;
        altSettings = new UsbInterfaceDescriptor[iface.numAltsetting()];
        for(int i = 0; i<iface.numAltsetting(); i++)
            altSettings[i] = new UsbInterfaceDescriptor(iface.altsetting()[i], dev);
    }

    public UsbInterfaceDescriptor getSetting(int index){
        return altSettings[index];
    }

    public Interface toLibUsb(){
        return iface;
    }

    @Override
    public String toString(){
        StringStructureBuilder sb = new StringStructureBuilder();
        sb.append("Num. of alt. settings", iface.numAltsetting());
        return sb.toString();

    }


}
