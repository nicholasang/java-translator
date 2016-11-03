package edu.nyu.oop;

import java.util.*;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;

/**
 * Created by kplajer on 10/28/16.
 */
public class FillLayoutSchematic {

    public static LayoutSchematic objectLayoutSchematic;

    public static void fillClasses(CppAst topNode) {
        List<ClassRef> classList = topNode.getClassRefs();

        for (ClassRef classRef : classList) {
            fillClass(classRef);
        }
    }

    public static void fillClass(ClassRef classRef) {
        populateClassStruct(classRef.getLayoutSchematic().classStruct, classRef.getJClassDeclaration());

        if (classRef.getParentClassRef() != null) {
            getInheritedFields(classRef.getLayoutSchematic().classStruct, classRef.getParentClassRef().getLayoutSchematic().classStruct);
            getInheritedVtable(classRef.getLayoutSchematic().vtableStruct, classRef.getParentClassRef().getLayoutSchematic().vtableStruct, classRef.getName());
        } else {
            getInheritedFields(classRef.getLayoutSchematic().classStruct, objectLayoutSchematic.classStruct);
            getInheritedVtable(classRef.getLayoutSchematic().vtableStruct, objectLayoutSchematic.vtableStruct, classRef.getName());
        }

        populateVtableFromClassStruct(classRef.getLayoutSchematic().vtableStruct, classRef.getLayoutSchematic().classStruct, classRef.getName());
    }

    static {
        objectLayoutSchematic = new LayoutSchematic("__Object");

        LayoutSchematic.ClassStruct classStruct = objectLayoutSchematic.classStruct;

        LayoutSchematic.Method hashcode = new LayoutSchematic.Method();
        hashcode.name = "hashcode";
        hashcode.parameterTypes.add("Object");
        hashcode.returnType = "int32_t";
        hashcode.accessModifier = "public";
        classStruct.methodList.add(hashcode);

        LayoutSchematic.Method equals = new LayoutSchematic.Method();
        equals.name = "equals";
        equals.parameterTypes.add("Object");
        equals.parameterTypes.add("Object");
        equals.returnType = "bool";
        equals.accessModifier = "public";
        classStruct.methodList.add(equals);

        LayoutSchematic.Method getClass = new LayoutSchematic.Method();
        getClass.name = "getClass";
        getClass.parameterTypes.add("Object");
        getClass.returnType = "Class";
        getClass.accessModifier = "public";
        classStruct.methodList.add(getClass);

        LayoutSchematic.Method toString = new LayoutSchematic.Method();
        toString.name = "toString";
        toString.parameterTypes.add("Object");
        toString.returnType = "String";
        toString.accessModifier = "public";
        classStruct.methodList.add(getClass);

        classStruct.constructorList.add(createDefaultConstructor());

        LayoutSchematic.VtableStruct vtableStruct = objectLayoutSchematic.vtableStruct;

        LayoutSchematic.Field hashcodePtr = new LayoutSchematic.Field();
        hashcodePtr.name = "hashcode";
        hashcodePtr.type = "int32_t (*) (Object)";
        vtableStruct.fieldList.add(hashcodePtr);

        LayoutSchematic.Field equalsPtr = new LayoutSchematic.Field();
        equalsPtr.name = "equals";
        equalsPtr.type = "bool (*) (Object, Object)";
        vtableStruct.fieldList.add(equalsPtr);

        LayoutSchematic.Field getClassPtr = new LayoutSchematic.Field();
        getClassPtr.name = "getClass";
        getClassPtr.type = "Class (*) (Object)";
        vtableStruct.fieldList.add(getClassPtr);

        LayoutSchematic.Field toStringPtr = new LayoutSchematic.Field();
        toStringPtr.name = "toString";
        toStringPtr.type = "String (*) (Object)";
        vtableStruct.fieldList.add(toStringPtr);

        LayoutSchematic.Initializer hashcodeInit = new LayoutSchematic.Initializer();
        hashcodeInit.fieldName = "hashcode";
        hashcodePtr = new LayoutSchematic.Field();
        hashcodePtr.name = "&__Object::hashcode";
        hashcodePtr.type = "int32_t (*) (Object)";
        hashcodeInit.initializeTo = hashcodePtr;
        vtableStruct.initializerList.add(hashcodeInit);

        LayoutSchematic.Initializer equalsInit = new LayoutSchematic.Initializer();
        equalsInit.fieldName = "equals";
        equalsPtr = new LayoutSchematic.Field();
        equalsPtr.name = "&__Object::equals";
        equalsPtr.type = "bool (*) (Object, Object)";
        equalsInit.initializeTo = equalsPtr;
        vtableStruct.initializerList.add(equalsInit);

        LayoutSchematic.Initializer getClassInit = new LayoutSchematic.Initializer();
        getClassInit.fieldName = "getClass";
        getClassPtr = new LayoutSchematic.Field();
        getClassPtr.name = "&__Object::getClass";
        getClassPtr.type = "Class (*) (Object)";
        getClassInit.initializeTo = getClassPtr;
        vtableStruct.initializerList.add(getClassInit);

        LayoutSchematic.Initializer toStringInit = new LayoutSchematic.Initializer();
        toStringInit.fieldName = "toString";
        toStringPtr = new LayoutSchematic.Field();
        toStringPtr.name = "&__Object::toString";
        toStringPtr.type = "String (*) (Object)";
        toStringInit.initializeTo = toStringPtr;
        vtableStruct.initializerList.add(toStringInit);

    }

