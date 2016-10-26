package edu.nyu.oop;

import edu.nyu.oop.util.JavaAstVisitor;
import xtc.tree.GNode;

import edu.nyu.oop.util.MappingNode;
import edu.nyu.oop.util.MappingNode.DataField;


import java.util.*;


public class CppHeaderAstGenerator {
    private CppHeaderAstGenerator() {}


    public static ArrayList<CppAst> allCppHeaderAsts;

    //most recent parent moved to the CppAst class
    //public static GNode cppHeaderMostRecentParent;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* VERSION 4 MAIN */ //NOTE: Not sure about return type yet, could return list of ClassRef + the hierarchy tree + other info
    public static ArrayList<CppAst> generateNew(List<GNode> javaAsts) { //decided to create the tree inside this class

        allCppHeaderAsts = new ArrayList<CppAst>();




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
            CppAst h = new CppAst("SomeBigWrapperNode");

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

        // TODO: visitation order determination here

        GNode javaRoot = javaAsts.get(0);


        //create the cpp header AST
        CppAst cppHeaderAst = new CppAst("SomeBigWrapperNode");
        //cpph = cppHeaderAst;


        //new visitor
        JavaAstVisitor jav = new JavaAstVisitor();

        //System.out.println(MappingNode.getEntryRepository());

        MappingNode.setEntryRepository(cppHeaderAst.getAllEntries());

        //display the embedded hashmaps for debugging
        //InvisiblePrintObject.toggleInvisibilityCloak();


        //add preprocessor directives,
        //node must be linked with a parent before anything is added to it!
        // (Not really, it still works, but consistency is nice)
        GNode preDirectives = MappingNode.createMappingNode("PreprocessorDirectives");
        MappingNode.addNode(cppHeaderAst.getRoot(), preDirectives);
        MappingNode.addDataFieldMultiVals(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

        //usingnamespace, "one-shot create and link with parent node" example
        GNode usingNamespace = MappingNode.createAndLinkDataFieldOneShot(cppHeaderAst.getRoot(),"UsingNamespace", "Name", "java::lang");

        //XtcTestUtils.prettyPrintAst(cppHeaderAst.getRoot());

        /*

        System.out.println("\nTESTING");
        System.out.println(MappingNode.getInstanceOf(preDirectives, "Name", 2));
        System.out.println(MappingNode.getAllInstancesOf(preDirectives, "Name"));
        System.out.println(MappingNode.getAllInstancesOf(preDirectives, "WEE"));

        System.out.println(MappingNode.getAllLocalIndicesOf(preDirectives, "Name"));
        System.out.println(MappingNode.getLocalIndexOf(preDirectives, "Name", 2));

        System.out.println(MappingNode.getAllGlobalIndicesOf(preDirectives, "Name"));
        System.out.println(MappingNode.getGlobalIndexOf(preDirectives, "Name", 2));
        */


        /*

        System.out.println("\nTESTING REPLACE FIELD VALUE");
        MappingNode.replaceLocalDataFieldValue(preDirectives, "Name", "SOMEONE STOLE MY INCLUDE!", 1);
        XtcTestUtils.prettyPrintAst(cppHeaderAst.getRoot());

        System.out.println(MappingNode.getEntryRepository());

        MappingNode.replaceLocalDataFieldValue(preDirectives, "Name", "#include \"java_lang.h\"", 1);



        //XtcTestUtils.prettyPrintAst(cppHeaderAst.getRoot());

        //System.out.println(MappingNode.getEntryRepository());

        */

        // TODO
        jav.visit(javaRoot, cppHeaderAst);


        XtcTestUtils.prettyPrintAst(cppHeaderAst.getRoot());

        /*
        System.out.println(cppHeaderAst.getAllEntries());


        XtcTestUtils.prettyPrintAst(cppHeaderAst.getRoot());

        System.out.println(cppHeaderAst.getAllEntries());



        XtcTestUtils.prettyPrintAst(cppHeaderAst.getRoot());


        System.out.println(cppHeaderAst.getAllEntries());


        System.out.println("\n\n" + cppHeaderAst.getAllEntries());

        System.out.println("\n");

        System.out.println("Testing getAllLocalDataFields");
        ArrayList<DataField> al = MappingNode.getAllLocalDataFields((GNode)MappingNode.getInstanceOf(cppHeaderAst.getRoot(), "Namespace", 0));

        System.out.println(al);

        System.out.println("Testing getAllLocalConstructs");

        ArrayList<GNode> alC = MappingNode.getAllLocalConstructs(cppHeaderAst.getRoot());

        for(GNode n : alC) {
            System.out.println(n.getName());
        }




        for(Object o : MappingNode.getEntryRepository())
        {
            if(o instanceof GNode)
            {
                System.out.print(((GNode)o).getName() + ", " );
            }
            else
            {
                System.out.print(o + ", ");
            }
        }
        System.out.println("\n\n\n" + MappingNode.getAllOfType("Name"));



        XtcTestUtils.prettyPrintAst(cppHeaderAst.getRoot());


        System.out.println("WEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        for(Object o : MappingNode.getEntryRepository())
        {
            if(o instanceof GNode)
            {
                System.out.print(((GNode)o).getName() + ", " );
            }
            else
            {
                System.out.print(o + ", ");
            }
        }
        System.out.println("\n\n\n" + MappingNode.getAllOfType("Name"));




        */
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

            addDataField(cppHeaderMostRecentParent, "WHAT AM I?", "A series of characters");


            addDataField(cppHeaderAst, "PROGRAMMER'S NOTE", "THIS TREE IS STILL A SAPLING");

            XtcTestUtils.prettyPrintAst(cppHeaderAst);
        }
        */



        /*MORE TESTS
        for(Node c : allClassDeclarations)
        {
           GNode n = (GNode)addNode(cppHeaderMostRecentParent, createMappingNode("ClassWrapper"));
        }

        addDataField((GNode)getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 0), "THIS_IS_A_TEST", "YEP");

        addNode((GNode)getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 0), createMappingNode("WHATAMIEVEN"));

        addDataField((GNode)getInstanceOf(cppHeaderMostRecentParent, "ClassWrapper", 0), "THIS_IS_A_TEST", "YEP2");

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




}