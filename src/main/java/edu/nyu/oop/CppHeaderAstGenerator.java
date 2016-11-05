package edu.nyu.oop;

import edu.nyu.oop.util.*;

import xtc.tree.GNode;

import static edu.nyu.oop.util.MappingNode.*;
import static edu.nyu.oop.LayoutSchematic.*;


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

        FillLayoutSchematic.fillClasses(headerAst);

        populateClassWrappers(headerAst);

//        XtcTestUtils.prettyPrintAst(headerAst.getRoot());
//
//        CppHVisitor outputHeader = new CppHVisitor();
//
//        outputHeader.visit(headerAst);

        return headerAst;
    }

    public static void populateClassWrappers(CppAst headerAst) {

        List<ClassRef> classes = headerAst.getClassRefs();

        for(ClassRef cR: classes) {
            GNode linkPoint = cR.getCppHAstLinkPoint();
            LayoutSchematic lS = cR.getLayoutSchematic();

            GNode cW = MappingNode.createMappingNode("ClassWrapper");
            MappingNode.addNode(linkPoint, cW);

            linkPoint = cW;
            //create, link, and populate struct
            populateClassStruct(linkPoint, lS, cR);
            //create, link, and populate struct
            populateVtableStruct(linkPoint, lS, cR);
        }

    }

    public static void populateClassStruct(GNode linkPoint, LayoutSchematic lS, ClassRef cR) {
        GNode struct = MappingNode.createMappingNode("Struct");
        MappingNode.addNode(linkPoint, struct);
        linkPoint = struct;
        MappingNode.addDataField(struct, "Type", "struct");
        MappingNode.addDataField(struct, "Name", cR.getName());

        LayoutSchematic.ClassStruct cStruct = lS.classStruct;

        for(Field f : cStruct.fieldList) {
            GNode fieldNode = MappingNode.createMappingNode("Field");
            MappingNode.addNode(linkPoint, fieldNode);

            MappingNode.addDataField(fieldNode, "AccessModifier", (f.accessModifier == null) ? "protected" : f.accessModifier);
            MappingNode.addDataField(fieldNode, "IsStatic", Boolean.toString(f.isStatic));
            MappingNode.addDataField(fieldNode, "Type", f.type);
            MappingNode.addDataField(fieldNode, "Name", f.name);
        }


        for(Constructor c : cStruct.constructorList) {
            GNode constructorNode = MappingNode.createMappingNode("Constructor");
            MappingNode.addNode(linkPoint, constructorNode);

            MappingNode.addDataField(constructorNode, "AccessModifier", c.accessModifier);
            MappingNode.addDataField(constructorNode, "Name", cR.getName());

            GNode pL = MappingNode.createMappingNode("ParameterList");
            MappingNode.addNode(constructorNode, pL);

            if(c.parameterList.size() > 0) {
                for (Parameter p : c.parameterList) {
                    GNode paramNode = MappingNode.createMappingNode("Parameter");
                    MappingNode.addNode(pL, paramNode);

                    MappingNode.addDataField(paramNode, "Type", p.type);
                    MappingNode.addDataField(paramNode, "Name", p.name);

                }
            }

        }


        for(Method m : cStruct.methodList) {
            GNode constructorNode = MappingNode.createMappingNode("Method");
            MappingNode.addNode(linkPoint, constructorNode);

            MappingNode.addDataField(constructorNode, "AccessModifier", (m.accessModifier == null) ? "protected" : m.accessModifier);
            MappingNode.addDataField(constructorNode, "IsStatic", Boolean.toString(m.isStatic));
            MappingNode.addDataField(constructorNode, "ReturnType", m.returnType);
            MappingNode.addDataField(constructorNode, "Name", m.name);


            GNode pL = MappingNode.createMappingNode("ParameterList");
            MappingNode.addNode(constructorNode, pL);

            if(m.parameterTypes.size() > 0) {
                for(String paramType : m.parameterTypes) {
                    GNode paramNode = MappingNode.createMappingNode("Parameter");
                    MappingNode.addNode(pL, paramNode);

                    MappingNode.addDataField(paramNode, "Type", paramType);
                }
            }

        }
    }

    public static void populateVtableStruct(GNode linkPoint, LayoutSchematic lS, ClassRef cR) {
        GNode struct = MappingNode.createMappingNode("Struct");
        MappingNode.addNode(linkPoint, struct);
        linkPoint = struct;
        MappingNode.addDataField(struct, "Type", "struct");
        MappingNode.addDataField(struct, "Name", cR.getName() + "_VT");

        LayoutSchematic.VtableStruct vtStruct = lS.vtableStruct;

        for(Field f : vtStruct.fieldList) {
            GNode fieldNode = MappingNode.createMappingNode("Field");
            MappingNode.addNode(linkPoint, fieldNode);


            MappingNode.addDataField(fieldNode, "AccessModifier", (f.accessModifier == null) ? "protected" : f.accessModifier);
            MappingNode.addDataField(fieldNode, "IsStatic", Boolean.toString(f.isStatic));
            MappingNode.addDataField(fieldNode, "Type", f.type);
            MappingNode.addDataField(fieldNode, "Name", f.name);
        }

        GNode constructorNode = MappingNode.createMappingNode("Constructor");
        MappingNode.addNode(linkPoint, constructorNode);

        MappingNode.addDataField(constructorNode, "AccessModifier", "public");
        MappingNode.addDataField(constructorNode, "Name", cR.getName());

        GNode paramList = MappingNode.createMappingNode("ParameterList");
        MappingNode.addNode(constructorNode, paramList);

        GNode initList = MappingNode.createMappingNode("InitializationList");
        MappingNode.addNode(constructorNode, initList);



        if(vtStruct.initializerList.size() > 0) {
            for(Initializer init : vtStruct.initializerList) {
                GNode initNode = MappingNode.createMappingNode("InitField");
                MappingNode.addNode(initList, initNode);

                MappingNode.addDataField(initNode, "Name", init.fieldName);

                Field f = init.initializeTo;

                GNode fieldNode = MappingNode.createMappingNode("InitFieldWith");
                MappingNode.addNode(initNode, fieldNode);


                MappingNode.addDataField(fieldNode, "Type", f.type);
                MappingNode.addDataField(fieldNode, "Name", f.name);

            }
        }

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
                curCR.setCppHAstLinkPoint(linkPoint);

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
        for(ClassRef cR : headerAst.getClassRefs()) {
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


    public static void displayCppHEntryListNumbered(CppAst header) {
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

    public static void displayCppHEntryListAs2DArray(CppAst header) {
        ArrayList<Object> backingList = header.getAllEntries();
        for(int i = 0; i < header.getAllEntries().size(); ++i) {
            Object o = backingList.get(i);
            if(o instanceof GNode) {
                if(((GNode) o).getName().equals("ClassWrapper")) {
                    System.out.println("},\n{");
                }
                System.out.println("\"" + ((GNode)(backingList.get(i))).getName() + "\",");
            } else if(o instanceof MappingNode.DataField) {
                System.out.println("\"" + ((MappingNode.DataField)(backingList.get(i))).getVal() + "\",");

            }
        }
    }
}
