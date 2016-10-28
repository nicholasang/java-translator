package edu.nyu.oop.util;
import java.util.HashMap;
import edu.nyu.oop.ClassRef;


import xtc.tree.GNode;
import java.util.*;

//4-tran
public class ClassHierarchyTree {
    HashMap<String, ClassRef> NameToRef;
    HashMap<String, String> childToParentMap;
    HashMap<String, ArrayList<String>> parentToChildrenMap;

    public ClassHierarchyTree() {
        this.NameToRef = new HashMap<String, ClassRef>();
        this.childToParentMap = new HashMap<String, String>();
        this.parentToChildrenMap = new HashMap<String, ArrayList<String>>();
    }

    public void putNameToRef(String name, ClassRef cR) {
        this.NameToRef.put(name, cR);
    }

    public ClassRef getNameToRef(String name) {
        return this.NameToRef.get(name);
    }

    public void putChildToParent(String childName, String parentName) {
        this.childToParentMap.put(childName, parentName);
    }

    public String getChildToParent(String childName) {
        return this.childToParentMap.get(childName);
    }

    public void putParentToChildren(String parentName, String childName) {
        ArrayList<String> childList = this.parentToChildrenMap.get(parentName);

        if(childList == null) {
            childList = new ArrayList<String>();
            childList.add(childName);
            this.parentToChildrenMap.put(parentName, childList);
        } else {
            childList.add(childName);
        }
    }

    public ArrayList<String> getParentToChildren(String parentName) {
        return this.parentToChildrenMap.get(parentName);
    }


}
