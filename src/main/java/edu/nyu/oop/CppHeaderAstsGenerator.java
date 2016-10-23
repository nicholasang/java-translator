package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;


import edu.nyu.oop.customUtil.InheritanceHierarchyTreeGenerator.*;

import edu.nyu.oop.customUtil.MappingNodeEntry.DataField;



import edu.nyu.oop.customUtil.*;

import java.util.*;


public class CppHeaderAstsGenerator {
    private CppHeaderAstsGenerator() {}

    public static ArrayList<CppHeaderAst> allCppHeaderAsts;

    public static CppHeaderAst currentCpph;

    //most recent parent
    public static GNode cppHeaderMostRecentParent;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* VERSION 3 MAIN */
    public static ArrayList<CppHeaderAst> generateNew(List<GNode> javaRoots, InheritanceHierarchyTree tree) {

        int i = 0;

        allCppHeaderAsts = new ArrayList<CppHeaderAst>();



        //some algorithm to build the ASTs in order of class hiararchy (superclasses to subclasses) (topological sort?)
        //track every item added, in order

        //since multiple classes in one header may extend from classes in other files, we may need to jump back and forth between
        //partially-finished trees and create pieces of class bodies here and there, rejoin later. I need to create an ArrayList of GNode pointers
        //to the class bodies

        //Need a hierarchy tree:
        /*EX:

        C and D in file 1, AST1
        B and A in file 2, AST2


        C -> B -> A   D


        HashMap<String, String>:

        //child-to-parent tree-map

        "A" {}
        "B" {"A}
        "C" {"B"}
        "D" {}

        in addition, need one to map names to ptrs

        HashMap<String, GNode>:

        "A" {classBodyA}
        "B" {classBodyB}
        "C" {classBodyC}
        "D" {classBodyD}

        and ptrs to ASTs

        HashMap<GNode, GNode>:

        "A" {AST2}
        "B" {AST2}
        "C" {AST1}
        "D" {AST1}
        /*


         */

        //SOMETHING LIKE THIS: (OR use a class wrapper object to contain more info like ClassRef (made the skeleton)

        /*
        HashMap<GNode, GNode> classToJAstMap = new HashMap<GNode, GNode>();

        for(GNode file : javaRoots)
        {
            CppHeaderAst h = new CppHeaderAst("SomeBigWrapperNode");

            List<Node> classes = NodeUtil.dfsAll(file, "ClassDeclaration");

            for(Node n : classes)
            {
                h.addClass((GNode)n);
                classToJAstMap.put((GNode)n, file);
            }


            allCppHeaderAsts.add(h);

        }

        HashMap<String, String> childNametoParentNameMap = new HashMap<String, String>();
        HashMap<String, GNode> classNameToBodyMap = new HashMap<String, GNode>();


        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        System.exit(0);
        /*

         */
        /*
        We need to jump around so we can fill the vtables/structs in order of increasing number of parents

        -also an intermediary structure to contain entries in our structs per class--one per our Class object (GNode pointing to class body)
        -probably will contain a linear list of methods as well as a linear list of fields, two of each for public/package private and private/static

        create a default intermediary structure with information from Object, save it (I will call the intermediary structure a schematic for now)
        Start with A or D:

        EX: D:
        see that it has no parent, create a full copy of the Object schematic, assign it to parent somehow (Another HashMap is fine)

        move to class A, see the same as for D, create a full copy of Object's schematic, assign

        ...somehow use topological sorting to figure where to go next, or something simpler if available.

        visit B next, see that its parent is A-- make a full copy of A's *public/package private schematic, but since the parent isn't Object, maybe
        simultaneously figure which methods and fields in A override A's methods and fields instead of making a full copy first. Possibly use some sort of ordered set

        visit C next, etc.

        I included ways to track the current AST and also what is the most recent parent to which we added something (manually updated since sometimes
        making that automatic wouldn't be helpful) NodeUtils.dfs would also work most of the time, but both the recent parent pointer and that may be helpful
         */

        // TODO: a loop should be here

        GNode javaRoot = javaRoots.get(i);
        //new visitor
        JavaAstVisitor jav = new JavaAstVisitor();

        //create the cpp header root
        GNode cppHeaderAst = createMappingNode("SomeBigWrapperNode");

        CppHeaderAst next = new CppHeaderAst(cppHeaderAst);
        allCppHeaderAsts.add(next);
        currentCpph = next;

        //display the embedded hashmaps for debugging
        //InvisiblePrintObject.toggleInvisibilityCloak();


        //add preprocessor directives,
        //node must be linked with a parent before anything is added to it!
        // (Not really, it still works, but consistency is nice)
        GNode preDirectives = createMappingNode("PreprocessorDirectives");
        addNode(cppHeaderAst, preDirectives);
        addDataFieldMappingMulti(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

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

        System.out.println(currentCpph.getAllEntries());

        GNode replacement = createMappingNode("UsingNamespace");
        addDataFieldMapping(replacement, "Name", "java::lang's been REPLACED!!");
        replaceNode(cppHeaderAst, replacement, 0);


        XtcTestUtils.prettyPrintAst(cppHeaderAst);

        System.out.println("\n\n" + currentCpph.getAllEntries());

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


        dataMap.put("%ALL", all);

        construct.add(new InvisiblePrintObject(dataMap));

        return construct;
    }

    public static Object addDataFieldMapping(GNode node, String fieldNameKey, String value) {

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(fieldNameKey);

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("%ALL");


        DataField df;

        if (localGlobalIndices == null || localGlobalIndices.size() == 0) {
            int nextAvailID = currentCpph.getAllEntries().size();

            df = MappingNodeEntry.createDataField(fieldNameKey, value, nextAvailID);

            ArrayList<Integer> localIndices = new ArrayList<Integer>();
            localIndices.add(node.size());
            ArrayList<Integer> globalIndices = new ArrayList<Integer>();
            globalIndices.add(nextAvailID);

            localGlobalIndices = new ArrayList<ArrayList<Integer>>();
            localGlobalIndices.add(localIndices);
            localGlobalIndices.add(globalIndices);

            currentCpph.getAllEntries().add(df);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);

            dataMap.put(fieldNameKey, localGlobalIndices);


        } else {
            int nextAvailID = currentCpph.getAllEntries().size();

            df = MappingNodeEntry.createDataField(fieldNameKey, value, nextAvailID);

            localGlobalIndices.get(0).add(node.size());

            localGlobalIndices.get(1).add(nextAvailID);

            currentCpph.getAllEntries().add(df);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);
        }

