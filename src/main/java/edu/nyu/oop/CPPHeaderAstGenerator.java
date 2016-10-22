package edu.nyu.oop;

import xtc.tree.GNode;

import edu.nyu.oop.customUtil.InheritanceHierarchyTreeGenerator.*;

import edu.nyu.oop.customUtil.MappingNodeEntry.DataField;



import edu.nyu.oop.customUtil.*;

import java.util.*;

public class CPPHeaderAstGenerator {
    private CPPHeaderAstGenerator() {}

    public static ArrayList<CPPHeaderAst> allCppHeaderAsts;

    //most recent parent
    public static GNode cppHeaderMostRecentParent;

    //this pointer references the list belonging to the AST currently being constructed, can be redirected
    public static ArrayList<Object> allEntries;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* VERSION 3 MAIN */
    public static ArrayList<CPPHeaderAst> generateNew(List<GNode> javaRoots, InheritanceHierarchyTree tree) {

        int i = 0;

        allCppHeaderAsts = new ArrayList<CPPHeaderAst>();

        //some algorithm to build the ASTs in order of class hiararchy (superclasses to subclasses) (topological sort?)
        //track every item added, in order

        // TODO: a loop should be here

        GNode javaRoot = javaRoots.get(i);
        //new visitor
        JavaAstVisitor jav = new JavaAstVisitor();

        //create the cpp header root
        GNode cppHeaderAst = createMappingNode("SomeBigWrapperNode");

        allCppHeaderAsts.add(new CPPHeaderAst(cppHeaderAst));

        allEntries = allCppHeaderAsts.get(i).getAllEntries();

        //display the embedded hashmaps for debugging
        //InvisiblePrintObject.toggleInvisibilityCloak();


        //add preprocessor directives,
        //node must be linked with a parent before anything is added to it!
        // (Not really, it still works, but consistency is nice)
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

        replaceLocalDataFieldValue(preDirectives, "Name", "#include \"java_lang.h\"", 1);


        XtcTestUtils.prettyPrintAst(cppHeaderAst);

        // TODO
        jav.visit(javaRoot, cppHeaderAst);

        System.out.println(allEntries);

        GNode replacement = createMappingNode("UsingNamespace");
        addDataFieldMapping(replacement, "Name", "java::lang's been REPLACED!!");
        replaceNode(cppHeaderAst, replacement, 0);


        XtcTestUtils.prettyPrintAst(cppHeaderAst);

        System.exit(0);
        return null;
        //get namespace nodes

        //System.out.println(allEntries);

        //XtcTestUtils.prettyPrintAst(cppHeaderAst);

        //find every ClassDeclaration
        //List<Node> allClassDeclarations = NodeUtil.dfsAll(javaRoot, "ClassDeclaration");


        //testing
        /*
        {
            GNode firstClass = createMappingNode("ClassWrapper");
            addNode(cppHeaderMostRecentParent, firstClass);

            addDataFieldMapping(cppHeaderMostRecentParent, "WHAT AM I?", "A series of characters");


            addDataFieldMapping(cppHeaderAst, "PROGRAMMER'S NOTE", "THIS TREE IS STILL A SAPLING");

            XtcTestUtils.prettyPrintAst(cppHeaderAst);
        }
        */



        /*MORE TESTS
        for(Node c : allClassDeclarations)
        {
           GNode n = (GNode)addNode(cppHeaderMostRecentParent, createMappingNode("ClassWrapper"));
        }

        addDataFieldMapping((GNode)getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 0), "THIS_IS_A_TEST", "YEP");

        addNode((GNode)getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 0), createMappingNode("WHATAMIEVEN"));

        addDataFieldMapping((GNode)getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 0), "THIS_IS_A_TEST", "YEP2");

        replaceLocalDataFieldValue((GNode)getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 0), "THIS_IS_A_TEST", "TEST_FINISHED", 0);

        System.out.println(getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 1) == allEntries.get(getGlobalIndexOf(cppHeaderMostRecentParent, "ClassWrapper", 1)));
        */


        //XtcTestUtils.prettyPrintAst(cppHeaderAst);


        //XtcTestUtils.prettyPrintAst(javaRoot);


        //comment out the above block please and do the following:

        //call jav.visit on each class in allClassDeclarations
        //add a bunch of visitors and follow the logic of the methods/objects I created plus their relationship
        //with this class to build the tree top-to-bottom



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

            df = MappingNodeEntry.createDataField(fieldNameKey, value, nextAvailID);

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

            df = MappingNodeEntry.createDataField(fieldNameKey, value, nextAvailID);

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

    // NOTE: Better not to use
    public static Object replaceNode(GNode node, GNode childReplacement, int ithOccurrence) {

        if(node == null || childReplacement == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(childReplacement.getName());

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        Integer globalIndex;
        if((globalIndex = getGlobalIndexOf(node, childReplacement.getName(), ithOccurrence)) == null)return null;
        allEntries.set(globalIndex, childReplacement);

        node.set(getLocalIndexOf(node, childReplacement.getName(), ithOccurrence), childReplacement);

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

    public static Integer getLocalIndexOf(GNode node, String key, int ithOccurrence) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0 || ithOccurrence >= localGlobalIndices.get(0).size() || ithOccurrence < 0)return null;

        return localGlobalIndices.get(0).get(ithOccurrence);
    }

    public static ArrayList<Integer> getAllLocalIndicesOf(GNode node, String key) {
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Integer> out = new ArrayList<Integer>();

        ArrayList<Integer> localIndices = localGlobalIndices.get(0);

        out.addAll(localIndices);

        return out;
    }

    public static Integer getGlobalIndexOf(GNode node, String key, int ithOccurence) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0 || ithOccurence >= localGlobalIndices.get(1).size() || ithOccurence < 0)return null;

        return localGlobalIndices.get(1).get(ithOccurence);
    }

    public static ArrayList<Integer> getAllGlobalIndicesOf(GNode node, String key) {
        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Integer> out = new ArrayList<Integer>();

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

    public static void resumeConstructionOf(CPPHeaderAst cppH) {
        allEntries = cppH.getAllEntries();
    }


}