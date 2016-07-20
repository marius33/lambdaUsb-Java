package structures;

/**
 * Created by Radu on 19/07/2016.
 */
public class BosDescriptor {

    private org.usb4java.BosDescriptor libusbBosDescriptor;

    public int bLength(){
        return libusbBosDescriptor.bLength();
    }

}
