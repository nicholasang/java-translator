package edu.nyu.oop;

import xtc.tree.GNode;

import static edu.nyu.oop.util.MappingNode.*;

import java.util.*;

public class CppAst {

    private GNode root;
    private ArrayList<Object> allEntries;
    private GNode mostRecentParent;
    private List<ClassRef> classRefs;

    public CppAst(String name) {
        this.allEntries = new ArrayList<Object>();
        this.root = createMappingNode(name);
        this.allEntries.add(this.root);
        this.classRefs = new ArrayList<ClassRef>();

    }

    public CppAst(GNode root) {
        this.root = root;
        this.allEntries = new ArrayList<Object>();
        this.allEntries.add(root);
        this.classRefs = new ArrayList<ClassRef>();
    }

    public void addClassRefs(ClassRef cR) {
        if(cR != null)this.classRefs.add(cR);
    }

    public void addClassRefs(List<ClassRef> cRs) {
        if(cRs == null)return;

        for(ClassRef cR : cRs) {
            if(cR != null)this.classRefs.add(cR);
        }

    }

    public List<ClassRef> getClassRefs() {
        return this.classRefs;
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
