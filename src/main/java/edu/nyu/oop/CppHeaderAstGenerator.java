package edu.nyu.oop;

import edu.nyu.oop.util.ClassHierarchyTree;
import edu.nyu.oop.util.InitVisitor;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;

import edu.nyu.oop.util.MappingNode;
import static edu.nyu.oop.util.MappingNode.*;

import xtc.tree.Node;


import java.util.*;


public class CppHeaderAstGenerator {
    private CppHeaderAstGenerator() {}


    public static ArrayList<CppAst> allCppHeaderAsts;

    public static ArrayList<CppAst> generateNew(List<GNode> javaAsts) { //decided to create the tree inside this class

        //array of ClassRef
        ArrayList<ClassRef> cRefs = new ArrayList<ClassRef>();

        CppAst headerAst = new CppAst("SomeBigWrapperNode");

        InitVisitor classBodyInit = new InitVisitor();
        MappingNode.setEntryRepository(headerAst.getAllEntries());

        //System.out.println(headerAst.getAllEntries());
        //System.out.println(MappingNode.getEntryRepository());
        //System.out.println(MappingNode.getAllOfType("SomeBigWrapperNode"));


        //System.exit(0);

        GNode preDirectives = MappingNode.createMappingNode("PreprocessorDirectives");
        MappingNode.addNode(headerAst.getRoot(), preDirectives);
        MappingNode.addDataFieldMultiVals(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );


        ArrayList<Object> o2 = (MappingNode.getAllOfType("PreprocessorDirectives"));

        System.out.println(o2);

        for (Object ob : o2)
        {
            if (ob instanceof GNode)
            {
                System.out.println(((GNode) ob).getName());
            }
            else
            {
                System.out.println(((DataField) ob).getKey());
            }
        }

        System.exit(0);
        GNode usingNamespace = MappingNode.createAndLinkDataFieldOneShot(headerAst.getRoot(),"UsingNamespace", "Name", "java::lang");

        classBodyInit.visit(javaAsts.get(0), headerAst);

        // TODO: auto forward declarations (no issue, will add later)

        List<GNode> classDeclarations = new ArrayList<GNode>();

        for(GNode jAst : javaAsts)
        {
            List<Node> result = NodeUtil.dfsAll(jAst, "ClassDeclaration");
            for(Node n : result)
            {
                classDeclarations.add((GNode)n);
            }
        }

        ArrayList<Object> o = MappingNode.getAllOfType("Namespace");

        /*
        for(Object ob : o)
        {
            if(ob instanceof GNode)
            {
                System.out.println(((GNode) ob).getName());
            }
            else
            {
                System.out.println(((DataField)ob).getKey());
            }
        }
        */

        System.exit(0);

        ClassHierarchyTree hierarchy = new ClassHierarchyTree();

        XtcTestUtils.prettyPrintAst(javaAsts.get(0));
        return null;
    }

    public static void determineClassOrder(List<GNode> javaAsts)
    {

    }




}