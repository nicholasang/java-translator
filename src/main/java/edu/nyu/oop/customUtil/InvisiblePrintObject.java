package edu.nyu.oop.customUtil;

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
        return (this.invisibleCloakIsInactive) ? o.toString() : whatToPrintInstead;
    }
}
