package main;

import structures.TBD;
import structures.UsbDevice;
import structures.UsbDeviceList;

/**
 * Created by Marius on 24/07/2016.
 */
public class Main {

    public static void main(String[] args){

        UsbDeviceList devices = TBD.getUsbDevices();
        for(UsbDevice dev : devices){
            System.out.println(dev);
        }

    }


}
