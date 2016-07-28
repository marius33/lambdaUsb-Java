package main;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by Marius on 27/07/2016.
 */
public class UsbTreeNode implements MutableTreeNode {

    private MutableTreeNode parent;
    private Vector<MutableTreeNode> children;
    private Object userObject;
    private boolean allowsChildren;
    private String description;

    public UsbTreeNode(Object object, String desc, boolean allowChildren){
        userObject = object;
        description = desc;
        allowsChildren = allowChildren;
        children = new Vector(0);
    }

    public UsbTreeNode(Object object, String desc){
        this(object, desc, true);
    }

    public UsbTreeNode(Object object, boolean allowChildren){
        this(object, null, allowChildren);
    }

    public UsbTreeNode(Object object){
        this(object, null, true);
    }

    public UsbTreeNode(){
        this(null, null, true);
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
        if(allowsChildren && child!=null) {
            MutableTreeNode oldParent = (MutableTreeNode) child.getParent();
            if(oldParent!=null)
                oldParent.remove(child);
            child.setParent(this);
            children.add(index, child);
        }
    }

    public void add(MutableTreeNode child){
        if(allowsChildren && child!=null) {
            MutableTreeNode oldParent = (MutableTreeNode) child.getParent();
            if(oldParent!=null)
                oldParent.remove(child);
            child.setParent(this);
            children.add(child);
        }
    }

    @Override
    public void remove(int index) {
        if(allowsChildren)
            children.remove(index);
    }

    @Override
    public void remove(MutableTreeNode node) {
        if(allowsChildren) {
            children.remove(node);
            node.setParent(null);
        }
    }

    @Override
    public void setUserObject(Object object) {
        userObject = object;
    }

    public Object getUserObject(){
        return userObject;
    }

    public void setDescription(String desc){
        description = desc;
    }

    public String getDescription(){
        return description;
    }

    @Override
    public void removeFromParent() {
        if(parent!=null)
        parent.remove(this);
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
        parent = newParent;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    public void setAllowSChildren(boolean allow){
        allowsChildren = allow;
    }

    @Override
    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    @Override
    public boolean isLeaf() {
        return children.size()==0;
    }

    @Override
    public Enumeration children() {
        return children.elements();
    }

    @Override
    public String toString(){
        if(description!=null)
            return description;
        else if(userObject!=null)
            return userObject.toString();
        else
            return "null";
    }

}
