package main;

import structures.TBD;
import structures.UsbDevice;
import structures.UsbDeviceList;

import javax.swing.*;

/**
 * Created by Marius on 24/07/2016.
 */
public class Main {

    private JTree tree1;
    private JTable table1;
    private JButton openButton;
    private JPanel jPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        UsbDeviceList devices = TBD.getUsbDevices();
        for(UsbDevice dev : devices){
            System.out.println(dev.getActiveConfigDescriptor());
        }

    }


}