    private static void populateVtableFromClassStruct(LayoutSchematic.VtableStruct vtableStruct, LayoutSchematic.ClassStruct classStruct, String className) {
        List<LayoutSchematic.Method> methodList = classStruct.methodList;

        for (LayoutSchematic.Method method : methodList) {
            if (method.name.equals("__class")) {
                continue;
            } else if (method.isStatic) {
                continue;
            } else if ("private".equals(method.accessModifier)) {
                continue;
            }
            // ^ private & static methods not in

            LayoutSchematic.Field methodPointer;
            LayoutSchematic.Field setTo;
            LayoutSchematic.Initializer initializer;

            if (fieldExists(method.name, vtableStruct.fieldList)) {
                // if overriding an inherited method

                methodPointer = getFieldWithName(method.name, vtableStruct.fieldList);
                initializer = getInitializerFor(method.name, vtableStruct.initializerList);
                setTo = initializer.initializeTo;
            } else {
                methodPointer = new LayoutSchematic.Field();
                vtableStruct.fieldList.add(methodPointer);
                setTo = new LayoutSchematic.Field();
                initializer = new LayoutSchematic.Initializer();
                vtableStruct.initializerList.add(initializer);
            }

            methodPointer.name = method.name;

            String type = method.returnType + " (*) (" + className;
            for (String parameterType : method.parameterTypes) {
                type += ", " + parameterType;
            }
            type += ")";
            methodPointer.type = type;

            setTo.name = "&" + className + "::" + method.name;
            setTo.type = type;

            initializer.fieldName = method.name;
            initializer.initializeTo = setTo;
        }
    }

