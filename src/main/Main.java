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

    private JTree tree;
    private DefaultMutableTreeNode treeRoot;
    private DefaultTreeModel treeModel;
    private JPanel jPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton open;

    public Main() {

        /*createUIComponents()*/

        for (UsbDevice dev : LambdaUsb.getUsbDevices().filter(ATMEL_VID, -1)) {
            UsbTreeNode devNode = new UsbTreeNode(dev, String.format("PID: %04x", dev.pid()));
            treeRoot.add(devNode);
        }
        treeModel.reload();
    }

    public static void main(String[] args) {
//        JFrame frame = new JFrame("Main");
//        Main m = new Main();
//        frame.setContentPane(m.jPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);

        UsbDevice dev = LambdaUsb.getUsbDevices().filter(ATMEL_VID, -1).get(0);
        System.out.println(dev);
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
            UsbInterfaceAltSetting ifDesc1 = if1.getSetting(0);
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
            System.out.println(if1.new int[]{33}, 2));
            dev.releaseInterface(1);



        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            dev.close();
        }

    }

    private void createUIComponents() {
        treeRoot = new DefaultMutableTreeNode("USB Devices");
        treeModel = new DefaultTreeModel(treeRoot);
        tree = new JTree(treeModel);
        tree.setRootVisible(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                UsbTreeNode node = (UsbTreeNode) tree.getLastSelectedPathComponent();
                if (node == null) return;
                System.out.println(node);
                populateNode(node);
                treeModel.nodeChanged(node);

                System.out.println(node.getUserObject());
                fillTable();

            }
        });
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        open = new JButton();
    }

    private void populateNode(UsbTreeNode deviceNode) {

        Object obj = deviceNode.getUserObject();

        LinkedList<Field> fields = new LinkedList(Arrays.asList(obj.getClass().getFields()));


        for(Field field : fields){
            if(!((field.getType().isPrimitive() && !field.getType().equals(Void.class)) || field.getType().equals(String.class))){
                try {
                    deviceNode.add(new UsbTreeNode(field.get(obj), field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    deviceNode.add(new UsbTreeNode(null, field.getName()));
                }
            }
        }

        LinkedList<Method> methods = new LinkedList(Arrays.asList(obj.getClass().getMethods()));
        for(Method method : methods){
            if(!((method.getReturnType().isPrimitive() && !method.getReturnType().equals(Void.class)) || method.getReturnType().equals(String.class))){
                if(method.getParameterCount()==0){
                    Object ret = null;
                    System.out.println(method.getName());
                    try {
                        ret = method.invoke(obj, null);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally{
                        deviceNode.add(new UsbTreeNode(ret, method.getName()));
                    }
                }
            }
        }

    }

    private void fillTable() {

        UsbDevice.class.getDeclaredFields()[0].getName();

    }

}
