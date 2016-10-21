package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;

import edu.nyu.oop.customUtil.InheritanceHierarchyTreeGenerator.*;
import edu.nyu.oop.customUtil.MappingNodeEntry.DataFieldList;
import edu.nyu.oop.customUtil.MappingNodeEntry.NodeEntry;

import edu.nyu.oop.customUtil.MappingNodeEntry2.DataField;



import edu.nyu.oop.customUtil.*;

import java.util.*;

public class CPPHeaderAstGenerator {
    private CPPHeaderAstGenerator() {}

    //public static List<MappingNodeEntry> allEntries;
    public static ArrayList<Object> allEntries;


    public static void generate(GNode javaRoot, InheritanceHierarchyTree tree) {




        /*STEP-WISE creation

        //SomeBigWrapperNode
        GNode cppHeaderAst = GNode.create("SomeBigWrapperNode");


        //preprocessor directives
        GNode preDirectives = createMappingNode("PrecompilerDeclarations");
        //add data fields
        addDataFieldMapping(preDirectives, "Names", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")));
        //link the node to the root
        cppHeaderAst.add(preDirectives);

        //namespaces
        GNode usingNamespace = createMappingNode("UsingNamespace");
        addDataFieldMapping(usingNamespace, "Names", "java::lang");
        cppHeaderAst.add(usingNamespace);
        */

        //ONE-SHOT CREATION:

        /*
        //SomeBigWrapperNode
        GNode cppHeaderAst = GNode.create("SomeBigWrapperNode");

        GNode preDirectives = createMappingNodeOneShot("PrecompilerDeclarations", "Names", new ArrayList<String>(
                Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>") ));

        cppHeaderAst.add(preDirectives);

        GNode usingNamespace = createMappingNodeOneShot("UsingNamespace", "Names", "java::lang");

        cppHeaderAst.add(usingNamespace);
        */


        //DEFAULT OUTER SHELL CREATION EXAMPLE:

        //SomeBigWrapperNode //I know other hard-codeable nodes are missing

        /*
        GNode cppHeaderAst = initSomeBigWrapperNode();


        System.out.println("JAVA_____________________________________");
        XtcTestUtils.prettyPrintAst(javaRoot);

        System.out.println("\nCPPHeader_____________________________________");

        XtcTestUtils.prettyPrintAst(cppHeaderAst);


        //get namespace nodes
        jav.visit(javaRoot, cppHeaderAst);

        XtcTestUtils.prettyPrintAst(cppHeaderAst);

        //find every ClassDeclaration
        List<Node> allClassDeclarations = NodeUtil.dfsAll(javaRoot, "ClassDeclaration");
        */





    }

    //methods
    /* VERSION 1

    public static GNode createMappingNode(String constructType) {

    GNode construct = GNode.create(constructType);
    LinkedHashMap<String, MappingNodeEntry> dataMap = new LinkedHashMap<String, MappingNodeEntry>();


    DataFieldList all = (DataFieldList)MappingNodeEntry.createDataFieldList();
    all.setAsAllMarker();
    dataMap.put("ALL", all);

    construct.add(dataMap);

    return construct;
    }


    public static GNode createMappingNodeOneShot(String constructType, String fieldNameKey, ArrayList<String> values) {
    GNode construct = createMappingNode(constructType);
    addDataFieldMapping(construct, fieldNameKey, values);
    return construct;
    }

    public static GNode createMappingNodeOneShot(String constructType, String fieldNameKey, String value) {
    GNode construct = createMappingNode(constructType);
    addDataFieldMapping(construct, fieldNameKey, value);
    return construct;
    }

    public static GNode initSomeBigWrapperNode() {
    //SomeBigWrapperNode
    GNode cppHeaderAst = GNode.create("SomeBigWrapperNode");

    GNode preDirectives = createMappingNodeOneShot("PrecompilerDeclarations", "Names", new ArrayList<String>(
                              Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>") ));

    cppHeaderAst.add(preDirectives);

    GNode usingNamespace = createMappingNodeOneShot("UsingNamespace", "Names", "java::lang");

    cppHeaderAst.add(usingNamespace);

    return cppHeaderAst;
    }


    public static void addDataFieldMapping(GNode node, String fieldNameKey, ArrayList<String> values) {
    LinkedHashMap<String, MappingNodeEntry> dataMap = (LinkedHashMap<String, MappingNodeEntry>)node.get(0);

    DataFieldList dfl = (DataFieldList)dataMap.get(fieldNameKey);

    DataFieldList all = (DataFieldList)dataMap.get("ALL");


    if(dfl == null) {
        dfl = new DataFieldList(values);
        dataMap.put(fieldNameKey, dfl);
        allEntries.add(dfl);

        all.append(values);
    } else {
        for(String value : values) {
            dfl.append(value);

            all.append(values);

        }
    }

    }

    public static void addDataFieldMapping(GNode node, String fieldNameKey, String value) {

    LinkedHashMap<String, MappingNodeEntry> dataMap = (LinkedHashMap<String, MappingNodeEntry>)node.get(0);

    DataFieldList dfl = (DataFieldList)dataMap.get(fieldNameKey);

    DataFieldList all = (DataFieldList)dataMap.get("ALL");


    if(dfl == null) {
        dfl = new DataFieldList(value);
        dataMap.put(fieldNameKey, dfl);
        allEntries.add(dfl);

        all.append(value);

    } else {
        dfl.append(value);

        all.append(value);

    }
    }
    */

