package edu.nyu.oop.customUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InvisiblePrintObject {
    private Object o;

    private static boolean invisibleCloakIsInactive;

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

    public static void toggleInvisibleCloak() {
        InvisiblePrintObject.invisibleCloakIsInactive = !InvisiblePrintObject.invisibleCloakIsInactive;
    }

    public String toString() {
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> map = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)o;
        return (InvisiblePrintObject.invisibleCloakIsInactive) ? o.toString() : "{numTypes=" + Integer.toString((map.size() - 1)) + "}";

    }
}
