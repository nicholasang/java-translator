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
        testPhase2HardCodedHeaderTreeSectionA();
        testPhase2Namespace();
        testPhase2ClassRefAndClassOrder();
        testForwardDeclarations();
        testPhase2ClassWrappers();
    }


    public void testPhase2HardCodedHeaderTreeSectionA() {

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
            {
                "ClassWrapper",
                "Struct",
                "struct",
                "__D",
                "Field",
                "protected",
                "false",
                "__D_VT*",
                "__vptr",
                "Field",
                "protected",
                "true",
                "__D_VT",
                "__vtable",
                "Constructor",
                "public",
                "__D",
                "ParameterList",
                "Method",
                "protected",
                "false",
                "Class",
                "__class",
                "ParameterList",
                "Method",
                "protected",
                "false",
                "void",
                "allByMyself",
                "ParameterList",
                "Struct",
                "struct",
                "__D_VT",
                "Field",
                "protected",
                "false",
                "Class",
                "__isa",
                "Field",
                "protected",
                "false",
                "int32_t (*) (__D)",
                "hashCode",
                "Field",
                "protected",
                "false",
                "bool (*) (__D, Object)",
                "equals",
                "Field",
                "protected",
                "false",
                "Class (*) (__D)",
                "getClass",
                "Field",
                "protected",
                "false",
                "String (*) (__D)",
                "toString",
                "Field",
                "protected",
                "false",
                "void (*) (__D)",
                "allByMyself",
                "Constructor",
                "public",
                "__D",
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
            {
                "ClassWrapper",

            },
            {
                "ClassWrapper",

            },
            {
                "ClassWrapper",

            }
        };



        for(String[] correctClassWrapper : correctClassWrappers) {
            backingListCompareTo(correctClassWrapper);

            index += correctClassWrapper.length;

            //remove later
            break;
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
