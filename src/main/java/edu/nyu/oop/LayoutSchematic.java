package edu.nyu.oop;

import java.util.ArrayList;

/**
 * Created by kplajer on 10/26/16.
 */
public class LayoutSchematic {
    // static v-table field, v-table pointer, __class() method, in v-table: __isa

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
        public ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
    }

    public static class ClassStruct {
        public ArrayList<Method> methodList = new ArrayList<Method>();
        public ArrayList<Field> fieldList = new ArrayList<Field>();
        public ArrayList<Constructor> constructorList = new ArrayList<Constructor>();
    }

    public static class Initializer {
        public String fieldName;
        public Field initializeTo;
    }

    public static class VtableStruct {
        public ArrayList<Field> fieldList = new ArrayList<Field>();
        public ArrayList<Initializer> initializerList = new ArrayList<Initializer>();
    }

    public ClassStruct classStruct;
    public VtableStruct vtableStruct;


    LayoutSchematic(String className) {
        classStruct = new ClassStruct();

        Method __class = new Method();
        __class.name = "__class";
        __class.returnType = "Class";
        classStruct.methodList.add(__class);

        Field vptr = new Field();
        vptr.name = "__vptr";
        vptr.type = "__" + className + "_VT*";
        classStruct.fieldList.add(vptr);

        Field vtable = new Field();
        vtable.name = "__vtable";
        vtable.type = "__" + className + "_VT";
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
        setIsaTo.name = "__String::__class()";
        setIsaTo.type = "Class";
        isaInit.initializeTo = setIsaTo;
    }

    public LayoutSchematic getCopy() {
        // return copy so we dont mess it up...... (?)
        return null;
    }

    //
//        private static class MethodPointerInitializer {
//            public String name;
//            public boolean isInherited;
//            public String classInheritedFrom;
//            public String methodName; // <- only used for __isa, b/c it is actually the __class() method
//            public String returnType;
//            public ArrayList<String> parameterType = new ArrayList<String>();
//        }
//
//        private static class FieldInitializer {
//            public String name;
//            public String methodName;
//
//        }

    // field: name, type, access modifier, isStatic
    //  name = hashCode
    //  type = int32_t (*) (String)
    //  no access / static

    // init:
    //  for field "hashcode"
    //  set to:
    // field:
    //  name = &__String::hashcode
    //  type = int32_t (*) (String) (aka casting)


    // field:
    //  name: __isa
    //  type: Class
    //  no access / static

    // init:
    //  field: __isa
    //  set to:
    // field:
    //  name = __String::__class()
    //  type = Class

//    private static class MethodPointer {
//        public String name;
//        public String returnType;
//        public ArrayList<String> parameterTypes = new ArrayList<String>();
//    }

}