    private static boolean fieldExists(String name, List<LayoutSchematic.Field> fields) {
        for (LayoutSchematic.Field field : fields) {
            if (field.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static LayoutSchematic.Field getFieldWithName(String name, List<LayoutSchematic.Field> fields) {
        for (LayoutSchematic.Field field : fields) {
            if (field.name.equals(name)) {
                return field;
            }
        }
        return null;
    }

    private static LayoutSchematic.Initializer getInitializerFor(String name, List<LayoutSchematic.Initializer> initializers) {
        for (LayoutSchematic.Initializer initializer : initializers) {
            if (initializer.fieldName.equals(name)) {
                return initializer;
            }
        }
        return null;
    }

    private static void getInheritedVtable(LayoutSchematic.VtableStruct childVtable, LayoutSchematic.VtableStruct parentVtable, String childClass) {
        // in Java, private methods are not really inherited (subclass does not "know" about them), but here, in order for other
        // inherited methods to work (say, inherited methods that call those private methods), the subclass must have these private methods

        for (LayoutSchematic.Field field : parentVtable.fieldList) {
            if (field.name.equals("__isa")) {
                continue;
            }
            LayoutSchematic.Field fieldCopy = copyField(field);
            changeTypeOfThis(fieldCopy, childClass);
            childVtable.fieldList.add(fieldCopy);
        }

        for (LayoutSchematic.Initializer initializer : parentVtable.initializerList) {
            if (initializer.fieldName.equals("__isa")) {
                continue;
            }
            LayoutSchematic.Initializer initCopy = copyInitializer(initializer);
            changeTypeOfThis(initCopy.initializeTo, childClass);
            childVtable.initializerList.add(initCopy);
        }
    }

    private static LayoutSchematic.Initializer copyInitializer(LayoutSchematic.Initializer initializer) {
        LayoutSchematic.Initializer copy = new LayoutSchematic.Initializer();

        copy.fieldName = initializer.fieldName;
        copy.initializeTo = copyField(initializer.initializeTo);

        return copy;
    }

    private static LayoutSchematic.Field copyField(LayoutSchematic.Field field) {
        LayoutSchematic.Field copy = new LayoutSchematic.Field();

        copy.name = field.name;
        copy.type = field.type;
        copy.accessModifier = field.accessModifier;
        copy.isStatic = field.isStatic;

        return copy;
    }

    private static void changeTypeOfThis(LayoutSchematic.Field field, String className) {
        int lastOpeningParen = field.type.lastIndexOf('(');
        int firstCommaOrClosingParen = field.type.indexOf(',');
        if (firstCommaOrClosingParen == -1) {
            firstCommaOrClosingParen = field.type.lastIndexOf(')');
        }

        field.type = field.type.substring(0, lastOpeningParen + 1) + className + field.type.substring(firstCommaOrClosingParen);
    }

    private static void getInheritedFields(LayoutSchematic.ClassStruct childClassStruct, LayoutSchematic.ClassStruct parentClassStruct) {
        for (LayoutSchematic.Field field : parentClassStruct.fieldList) {
            if ("__vtable".equals(field.name) || "__vptr".equals(field.name)) {
                continue;
            } else if (field.isStatic) {
                continue;
            }

            // technically, in Java, subclasses do NOT inherit private fields
            // HOWEVER, the C++ "subclass" needs to be able to have/access all the same field as its parent class
            // in order for inherited getter / setter methods to work, so here private fields are "inherited"

            LayoutSchematic.Field fieldCopy = copyField(field);
            childClassStruct.fieldList.add(fieldCopy);
        }
    }

    private static void populateClassStruct(LayoutSchematic.ClassStruct classStruct, GNode classNode) {
        List<Node> methodNodes = NodeUtil.dfsAll(classNode, "MethodDeclaration");
        List<Node> fieldNodes = NodeUtil.dfsAll(classNode, "FieldDeclaration");
        List<Node> constructorNodes = NodeUtil.dfsAll(classNode, "ConstructorDeclaration");


        for (Node methodNode : methodNodes) {
            classStruct.methodList.add(createMethod((GNode) methodNode));
        }

        for (Node fieldNode : fieldNodes) {
            classStruct.fieldList.addAll(createField((GNode) fieldNode));
        }

        boolean haveNoArgConstructor = false;
        for (Node constructorNode : constructorNodes) {
            if (hasNoArguments((GNode) constructorNode)) {
                haveNoArgConstructor = true;
            }
            classStruct.constructorList.add(createConstructor((GNode) constructorNode));
        }
        if (! haveNoArgConstructor) {
            // need to check if we have the default no-arg constructor, and if not add it ourselves
            classStruct.constructorList.add(createDefaultConstructor());
        }
    }

    private static LayoutSchematic.Method createMethod(GNode methodNode) {
        LayoutSchematic.Method method = new LayoutSchematic.Method();

        GNode modifiers = (GNode) methodNode.getNode(0);

        for (int i = 0; i < modifiers.size(); i++) {
            Node modNode = modifiers.getNode(i);
            if (modNode.getName().equals("Modifier")) {
                String modifier = modNode.getString(0);
                if (modifier.equals("static")) {
                    method.isStatic = true;
                } else {
                    method.accessModifier = modifier;
                }
            }
        }

        method.returnType = getType((GNode) methodNode.getNode(2));

        method.name = methodNode.getString(3);

        GNode parameters = (GNode) methodNode.getNode(4);
        for (int i = 0; i < parameters.size(); i++) {
            Node parameter = parameters.getNode(i);
            method.parameterTypes.add(getType((GNode) parameter.getNode(1)));
        }

        return method;
    }

    private static List<LayoutSchematic.Field> createField(GNode fieldNode) {
        // multiple fields may be declared in one FieldDeclaration node (i.e. "int a, b, c;"),
        // so we have to handle this and return a list of Fields

        List<LayoutSchematic.Field> fields = new ArrayList<LayoutSchematic.Field>();

        GNode names = (GNode) fieldNode.getNode(2);
        for (int i = 0; i < names.size(); i++) {
            String name = names.getNode(i).getString(0);
            LayoutSchematic.Field field = new LayoutSchematic.Field();
            field.name = name;

            GNode modifiers = (GNode) fieldNode.getNode(0);

            for (int j = 0; j < modifiers.size(); j++) {
                Node modNode = modifiers.getNode(j);
                if (modNode.getName().equals("Modifier")) {
                    String modifier = modNode.getString(0);
                    if (modifier.equals("static")) {
                        field.isStatic = true;
                    } else {
                        field.accessModifier = modifier;
                    }
                }
            }

            field.type = getType((GNode) fieldNode.getNode(1));

            fields.add(field);
        }

        return fields;
    }

    private static boolean hasNoArguments(GNode constructorNode) {
        GNode parameters = (GNode) constructorNode.getNode(3);
        if (parameters.size() == 0) {
            return true;
        }
        return false;
    }

    private static LayoutSchematic.Constructor createDefaultConstructor() {
        LayoutSchematic.Constructor constructor = new LayoutSchematic.Constructor();
        constructor.accessModifier = "public";

        return constructor;
    }


    private static LayoutSchematic.Constructor createConstructor(GNode constructorNode) {
        LayoutSchematic.Constructor constructor = new LayoutSchematic.Constructor();

        Node modifiers = constructorNode.getNode(0);

        for (int i = 0; i < modifiers.size(); i++) {
            Node modNode = modifiers.getNode(i);
            if (modNode.getName().equals("Modifier")) {
                String modifier = modNode.getString(0);
                constructor.accessModifier = modifier;
            }
        }

        GNode parameters = (GNode) constructorNode.getNode(3);
        for (int i = 0; i < parameters.size(); i++) {
            LayoutSchematic.Parameter parameter = new LayoutSchematic.Parameter();
            Node parameterNode = parameters.getNode(i);

            parameter.type = getType((GNode) parameterNode.getNode(1));
            parameter.name = parameterNode.getString(3);

            constructor.parameterList.add(parameter);
        }

        return constructor;
    }

    private static String getType(GNode typeNode) {
        if (typeNode.size() == 0 || typeNode.getName().equals("VoidType")) { // void (e.g. method return type)
            return "void";
        } else {
            String innerType = getCType(typeNode.getNode(0).getString(0));
            if (typeNode.get(1) != null) { // is an array
                return "__rt::Array<" + innerType + ">*";
            }
            return innerType;
        }
    }

    private static String getCType(String javaType) {
        String cType;
        switch (javaType) {
        case "long":
            cType = "int64_t";
            break;
        case "int":
            cType = "int32_t";
            break;
        case "short":
            cType = "int16_t";
            break;
        case "byte":
            cType = "int8_t";
            break;
        case "boolean":
            cType = "bool";
            break;
        default:
            cType = javaType;
            break;
        }

        return cType;
    }
}
