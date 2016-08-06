package main;

import structures.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Marius on 24/07/2016.
 */
public class Main {

    private static final int ATMEL_VID = 0x03EB;
    private static final int ATMEL_PID = 0x2404;

    public static void main(String[] args) {

        UsbDevice dev = LambdaUsb.getUsbDevices().filter(ATMEL_VID, -1).get(0);
        try {
            dev.open();

            System.out.println("Active config descriptor");
            UsbConfigDescriptor activConfig = dev.getActiveConfigDescriptor();
            System.out.println(activConfig);
            System.out.println();

            System.out.println("Product string");
            System.out.print(dev.getDeviceDescriptor().product());
            System.out.println();

            System.out.println("Interface 0");
            UsbInterface if0 = activConfig.getInterface(0);
            System.out.println(if0);
            System.out.println();

            System.out.println("Alt setting 0");
            UsbInterfaceAltSetting ifDesc0 = if0.getAltSetting(0);
            System.out.println(ifDesc0);
            System.out.println();

            System.out.println("Endpoint 0");
            UsbEndpoint ep00 = ifDesc0.getEndpoint(0);
            System.out.println(ep00);
            System.out.println();

            System.out.println("Interface 1");
            UsbInterface if1 = activConfig.getInterface(1);
            System.out.println(if1);
            System.out.println();

            System.out.println("Alt setting 0");
            UsbInterfaceAltSetting ifDesc1 = if1.getAltSetting(0);
            System.out.println(ifDesc1);
            System.out.println();

            System.out.println("Endpoint 0");
            UsbEndpoint ep10 = ifDesc1.getEndpoint(0);
            System.out.println(ep10);
            System.out.println();

            System.out.println("Endpoint 1");
            UsbEndpoint ep11 = ifDesc1.getEndpoint(1);
            System.out.println(ep11);
            System.out.println();

            if1.claim();
            ep11.transferSync(new byte[]{33});
            if1.release();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            dev.close();
        }

    }

}
