package edu.nyu.oop;

import org.junit.*;
import org.slf4j.Logger;

import static org.junit.Assert.*;

import java.util.*;
import java.io.File;
import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;


// Testing FillLayoutSchematic.java (as well as LayoutSchematic.java by proxy)

// see LayoutSchematicTest/TestClass.java for annotations on what specificially each method/field/constructor
// is trying to test in the functionality of LayoutSchematic
public class TestLayoutSchematic {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(TestLayoutSchematic.class);

    private static ClassRef testClass;
    private static LayoutSchematic layout;

    @BeforeClass
    public static void beforeClass() {
        Node n = NodeUtil.parseJavaFile(new File("src/test/java/inputs/LayoutSchematicTest/TestClass.java"));
        GNode testClassNode = (GNode) NodeUtil.dfs(n, "ClassDeclaration");

        testClass = new ClassRef("__TestClass");
        testClass.setJAst(testClassNode);
        testClass.setJClassDeclaration((GNode) NodeUtil.dfs(testClassNode, "ClassDeclaration"));

        FillLayoutSchematic.fillClass(testClass);

        layout = testClass.getLayoutSchematic();
    }

    @Test
    public void testVtable() {
        List<LayoutSchematic.Field> fields = layout.vtableStruct.fieldList;
        List<LayoutSchematic.Initializer> initializers = layout.vtableStruct.initializerList;

        assertEquals(1 + 4 + 1, fields.size()); // 2 from TestClass (not private method), 4 inherited (including toString), 1 is __isa
        assertEquals(1 + 4 + 1, initializers.size()); // see above

        // since field and initializer lists are very similar, just testing initializer list for correctness

        boolean hashcode = false, equals = false, getClass = false, toString = false, mainish = false;
        for (LayoutSchematic.Initializer initializer : initializers) {
            LayoutSchematic.Field initTo = initializer.initializeTo;
            switch (initializer.fieldName) {
            case "hashcode":
                hashcode = initTo.type.equals("int32_t (*) (__TestClass)") && initTo.name.equals("&__Object::hashcode");
                break;
            case "equals":
                equals = initTo.type.equals("bool (*) (__TestClass, Object)") && initTo.name.equals("&__Object::equals");
                break;
            case "getClass":
                getClass = initTo.type.equals("Class (*) (__TestClass)") && initTo.name.equals("&__Object::getClass");
                break;
            case "toString":
                toString = initTo.type.equals("String (*) (__TestClass)") && initTo.name.equals("&__TestClass::toString");
                break;
            case "mainish":
                mainish = initTo.type.equals("__rt::Array<bool>* (*) (__TestClass, __rt::Array<String>*)") && initTo.name.equals("&__TestClass::mainish");
                break;
            }
        }

        assert(hashcode && equals && getClass && toString && mainish);
        logger.info("Vtable struct laoyut correct");
    }

    @Test
    public void testClassConstructors() {
        List<LayoutSchematic.Constructor> constructors = layout.classStruct.constructorList;

        assertEquals(2 + 1, constructors.size()); // 2 defined in class plus added default no-arg constructor

        boolean defaultFound = false, publicFound = false, privateFound = false;
        for (LayoutSchematic.Constructor constructor : constructors) {
            if (constructor.parameterList.size() == 0 && constructor.accessModifier.equals("public")) {
                defaultFound = true;
            } else if (constructor.parameterList.size() == 1) {
                LayoutSchematic.Parameter param = constructor.parameterList.get(0);
                if (constructor.accessModifier.equals("private") && param.name.equals("num") && param.type.equals("int_16t")) {
                    privateFound = true;
                } else if (constructor.accessModifier.equals("public") && param.name.equals("num") && param.type.equals("int_32t")) {
                    publicFound = true;
                }
            }
        }

        assert(privateFound && publicFound && defaultFound);
        logger.info("All constructors correct");
    }

    @Test
    public void testClassMethods() {
        List<LayoutSchematic.Method> methods = layout.classStruct.methodList;

        assertEquals(4 + 1, methods.size()); // 4 defined in class plus __class() method

        boolean privateFound = false, mainishFound = false, toStringFound = false, staticFound = false;
        for (LayoutSchematic.Method method : methods) {
            switch (method.name) {
            case "privateMethod":
                privateFound = method.accessModifier.equals("private");
                privateFound = privateFound && method.parameterTypes.size() == 2 && method.parameterTypes.get(0).equals("String") && method.parameterTypes.get(1).equals("int_32t");
                break;
            case "mainish":
                mainishFound = method.parameterTypes.size() == 1 && method.parameterTypes.get(0).equals("__rt::Array<String>");
                mainishFound = mainishFound && method.returnType.equals("__rt::Array<bool>*");
                break;
            case "toString":
                toStringFound = method.returnType.equals("String") && method.accessModifier.equals("public") && method.parameterTypes.size() == 0;
                break;
            case "staticMethod":
                staticFound = method.isStatic && method.returnType.equals("void");
                break;
            }
        }

        assert(privateFound && mainishFound && toStringFound && staticFound);
        logger.info("All methods correct");
    }

    @Test
    public void testClassFields() {
        List<LayoutSchematic.Field> fields = layout.classStruct.fieldList;

        assertEquals(6 + 2, fields.size()); // 6 declared in class plus vptr and vtable fields


        boolean publicFound = false, defaultFound = false, staticFound = false, multipleFound = false, declaredFound = false, arrayFound = false;
        for (LayoutSchematic.Field field : fields) {
            switch (field.name) {
            case "publicField": // tests access modifier and type (java -> c++) conversion
                publicFound = field.type.equals("int_32t") && field.accessModifier.equals("public");
                break;
            case "defaultField": // test lack of access modifier and type conversion
                defaultFound = field.type.equals("int_8t") && (field.accessModifier == null);
                break;
            case "staticField": // test static-ness
                staticFound = field.isStatic;
                break;
            case "multiple": // test multiple declarations on one line (do they get right type?)
                multipleFound = field.type.equals("int32_t");
                break;
            case "declared": // test multiple declarations on one line (do they get right type?)
                declaredFound = field.type.equals("int32_t");
                break;
            case "arrayField": // test array type
                arrayFound = field.type.equals("Array<int32_t>*");
                break;
            }
        }

        assert(publicFound && defaultFound && staticFound && multipleFound && declaredFound && arrayFound);
        logger.info("All fields correct");
    }

}
