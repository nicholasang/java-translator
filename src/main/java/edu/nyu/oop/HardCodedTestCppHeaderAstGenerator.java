package edu.nyu.oop;

import edu.nyu.oop.util.JavaAstVisitor;
import edu.nyu.oop.util.InitVisitor;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;


import java.util.ArrayList;
import java.util.List;


import java.util.*;


import edu.nyu.oop.util.MappingNode;
import edu.nyu.oop.util.MappingNode.DataField;

public abstract class HardCodedTestCppHeaderAstGenerator {
    public static CppAst generateNew(List<GNode> javaAsts) {
        //array of ClassRef
        ArrayList<ClassRef> cRefs = new ArrayList<ClassRef>();

        CppAst headerAst = new CppAst("SomeBigWrapperNode");

        InitVisitor classBodyInit = new InitVisitor();
        MappingNode.setEntryRepository(headerAst.getAllEntries());

        GNode preDirectives = MappingNode.createMappingNode("PreprocessorDirectives");
        MappingNode.addNode(headerAst.getRoot(), preDirectives);
        MappingNode.addDataFieldMultiVals(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

        GNode usingNamespace = MappingNode.createAndLinkDataFieldOneShot(headerAst.getRoot(),"UsingNamespace", "Name", "java::lang");

        classBodyInit.visit(javaAsts.get(0), headerAst);

        // TODO: auto forward declarations (no issue, will add later)

        // TEMP ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //just for hardcoding version: make room for 4 classRefs:
        List<Node> classDeclarations = NodeUtil.dfsAll(javaAsts.get(0), "ClassDeclaration"); //main class at index 0 in this case since it's the first class in the ABCD.java file

        ClassRef refB = new ClassRef("__B");
        refB.setCppHAst(headerAst);
        refB.setJAst(javaAsts.get(0));
        refB.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refB.setJClassDeclaration((GNode)classDeclarations.get(1));

        ClassRef refC = new ClassRef("__C");
        refC.setCppHAst(headerAst);
        refC.setJAst(javaAsts.get(0));
        refC.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refC.setJClassDeclaration((GNode)classDeclarations.get(2));

        ClassRef refD = new ClassRef("__D");
        refD.setCppHAst(headerAst);
        refD.setJAst(javaAsts.get(0));
        refD.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refD.setJClassDeclaration((GNode)classDeclarations.get(3));

        ClassRef refA = new ClassRef("__A");
        refA.setCppHAst(headerAst);
        refA.setJAst(javaAsts.get(0));
        refA.setCppHAstLinkPoint(headerAst.getMostRecentParent());
        refA.setJClassDeclaration((GNode)classDeclarations.get(4));

        refB.setParentClassRef(refA);
        refC.setParentClassRef(refA);


        List<ClassRef> orderedClassRefs = headerAst.getClassRefs();

        orderedClassRefs.add(refB);
        orderedClassRefs.add(refC);
        orderedClassRefs.add(refD);
        orderedClassRefs.add(refA);

        /*printout testing
        for(ClassRef c : orderedClassRefs)
        {
            System.out.println("-------------------------------");
            System.out.printf("ClassRef name: %s\nJava AST Class Declaration sub-tree:\n", c);
        }

        System.out.println(headerAst.getMostRecentParent());
        */

        return null;
    }
}
