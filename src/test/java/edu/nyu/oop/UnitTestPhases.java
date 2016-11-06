package edu.nyu.oop;


import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;

import java.util.*;

import edu.nyu.oop.util.InitVisitor;
import edu.nyu.oop.util.MappingNode;
import edu.nyu.oop.util.NodeUtil;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import xtc.tree.GNode;

import xtc.tree.Location;
import xtc.tree.Node;


import java.io.File;

import org.junit.Test;

// NOTE: names of tests must be in alphabetical order to run in order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnitTestPhases {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(UnitTestPhases.class);

    private static List<GNode> javaAsts;
    private static final String TESTFILERELATIVEPATH = "src/test/java/inputs/testABCD/TestABCD.java";
    private static CppAst headerAst;
    private static ArrayList<Object> backingList;
    private static InitVisitor classDecInit;
    private static int index;

    @Test
    public void a0_TestPhase1() {
        Node n = NodeUtil.parseJavaFile(new File(TESTFILERELATIVEPATH));

        String workingDir = System.getProperty("user.dir");

        Location nLocation = n.getLocation();
        Location longLocation = new Location(workingDir + "/" + nLocation.file, nLocation.line, nLocation.column);
        n.setLocation(longLocation);

        javaAsts = GenerateJavaASTs.beginParse((GNode) n);

        String[] correctSubPaths = {
            "translator-4Tran/src/test/java/inputs/testABCD/TestABCD.java",
            "translator-4Tran/src/test/java/inputs/testABCD/C.java",
            "translator-4Tran/src/test/java/inputs/testABCD/B.java"
        };

        for(int i = 0; i < correctSubPaths.length; ++i) {

            String toTest = javaAsts.get(i).getLocation().file;
            org.junit.Assert.assertEquals(toTest.substring(toTest.length() - correctSubPaths[i].length()), correctSubPaths[i]);
        }
    }

    @Test
    public void b0_TestPhase2() {
        testPhase2HardCodedHeaderTreeSection();
        testPhase2Namespace();
        testPhase2ClassRefAndClassOrder();
        testForwardDeclarations();
        testPhase2ClassWrappers();
    }


    public void testPhase2HardCodedHeaderTreeSection() {

        headerAst = new CppAst("SomeBigWrapperNode");

        classDecInit = new InitVisitor();
        MappingNode.setEntryRepository(headerAst.getAllEntries());
        MappingNode.setEntryRepositoryMap(headerAst.getAllEntriesMap());

        GNode preDirectives = MappingNode.createMappingNode("PreprocessorDirectives");
        MappingNode.addNode(headerAst.getRoot(), preDirectives);
        MappingNode.addDataFieldMultiVals(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

        MappingNode.createAndLinkDataFieldOneShot(headerAst.getRoot(),"UsingNamespace", "Name", "java::lang");

        backingList = headerAst.getAllEntries();

        String[] correctHardCodedEntries = {
            "SomeBigWrapperNode",
            "PreprocessorDirectives",
            "#pragma once",
            "#include \"java_lang.h\"",
            "#include <stdint.h>",
            "#include <string>",
            "UsingNamespace",
            "java::lang"
        };

        backingListCompareTo(correctHardCodedEntries);

        index += correctHardCodedEntries.length;

    }

    public void testPhase2Namespace() {

        for(GNode javaAst : javaAsts) {
            classDecInit.visit(javaAst, headerAst);
        }

        String[] correctNameSpaces = {
            "Namespace",
            "inputs",
            "Namespace",
            "testABCD"
        };

        backingListCompareTo(correctNameSpaces);

        index += correctNameSpaces.length;
    }

    public void testPhase2ClassRefAndClassOrder() {

        ClassRef.setHierarchy(CppHeaderAstGenerator.determineClassOrder(javaAsts, headerAst));

        List<Node> classDeclarations = new ArrayList<Node>();

        for(GNode jAst : javaAsts) {
            classDeclarations.addAll(NodeUtil.dfsAll(jAst, "ClassDeclaration")); //main class at index 0 in this case since it's the first class in the ABCD.java file
        }


        ClassRef refB = new ClassRef("__B");
        refB.setCppHAst(headerAst);
        refB.setJAst(javaAsts.get(2));
        refB.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refB.setJClassDeclaration((GNode)classDeclarations.get(4));

        ClassRef refC = new ClassRef("__C");
        refC.setCppHAst(headerAst);
        refC.setJAst(javaAsts.get(1));
        refC.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refC.setJClassDeclaration((GNode)classDeclarations.get(3));

        ClassRef refD = new ClassRef("__D");
        refD.setCppHAst(headerAst);
        refD.setJAst(javaAsts.get(0));
        refD.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refD.setJClassDeclaration((GNode)classDeclarations.get(1));

        ClassRef refA = new ClassRef("__A");
        refA.setCppHAst(headerAst);
        refA.setJAst(javaAsts.get(0));
        refA.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refA.setJClassDeclaration((GNode)classDeclarations.get(2));

        refB.setParentClassRef(refA);
        refC.setParentClassRef(refA);


        List<ClassRef> orderedClassRefs = new ArrayList<ClassRef>();

        orderedClassRefs.add(refD);
        orderedClassRefs.add(refA);
        orderedClassRefs.add(refC);
        orderedClassRefs.add(refB);

        List<ClassRef> cR = headerAst.getClassRefs();

        for(int i = 0; i < cR.size(); ++i) {
            ClassRef curr = cR.get(i);
            ClassRef comp = orderedClassRefs.get(i);

            org.junit.Assert.assertEquals(curr.getName(), comp.getName());
            org.junit.Assert.assertEquals(curr.getJClassDeclaration(), comp.getJClassDeclaration());

        }
    }

    public void testForwardDeclarations() {
        CppHeaderAstGenerator.setForwardDeclarations(headerAst);

        String[] correctForwardDeclarations = {
            "ForwardDeclaration",
            "struct",
            "__D",
            "ForwardDeclaration",
            "struct",
            "__D_VT",
            "TypeDefinition",
            "__D*",
            "D",

            "ForwardDeclaration",
            "struct",
            "__A",
            "ForwardDeclaration",
            "struct",
            "__A_VT",
            "TypeDefinition",
            "__A*",
            "A",

            "ForwardDeclaration",
            "struct",
            "__C",
            "ForwardDeclaration",
            "struct",
            "__C_VT",
            "TypeDefinition",
            "__C*",
            "C",

            "ForwardDeclaration",
            "struct",
            "__B",
            "ForwardDeclaration",
            "struct",
            "__B_VT",
            "TypeDefinition",
            "__B*",
            "B",
        };

        backingListCompareTo(correctForwardDeclarations);

        index += correctForwardDeclarations.length;
    }

    public void testPhase2ClassWrappers() {

        FillLayoutSchematic.fillClasses(headerAst);

        CppHeaderAstGenerator.populateClassWrappers(headerAst);

        String[][] correctClassWrappers = {
            ////////////////////////////struct
            {
                "ClassWrapper",
                "Struct",
                "struct",
                "__D",
                "Field",
                "public",
                "false",
                "__D_VT*",
                "__vptr",
                "Field",
                "public",
                "true",
                "__D_VT",
                "__vtable",
                "Constructor",
                "public",
                "__D",
                "ParameterList",
                "Method",
                "public",
                "false",
                "Class",
                "__class",
                "ParameterList",
                "Method",
                "public",
                "false",
                "void",
                "allByMyself",
                "ParameterList",
                ////////////////////////////vtable
                "Struct",
                "struct",
                "__D_VT",
                "Field",
                "public",
                "false",
                "Class",
                "__isa",
                "Field",
                "public",
                "false",
                "int32_t (*) (__D)",
                "hashCode",
                "int32_t (*hashCode) (__D)",
                "Field",
                "public",
                "false",
                "bool (*) (__D, Object)",
                "equals",
                "bool (*equals) (__D, Object)",
                "Field",
                "public",
                "false",
                "Class (*) (__D)",
                "getClass",
                "Class (*getClass) (__D)",
                "Field",
                "public",
                "false",
                "String (*) (__D)",
                "toString",
                "String (*toString) (__D)",
                "Field",
                "public",
                "false",
                "void (*) (__D)",
                "allByMyself",
                "void (*allByMyself) (__D)",
                "Constructor",
                "public",
                "__D_VT",
                "ParameterList",
                "InitializationList",
                "InitField",
                "__isa",
                "InitFieldWith",
                "Class",
                "__D::__class()",
                "InitField",
                "hashCode",
                "InitFieldWith",
                "int32_t (*) (__D)",
                "&__Object::hashCode",
                "InitField",
                "equals",
                "InitFieldWith",
                "bool (*) (__D, Object)",
                "&__Object::equals",
                "InitField",
                "getClass",
                "InitFieldWith",
                "Class (*) (__D)",
                "&__Object::getClass",
                "InitField",
                "toString",
                "InitFieldWith",
                "String (*) (__D)",
                "&__Object::toString",
                "InitField",
                "allByMyself",
                "InitFieldWith",
                "void (*) (__D)",
                "&__D::allByMyself"
            },

            ////////////////////////////struct
            {
                "ClassWrapper",
                "Struct",
                "struct",
                "__A",
                "Field",
                "public",
                "false",
                "__A_VT*",
                "__vptr",
                "Field",
                "public",
                "true",
                "__A_VT",
                "__vtable",
                "Field",
                "public",
                "false",
                "String",
                "publicDataA",
                "Field",
                "protected",
                "false",
                "String",
                "protectedDataA",
                "Field",
                "private",
                "false",
                "String",
                "privateDataA",
                "Field",
                "public",
                "true",
                "String",
                "staticDataA",
                "Constructor",
                "public",
                "__A",
                "ParameterList",
                "Constructor",
                "public",
                "__A",
                "ParameterList",
                "Parameter",
                "String",
                "item",
                "Method",
                "public",
                "false",
                "Class",
                "__class",
                "ParameterList",
                "Method",
                "public",
                "false",
                "String",
                "getArbitraryData",
                "ParameterList",
                "Method",
                "public",
                "true",
                "String",
                "Love",
                "ParameterList",
                ////////////////////////////vtable
                "Struct",
                "struct",
                "__A_VT",
                "Field",
                "public",
                "false",
                "Class",
                "__isa",
                "Field",
                "public",
                "false",
                "int32_t (*) (__A)",
                "hashCode",
                "int32_t (*hashCode) (__A)",
                "Field",
                "public",
                "false",
                "bool (*) (__A, Object)",
                "equals",
                "bool (*equals) (__A, Object)",
                "Field",
                "public",
                "false",
                "Class (*) (__A)",
                "getClass",
                "Class (*getClass) (__A)",
                "Field",
                "public",
                "false",
                "String (*) (__A)",
                "toString",
                "String (*toString) (__A)",
                "Field",
                "public",
                "false",
                "String (*) (__A)",
                "getArbitraryData",
                "String (*getArbitraryData) (__A)",
                "Constructor",
                "public",
                "__A_VT",
                "ParameterList",
                "InitializationList",
                "InitField",
                "__isa",
                "InitFieldWith",
                "Class",
                "__A::__class()",
                "InitField",
                "hashCode",
                "InitFieldWith",
                "int32_t (*) (__A)",
                "&__Object::hashCode",
                "InitField",
                "equals",
                "InitFieldWith",
                "bool (*) (__A, Object)",
                "&__Object::equals",
                "InitField",
                "getClass",
                "InitFieldWith",
                "Class (*) (__A)",
                "&__Object::getClass",
                "InitField",
                "toString",
                "InitFieldWith",
                "String (*) (__A)",
                "&__Object::toString",
                "InitField",
                "getArbitraryData",
                "InitFieldWith",
                "String (*) (__A)",
                "&__A::getArbitraryData"
            },

            ////////////////////////////struct
            {
                "ClassWrapper",
                "Struct",
                "struct",
                "__C",
                "Field",
                "public",
                "false",
                "__C_VT*",
                "__vptr",
                "Field",
                "public",
                "true",
                "__C_VT",
                "__vtable",
                "Field",
                "public",
                "false",
                "String",
                "publicDataA",
                "Field",
                "protected",
                "false",
                "String",
                "protectedDataA",
                "Field",
                "private",
                "false",
                "String",
                "privateDataA",
                "Constructor",
                "public",
                "__C",
                "ParameterList",
                "Method",
                "public",
                "false",
                "Class",
                "__class",
                "ParameterList",
                "Method",
                "public",
                "false",
                "int32_t",
                "add",
                "ParameterList",
                "Parameter",
                "int32_t",
                "Parameter",
                "int32_t",
                ////////////////////////////vtable
                "Struct",
                "struct",
                "__C_VT",
                "Field",
                "public",
                "false",
                "Class",
                "__isa",
                "Field",
                "public",
                "false",
                "int32_t (*) (__C)",
                "hashCode",
                "int32_t (*hashCode) (__C)",
                "Field",
                "public",
                "false",
                "bool (*) (__C, Object)",
                "equals",
                "bool (*equals) (__C, Object)",
                "Field",
                "public",
                "false",
                "Class (*) (__C)",
                "getClass",
                "Class (*getClass) (__C)",
                "Field",
                "public",
                "false",
                "String (*) (__C)",
                "toString",
                "String (*toString) (__C)",
                "Field",
                "public",
                "false",
                "String (*) (__C)",
                "getArbitraryData",
                "String (*getArbitraryData) (__C)",
                "Field",
                "public",
                "false",
                "int32_t (*) (__C, int32_t, int32_t)",
                "add",
                "int32_t (*add) (__C, int32_t, int32_t)",
                "Constructor",
                "public",
                "__C_VT",
                "ParameterList",
                "InitializationList",
                "InitField",
                "__isa",
                "InitFieldWith",
                "Class",
                "__C::__class()",
                "InitField",
                "hashCode",
                "InitFieldWith",
                "int32_t (*) (__C)",
                "&__Object::hashCode",
                "InitField",
                "equals",
                "InitFieldWith",
                "bool (*) (__C, Object)",
                "&__Object::equals",
                "InitField",
                "getClass",
                "InitFieldWith",
                "Class (*) (__C)",
                "&__Object::getClass",
                "InitField",
                "toString",
                "InitFieldWith",
                "String (*) (__C)",
                "&__Object::toString",
                "InitField",
                "getArbitraryData",
                "InitFieldWith",
                "String (*) (__C)",
                "&__A::getArbitraryData",
                "InitField",
                "add",
                "InitFieldWith",
                "int32_t (*) (__C, int32_t, int32_t)",
                "&__C::add"
            },

            ////////////////////////////struct
            {
                "ClassWrapper",
                "Struct",
                "struct",
                "__B",
                "Field",
                "public",
                "false",
                "__B_VT*",
                "__vptr",
                "Field",
                "public",
                "true",
                "__B_VT",
                "__vtable",
                "Field",
                "private",
                "false",
                "String",
                "privateDataB",
                "Field",
                "public",
                "false",
                "String",
                "publicDataA",
                "Field",
                "protected",
                "false",
                "String",
                "protectedDataA",
                "Field",
                "private",
                "false",
                "String",
                "privateDataA",
                "Constructor",
                "public",
                "__B",
                "ParameterList",
                "Method",
                "public",
                "false",
                "Class",
                "__class",
                "ParameterList",
                "Method",
                "public",
                "false",
                "String",
                "getArbitraryData",
                "ParameterList",
                ////////////////////////////vtable
                "Struct",
                "struct",
                "__B_VT",
                "Field",
                "public",
                "false",
                "Class",
                "__isa",
                "Field",
                "public",
                "false",
                "int32_t (*) (__B)",
                "hashCode",
                "int32_t (*hashCode) (__B)",
                "Field",
                "public",
                "false",
                "bool (*) (__B, Object)",
                "equals",
                "bool (*equals) (__B, Object)",
                "Field",
                "public",
                "false",
                "Class (*) (__B)",
                "getClass",
                "Class (*getClass) (__B)",
                "Field",
                "public",
                "false",
                "String (*) (__B)",
                "toString",
                "String (*toString) (__B)",
                "Field",
                "public",
                "false",
                "String (*) (__B)",
                "getArbitraryData",
                "String (*getArbitraryData) (__B)",
                "Constructor",
                "public",
                "__B_VT",
                "ParameterList",
                "InitializationList",
                "InitField",
                "__isa",
                "InitFieldWith",
                "Class",
                "__B::__class()",
                "InitField",
                "hashCode",
                "InitFieldWith",
                "int32_t (*) (__B)",
                "&__Object::hashCode",
                "InitField",
                "equals",
                "InitFieldWith",
                "bool (*) (__B, Object)",
                "&__Object::equals",
                "InitField",
                "getClass",
                "InitFieldWith",
                "Class (*) (__B)",
                "&__Object::getClass",
                "InitField",
                "toString",
                "InitFieldWith",
                "String (*) (__B)",
                "&__Object::toString",
                "InitField",
                "getArbitraryData",
                "InitFieldWith",
                "String (*) (__B)",
                "&__B::getArbitraryData"

            }
        };



        for(String[] correctClassWrapper : correctClassWrappers) {
            backingListCompareTo(correctClassWrapper);

            index += correctClassWrapper.length;
        }

    }


    @AfterClass
    public static void afterClass() {
    }

    private static void backingListCompareTo(String[] correct) {
        for(int i = 0; i < correct.length; ++i) {
            Object o = UnitTestPhases.backingList.get(i + index);
            if(o instanceof GNode) {
                org.junit.Assert.assertEquals(correct[i], ((GNode)o).getName());
            } else if(o instanceof MappingNode.DataField) {
                org.junit.Assert.assertEquals(correct[i], ((MappingNode.DataField)o).getVal());
            }
        }
    }

    private static void displayBackingListDebug() {
        for(int i = 0; i < UnitTestPhases.backingList.size(); ++i) {
            Object o = UnitTestPhases.backingList.get(i);
            if(o instanceof GNode) {
                logger.debug(Integer.toString(i) + " " +  ((GNode)(UnitTestPhases.backingList.get(i))).getName());
            } else if(o instanceof MappingNode.DataField) {
                logger.debug(Integer.toString(i) + " " + ((MappingNode.DataField)(UnitTestPhases.backingList.get(i))).getVal());

            }
        }
    }
}
