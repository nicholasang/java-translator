package edu.nyu.oop.customUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InvisiblePrintObject {
    private Object o;

    private boolean invisibleCloakIsInactive;

    private String whatToPrintInstead;

    public InvisiblePrintObject(Object o) {
        this.o = o;
        this.whatToPrintInstead = "";
    }

    public InvisiblePrintObject(Object o, String whatToPrintInstead) {
        this.o = o;
        this.whatToPrintInstead = whatToPrintInstead;
    }

    public Object get() {
        return this.o;
    }

    public void toggleInvisibleCloak() {
        this.invisibleCloakIsInactive = !this.invisibleCloakIsInactive;
    }

    public String toString() {
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> map = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)o;
        return (this.invisibleCloakIsInactive) ? o.toString() : "{numTypes=" + Integer.toString((map.size() - 1)) + "}";

    }
}
