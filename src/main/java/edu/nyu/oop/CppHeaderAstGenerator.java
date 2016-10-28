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
    //prevent direct instantiation
    private CppHeaderAstGenerator() {}

    public static CppAst generateNew(List<GNode> javaAsts) { //decided to create the tree inside this class

        CppAst headerAst = new CppAst("SomeBigWrapperNode");

        InitVisitor classDecInit = new InitVisitor();
        MappingNode.setEntryRepository(headerAst.getAllEntries());
        MappingNode.setEntryRepositoryMap(headerAst.getAllEntriesMap());

        GNode preDirectives = MappingNode.createMappingNode("PreprocessorDirectives");
        MappingNode.addNode(headerAst.getRoot(), preDirectives);
        MappingNode.addDataFieldMultiVals(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

        MappingNode.createAndLinkDataFieldOneShot(headerAst.getRoot(),"UsingNamespace", "Name", "java::lang");

        for(GNode javaAst : javaAsts) {
            classDecInit.visit(javaAst, headerAst);
        }

        ClassRef.setHierarchy(determineClassOrder(javaAsts, headerAst));

        setForwardDeclarations(headerAst);

        return headerAst;
    }

    public static ClassHierarchyTree determineClassOrder(List<GNode> javaAsts, CppAst headerAst) {

        List<ClassRef> cRefs = new ArrayList<ClassRef>();
        ClassRef mainClassRef = null;
        boolean mainFound = false;
        ClassHierarchyTree hierarchy = new ClassHierarchyTree();

        ArrayList<Object> namespaces = getAllOfType("Namespace");
        GNode linkPoint;
        if(namespaces == null) {
            linkPoint = headerAst.getRoot();
        } else {
            linkPoint = (GNode)namespaces.get(namespaces.size() - 1);
        }

        for(GNode jAst : javaAsts) {
            List<Node> classDeclarations = NodeUtil.dfsAll(jAst, "ClassDeclaration");

            for(Node classDec : classDeclarations) {
                ClassRef curCR = new ClassRef("__" + classDec.get(1));
                curCR.setCppHAst(headerAst);
                curCR.setJAst(jAst);
                curCR.setJClassDeclaration((GNode)classDec);
                curCR.setCppAstLinkPoint(linkPoint);

                //check if current class is the main class, if so, save as own field (separate from other classes)
                if(!mainFound) {
                    List<Node> methodDecs = NodeUtil.dfsAll(classDec, "MethodDeclaration");
                    for (Node methodDec : methodDecs) {
                        if (methodDec.get(3).equals("main")) {
                            mainClassRef = curCR;
                            mainFound = true;
                        }
                    }
                }

                if(curCR != mainClassRef) {

                    hierarchy.putNameToRef(curCR.getName(), curCR);

                    GNode layer = (GNode)classDec.get(3);
                    if(layer != null) {
                        layer = (GNode)(((GNode) (layer.get(0))).get(0));
                        String extension = (String) layer.get(0);
                        hierarchy.putChildToParent(curCR.getName(), "__" + extension);
                    }

                    cRefs.add(curCR);
                }

            }

        }


        for(ClassRef cR : cRefs) {
            String superClassName = hierarchy.getChildToParent(cR.getName());

            hierarchy.putParentToChildren(superClassName, cR.getName());

            cR.setParentClassRef(hierarchy.getNameToRef(superClassName));
        }


        headerAst.setMainClassRef(mainClassRef);

        for(ClassRef cR : cRefs) {
            ClassRef parent = cR.getParentClassRef();
            if(parent == null) {
                topologicalSorting(cR, hierarchy, headerAst);
            }
        }

        return hierarchy;
    }

    public static void topologicalSorting(ClassRef start, ClassHierarchyTree hierarchy, CppAst headerAst) {
        ArrayDeque<ClassRef> Q = new ArrayDeque<ClassRef>();
        Q.add(start);

        while(!Q.isEmpty()) {
            ClassRef curCR = Q.poll();

            headerAst.addClassRefs(curCR);

            ArrayList<String> childList = hierarchy.getParentToChildren(curCR.getName());

            if(childList != null) {

                for (String childName : childList) {
                    Q.add(hierarchy.getNameToRef(childName));
                }
            }
        }
    }

    public static void setForwardDeclarations(CppAst headerAst) {
        for (ClassRef cR : headerAst.getClassRefs()) {
            //forward declaration struct
            GNode construct = MappingNode.createMappingNode("ForwardDeclaration");
            MappingNode.addNode(headerAst.getMostRecentParent(), construct);
            MappingNode.addDataField(construct, "Type", "struct");
            MappingNode.addDataField(construct, "Declaration", cR.getName());

            //forward declaration struct vtable
            construct = MappingNode.createMappingNode("ForwardDeclaration");
            MappingNode.addNode(headerAst.getMostRecentParent(), construct);
            MappingNode.addDataField(construct, "Type", "struct");
            MappingNode.addDataField(construct, "Declaration", cR.getName() + "_VT");

            //typedef
            construct = MappingNode.createMappingNode("TypeDefinition");
            MappingNode.addNode(headerAst.getMostRecentParent(), construct);
            MappingNode.addDataField(construct, "Type", cR.getName() + "*");
            MappingNode.addDataField(construct, "Definition", cR.getName().substring(2));
        }
    }


    public static void displayCppHEntryList(CppAst header) {
        ArrayList<Object> backingList = header.getAllEntries();
        for(int i = 0; i < header.getAllEntries().size(); ++i) {
            Object o = backingList.get(i);
            if(o instanceof GNode) {
                System.out.println(i + " " + ((GNode)(backingList.get(i))).getName());
            } else if(o instanceof MappingNode.DataField) {
                System.out.println(i + " " + ((MappingNode.DataField)(backingList.get(i))).getVal());

            }
        }
    }
}