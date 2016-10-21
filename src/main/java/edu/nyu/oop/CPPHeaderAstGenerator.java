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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* VERSION 2 MAIN */
    public static void generateNew(GNode javaRoot, InheritanceHierarchyTree tree) {

        //track every item added, in order
        allEntries = new ArrayList<Object>();

        //new visitor
        JavaAstVisitor jav = new JavaAstVisitor();

        //create the cpp header root
        GNode cppHeaderAst = createMappingNode("SomeBigWrapperNode");

        allEntries.add(cppHeaderAst); //no hashmap stores the root, but the root is implicitly the first item in allEntries

        //display the embedded hashmaps for debugging
        InvisiblePrintObject.toggleInvisibleCloak();


        //add preprocessor directives, node must be linked with a parent before anything is added to it!
        GNode preDirectives = createMappingNode("PreprocessorDirectives");
        addNode(cppHeaderAst, preDirectives);
        addDataFieldMapping(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

        //usingnamespace, "one-shot create and link with parent node" example
        GNode usingNamespace = createAndLinkDataFieldMappingNodeOneShot(cppHeaderAst,"UsingNamespace", "Name", "java::lang");

        XtcTestUtils.prettyPrintAst(cppHeaderAst);


        System.out.println("\nTESTING");
        System.out.println(getInstanceOf(preDirectives, "Name", 2));
        System.out.println(getAllInstancesOf(preDirectives, "Name"));
        System.out.println(getAllInstancesOf(preDirectives, "WEE"));

        System.out.println(getAllLocalIndicesOf(preDirectives, "Name"));
        System.out.println(getLocalIndexOf(preDirectives, "Name", 2));

        System.out.println(getAllGlobalIndicesOf(preDirectives, "Name"));
        System.out.println(getGlobalIndexOf(preDirectives, "Name", 2));


        System.out.println("\nTESTING REPLACE FIELD VALUE");
        replaceLocalDataFieldValue(preDirectives, "Name", "SOMEONE STOLE MY INCLUDE!", 1);
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

        construct.add(new InvisiblePrintObject(dataMap));

        return construct;
    }

    public static Object addDataFieldMapping(GNode node, String fieldNameKey, String value) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(fieldNameKey);

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("ALL");


        DataField df;

        if (localGlobalIndices == null || localGlobalIndices.size() == 0) {
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

        return node;
    }

    public static Object addDataFieldMapping(GNode node, String fieldNameKey, ArrayList<String> values) {

        Object success = null;
        for(String value : values) {
            if((success = addDataFieldMapping(node, fieldNameKey, value)) == null)return null;
        }
        return success;
    }

    public static GNode createAndLinkDataFieldMappingNodeOneShot(GNode parent, String constructType, String fieldNameKey, String value) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);
        addDataFieldMapping(construct, fieldNameKey, value);
        return construct;
    }

    public static GNode createAndLinkDataFieldMappingNodeOneShot(GNode parent, String constructType, String fieldNameKey, ArrayList<String> values) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);

        for(String value : values) {
            addDataFieldMapping(construct, fieldNameKey, value);
        }
        return construct;
    }

    /*
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
    */


    public static Object addNode(GNode node, GNode child) {

        if(node == null || child == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(child.getName());

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("ALL");


        if(localGlobalIndices == null || localGlobalIndices.size() == 0) {
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

        return node;
    }

    public static Object getInstanceOf(GNode node, String key, int index) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || index >= localGlobalIndices.get(0).size() || index < 0)return null;

        return node.get(localGlobalIndices.get(0).get(index));
    }

    public static ArrayList<Object> getAllInstancesOf(GNode node, String key) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Object> out = new ArrayList<Object>();

        ArrayList<Integer> localIndices = localGlobalIndices.get(0);
        int len = localIndices.size();
        for(int i = 0; i < len; ++i) {
            out.add(node.get(localIndices.get(i)));
        }

        return out;
    }

    public static Object getLocalIndexOf(GNode node, String key, int ithOccurrence) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0 || ithOccurrence >= localGlobalIndices.get(0).size() || ithOccurrence < 0)return null;

        return localGlobalIndices.get(0).get(ithOccurrence);
    }

    public static ArrayList<Object> getAllLocalIndicesOf(GNode node, String key) {
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Object> out = new ArrayList<Object>();

        ArrayList<Integer> localIndices = localGlobalIndices.get(0);

        out.addAll(localIndices);

        return out;
    }

    public static Object getGlobalIndexOf(GNode node, String key, int ithOccurence) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0 || ithOccurence >= localGlobalIndices.get(1).size() || ithOccurence < 0)return null;

        return localGlobalIndices.get(1).get(ithOccurence);
    }

    public static ArrayList<Object> getAllGlobalIndicesOf(GNode node, String key) {
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Object> out = new ArrayList<Object>();

        ArrayList<Integer> globalIndices = localGlobalIndices.get(1);

        out.addAll(globalIndices);

        return out;
    }

    public static Object replaceLocalDataFieldValue(GNode node, String key, String value, int ithOccurrence) {
        Object o = node.get((Integer)(getLocalIndexOf(node, key, ithOccurrence)));

        if(o instanceof DataField) {
            ((DataField) o).setVal(value);
        }

        return null;
    }



}
