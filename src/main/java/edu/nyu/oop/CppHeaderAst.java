package edu.nyu.oop;

import xtc.tree.GNode;


import java.util.*;

public class CppHeaderAst {

    private GNode root;
    private ArrayList<Object> allEntries;
    private GNode mostRecentParent;
    private ArrayList<GNode> classes;

    // TODO: have a list of GNodes pointing to all class bodies in the tree, also list of methods/fields
    private ArrayList<MethodsAndFieldsSchematic> classMethodsAndFields;

    public CppHeaderAst(String name) {
        this.allEntries = new ArrayList<Object>();
        this.root = CppHeaderAstsGenerator.createMappingNode(name);
        this.allEntries.add(this.root);
        this.classes = new ArrayList<GNode>();

    }

    public CppHeaderAst(GNode root) {
        this.allEntries = new ArrayList<Object>();
        this.allEntries.add(root);
        this.classes = new ArrayList<GNode>();
    }

    public void addClass(GNode c) {
        this.classes.add(c);
    }
    public GNode getRoot() {
        return this.root;
    }

    public GNode getMostRecentParent() {
        return this.mostRecentParent;
    }

    public void setMostRecentParent(GNode mostRecentParent) {
        this.mostRecentParent = mostRecentParent;
    }

    public ArrayList<Object> getAllEntries() {
        return this.allEntries;
    }

    public String toString() {
        return root.toString();
    }

    public void print() {
        XtcTestUtils.prettyPrintAst(this.root);
    }

}
