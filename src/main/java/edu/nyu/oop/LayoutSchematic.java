package edu.nyu.oop;

import java.util.ArrayList;

/*
 * Phase 2
 * Creates the layout schematic for each method, field, and constructor
 * in the class struct as well as the V-table struct in the C++ header file.
 * Each subclass mimics the use of a struct in C++
 */
public class LayoutSchematic {
    /*
     * Java struct equivalents to represent components of the header file
     */
    public static class Method {
        public String name;
        public ArrayList<String> parameterTypes = new ArrayList<String>();
        public String returnType;
        public String accessModifier;
        public boolean isStatic;
    }

    public static class Field {
        public String name;
        public String type;
        public String accessModifier;
        public boolean isStatic;
    }

    public static class Parameter {
        public String name;
        public String type;
    }

    public static class Constructor {
        public String accessModifier;
        public ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
    }

    /*
     * Represents the class struct with methods, fields, and constructors
     */
    public static class ClassStruct {
        public ArrayList<Method> methodList = new ArrayList<Method>();
        public ArrayList<Field> fieldList = new ArrayList<Field>();
        public ArrayList<Constructor> constructorList = new ArrayList<Constructor>();
    }

    public static class Initializer {
        public String fieldName;
        public Field initializeTo;
    }

    /*
     * Represents the V-table struct with fields and initialized methods
     */
    public static class VtableStruct {
        public ArrayList<Field> fieldList = new ArrayList<Field>();
        public ArrayList<Initializer> initializerList = new ArrayList<Initializer>();
    }

    public ClassStruct classStruct;
    public VtableStruct vtableStruct;

    // constructor
    public LayoutSchematic(String className) {
        classStruct = new ClassStruct();

        Method __class = new Method();
        __class.name = "__class";
        __class.returnType = "Class";
        classStruct.methodList.add(__class);

        Field vptr = new Field();
        vptr.name = "__vptr";
        vptr.type = className + "_VT*";
        classStruct.fieldList.add(vptr);

        Field vtable = new Field();
        vtable.name = "__vtable";
        vtable.type = className + "_VT";
        vtable.isStatic = true;
        classStruct.fieldList.add(vtable);

        vtableStruct = new VtableStruct();

        Field isa = new Field();
        isa.name = "__isa";
        isa.type = "Class";
        vtableStruct.fieldList.add(isa);

        Initializer isaInit = new Initializer();
        isaInit.fieldName = "__isa";
        Field setIsaTo = new Field();
        setIsaTo.name = className + "::__class()";
        setIsaTo.type = "Class";
        isaInit.initializeTo = setIsaTo;
        vtableStruct.initializerList.add(isaInit);
    }

    public String toString() {
        String string = "";

        string += "Class:\n";
        for (Method method : classStruct.methodList) {
            string += "\t Method: " + method.accessModifier + " " + (method.isStatic? "static " : "") + method.name + "\n";
        }
        for (Field field : classStruct.fieldList) {
            string += "\t Field: " + field.accessModifier + " " + (field.isStatic? "static " : "") + field.type + " " + field.name + "\n";
        }
        for (Constructor constructor : classStruct.constructorList) {
            string += "\t Constructor: # of args = " + constructor.parameterList.size() + "\n";
        }

        string += "Vtable:\n";
        for (Field field : vtableStruct.fieldList) {
            string += "\t Field: " + field.type + " " + field.name + "\n";
        }
        for (Initializer initializer : vtableStruct.initializerList) {
            string += "\t Initializer: " + initializer.fieldName + " = " + initializer.initializeTo.type + " " + initializer.initializeTo.name + "\n";
        }

        return string;
    }

}