        node.add(df);

        return node;
    }

    public static Object addDataFieldMappingMulti(GNode node, String fieldNameKey, ArrayList<String> values) {

        if(node == null)return null;

        Object success = null;
        for(String value : values) {
            if((success = addDataFieldMapping(node, fieldNameKey, value)) == null)return null;
        }
        return success;
    }

    // TODO: IS A COMMA-DELIMITED STRING GOOD ENOUGH, OR SHOULD I MODIFY SO A LIST OF STRINGS IS CONTAINED, WOULD REQUIRE MORE WORK, BUT DOABLE
    public static Object addDataFieldMappingAsList(GNode node, String fieldNameKey, ArrayList<String> values) {

        if(node == null)return null;

        Object success = null;
        StringBuilder sb = new StringBuilder();
        int s = 0;
        int bound = values.size() - 1;
        for(; s < bound; ++s) {
            sb.append(values.get(s));
            sb.append(", ");
        }
        sb.append(values.get(s));

        if((success = addDataFieldMapping(node, fieldNameKey, sb.toString())) == null)return null;

        return success;
    }

    public static GNode createAndLinkDataFieldMappingNodeOneShot(GNode parent, String constructType, String fieldNameKey, String value) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);
        addDataFieldMapping(construct, fieldNameKey, value);
        return construct;
    }

    public static GNode createAndLinkDataFieldMappingNodeOneShotMulti(GNode parent, String constructType, String fieldNameKey, ArrayList<String> values) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);

        for(String value : values) {
            addDataFieldMapping(construct, fieldNameKey, value);
        }
        return construct;
    }

    public static GNode createAndLinkDataFieldMappingNodeOneShotAsList(GNode parent, String constructType, String fieldNameKey, ArrayList<String> values) {

        if(parent == null)return null;

        GNode construct = createMappingNode(constructType);
        addNode(parent, construct);

        for(String value : values) {
        }

        StringBuilder sb = new StringBuilder();
        int s = 0;
        int bound = values.size() - 1;
        for(; s < bound; ++s) {
            sb.append(values.get(s));
            sb.append(", ");
        }
        sb.append(values.get(s));

        addDataFieldMapping(construct, fieldNameKey, sb.toString());

        return construct;
    }

    public static Object addNode(GNode node, GNode child) {

        if(node == null || child == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();

        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(child.getName());

        ArrayList<ArrayList<Integer>> all = (ArrayList<ArrayList<Integer>>)dataMap.get("%ALL");


        if(localGlobalIndices == null || localGlobalIndices.size() == 0) {
            int nextAvailID = currentCpph.getAllEntries().size();

            ArrayList<Integer> localIndices = new ArrayList<Integer>();
            localIndices.add(node.size());
            ArrayList<Integer> globalIndices = new ArrayList<Integer>();
            globalIndices.add(nextAvailID);

            localGlobalIndices = new ArrayList<ArrayList<Integer>>();
            localGlobalIndices.add(localIndices);
            localGlobalIndices.add(globalIndices);

            currentCpph.getAllEntries().add(child);

            all.get(0).add(node.size());
            all.get(1).add(nextAvailID);

            dataMap.put(child.getName(), localGlobalIndices);


        } else {
            int nextAvailID = currentCpph.getAllEntries().size();
            localGlobalIndices.get(0).add(node.size());

            localGlobalIndices.get(1).add(nextAvailID);

            currentCpph.getAllEntries().add(child);

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
        currentCpph.getAllEntries().set(globalIndex, childReplacement);

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

        if(node == null)return null;

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

        if(node == null)return null;

        LinkedHashMap<String, ArrayList<ArrayList<Integer>>> dataMap = (LinkedHashMap<String, ArrayList<ArrayList<Integer>>>)((InvisiblePrintObject)node.get(0)).get();
        ArrayList<ArrayList<Integer>> localGlobalIndices = (ArrayList<ArrayList<Integer>>)dataMap.get(key);

        if(localGlobalIndices == null || localGlobalIndices.size() == 0)return null;

        ArrayList<Integer> out = new ArrayList<Integer>();

        ArrayList<Integer> globalIndices = localGlobalIndices.get(1);

        out.addAll(globalIndices);

        return out;
    }

    public static Object replaceLocalDataFieldValue(GNode node, String key, String value, int ithOccurrence) {

        if(node == null)return null;

        Object o;
        Integer i;
        if((i = (Integer)(getLocalIndexOf(node, key, ithOccurrence))) == null
                || (o = node.get(i)) == null)return null;

        if(o instanceof DataField) {
            ((DataField) o).setVal(value);
        }

        return node;
    }

    public static void resumeConstructionOfClass(ClassRef cR) {
        //save what we were doing. This is almost like the mindset behind process switching

        //decided to let the programmer explicitly decide whether to update the parent,
        //the parent pointer is more of a special tool when using the visitors than
        //something that should be done automatically
        //currentCpph.setMostRecentParent(CppHeaderAstsGenerator.cppHeaderMostRecentParent);
        currentCpph = cR.getCppHAstObj();
    }


}