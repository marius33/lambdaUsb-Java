package main;

import structures.TBD;
import structures.UsbDevice;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * Created by Marius on 24/07/2016.
 */
public class Main {

    private JTree tree;
    private DefaultMutableTreeNode treeRoot;
    private DefaultTreeModel treeModel;
    private JPanel jPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton open;

    public Main() {

        /*createUIComponents()*/

        for(UsbDevice dev : TBD.getUsbDevices()){
            treeRoot.add(new UsbTreeNode(dev, String.format("PID: %04x", dev.pid())));
        }
        treeModel.reload();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        Main m = new Main();
        frame.setContentPane(m.jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
                if(node==null) return;
                
                System.out.println(node.getUserObject());
                fillTable();

            }
        });
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        open = new JButton();
    }

    private void fillTable(){

        UsbDevice.class.getDeclaredFields()[0].getName();

    }

}