    // TODO: add official MappingNode adder for NodeEntry subclass of MappingNode to keep track of previous node found

    /* VERSION 2 MAIN */
    public static void generateNew(GNode javaRoot, InheritanceHierarchyTree tree) {

        InvisiblePrintObject allEntriesEmbeddedInTree = new InvisiblePrintObject(allEntries = new ArrayList<Object>());
        JavaAstVisitor jav = new JavaAstVisitor();

        GNode cppHeaderAst = createMappingNode("SomeBigWrapperNode");


        //preprocessor directives
        /*
        GNode preDirectives = createDataFieldMappingNodeOneShot("PrecompilerDeclarations", "Name",
                              new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")));
        */


        GNode preDirectives = createMappingNode("ProcompilerDeclarations");
        addNode(cppHeaderAst, preDirectives);
        addDataFieldMapping(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

        //usingnamespace, oneshot create and link with parent
        GNode usingNamespace = createAndLinkDataFieldMappingNodeOneShot(cppHeaderAst,"UsingNamespace", "Name", "java::lang");

        XtcTestUtils.prettyPrintAst(cppHeaderAst);

        System.out.println(allEntries);


    }

    public static GNode createMappingNode(String constructType) {

        GNode construct = GNode.create(constructType);
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = new LinkedHashMap<String, ArrayList<ArrayList<Integer>>>();


        ArrayList<ArrayList<Integer>> all = new ArrayList<ArrayList<Integer>>();
        all.add(new ArrayList<Integer>());
        all.add(new ArrayList<Integer>());


        dataMap.put("ALL", all);

        construct.add(dataMap);

        return construct;
    }

    public static void addDataFieldMapping(GNode node, String fieldNameKey, String value) {

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>) node.get(0);

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(fieldNameKey);

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("ALL");


        DataField df;

        if (localGlobalIndices == null) {
            int nextAvailID = allEntries.size();

            df = MappingNodeEntry2.createDataField(fieldNameKey, value, nextAvailID);

            ArrayList<Integer> localIndices = new ArrayList<Integer>();
            localIndices.add(node.size());
            ArrayList<Integer> globalIndices = new ArrayList<Integer>();
            globalIndices.add(nextAvailID);

            localGlobalIndices = new ArrayList<ArrayList<Integer>>();
            localGlobalIndices.add(localIndices);
            localGlobalIndices.add(globalIndices);

            allEntries.add(df);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);

            dataMap.put(fieldNameKey, localGlobalIndices);


        } else {
            int nextAvailID = allEntries.size();

            df = MappingNodeEntry2.createDataField(fieldNameKey, value, nextAvailID);

            localGlobalIndices.get(0).add(node.size());

            localGlobalIndices.get(1).add(nextAvailID);

            allEntries.add(df);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);
        }

        node.add(df);
    }

    public static void addDataFieldMapping(GNode node, String fieldNameKey, ArrayList<String> values) {

        for(String value : values) {
            addDataFieldMapping(node, fieldNameKey, value);
        }
    }

    public static GNode createAndLinkDataFieldMappingNodeOneShot(GNode parent, String constructType, String fieldNameKey, String value) {
        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);
        addDataFieldMapping(construct, fieldNameKey, value);
        return construct;
    }

    public static GNode createAndLinkDataFieldMappingNodeOneShot(GNode parent, String constructType, String fieldNameKey, ArrayList<String> values) {
        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);

        for(String value : values) {
            addDataFieldMapping(construct, fieldNameKey, value);
        }
        return construct;
    }

    @Deprecated
    public static GNode createDataFieldMappingNodeOneShot(String constructType, String fieldNameKey, String value) {
        GNode construct = createMappingNode(constructType);
        addDataFieldMapping(construct, fieldNameKey, value);
        return construct;
    }

    @Deprecated
    public static GNode createDataFieldMappingNodeOneShot(String constructType, String fieldNameKey, ArrayList<String> values) {
        GNode construct = createMappingNode(constructType);

        for(String value : values) {
            addDataFieldMapping(construct, fieldNameKey, value);
        }
        return construct;
    }


    public static void addNode(GNode node, GNode child) {

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>) node.get(0);

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(child.getName());

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("ALL");


        if (localGlobalIndices == null) {
            int nextAvailID = allEntries.size();

            ArrayList<Integer> localIndices = new ArrayList<Integer>();
            localIndices.add(node.size());
            ArrayList<Integer> globalIndices = new ArrayList<Integer>();
            globalIndices.add(nextAvailID);

            localGlobalIndices = new ArrayList<ArrayList<Integer>>();
            localGlobalIndices.add(localIndices);
            localGlobalIndices.add(globalIndices);

            allEntries.add(child);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);

            dataMap.put(child.getName(), localGlobalIndices);


        } else {
            int nextAvailID = allEntries.size();
            localGlobalIndices.get(0).add(node.size());

            localGlobalIndices.get(1).add(nextAvailID);

            allEntries.add(child);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);
        }

        node.add(child);
    }

    //methods



}
