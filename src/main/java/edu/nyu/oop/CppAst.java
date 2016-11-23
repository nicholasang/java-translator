package edu.nyu.oop;

import xtc.tree.GNode;

import static edu.nyu.oop.util.MappingNode.*;

import java.util.*;

/*
 * Phase Two
 * Represents the C++ AST
 */
public class CppAst {

    private GNode root;
    private ArrayList<Object> allEntries;
    private LinkedHashMap<String, ArrayList<Object>> allEntriesMap;
    private GNode mostRecentParent;
    private List<ClassRef> classRefs;

    private ClassRef mainClassRef;

    public CppAst(String name) {
        this.root = createMappingNode(name);

        this.allEntries = new ArrayList<Object>();
        this.allEntries.add(this.root);

        this.allEntriesMap = new LinkedHashMap<String, ArrayList<Object>>();
        this.allEntriesMap.put(this.root.getName(), new ArrayList<Object>(Arrays.asList(this.root)));

        this.classRefs = new ArrayList<ClassRef>();

    }

    public CppAst(GNode root) {
        this.root = root;

        this.allEntries = new ArrayList<Object>();
        this.allEntries.add(this.root);

        this.allEntriesMap = new LinkedHashMap<String, ArrayList<Object>>();
        this.allEntriesMap.put(this.root.getName(), new ArrayList<Object>(Arrays.asList(this.root)));

        this.classRefs = new ArrayList<ClassRef>();
    }

    // adds a class reference
    public void addClassRefs(ClassRef cR) {
        if(cR != null)this.classRefs.add(cR);
    }

    // adds a lis of class references
    public void addClassRefs(List<ClassRef> cRs) {
        if(cRs == null)return;

        for(ClassRef cR : cRs) {
            if(cR != null)this.classRefs.add(cR);
        }

    }

    // getters and setters
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

    public LinkedHashMap<String, ArrayList<Object>> getAllEntriesMap() {
        return this.allEntriesMap;
    }

    public ClassRef getMainClassRef() {
        return this.mainClassRef;
    }
    public void setMainClassRef(ClassRef mainClassRef) {
        this.mainClassRef = mainClassRef;
    }

    public String toString() {
        return root.toString();
    }

    public void print() {
        XtcTestUtils.prettyPrintAst(this.root);
    }

}
