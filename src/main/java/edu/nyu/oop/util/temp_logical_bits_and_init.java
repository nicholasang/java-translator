package edu.nyu.oop.util;


import edu.nyu.oop.ClassRef;
import edu.nyu.oop.*;
import xtc.tree.GNode;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class temp_logical_bits_and_init
{
    final static boolean THIS = true;
    final static boolean LOCAL = false;

    /* run this command:
     runxtc -translateJava src/test/java/inputs/test041/Test041.java

     */
    public static void run(CppAst header)
    {

        // to get all names
        List<ClassRef> cR = header.getClassRefs();
        List<String> classNames = new ArrayList<String>(cR.size());
        for (ClassRef c : cR)
        {
            classNames.add(c.getName());
        }

        System.out.println(classNames);

        // available mappings:
        /*
        HashMap<String, ClassRef> NameToRef = hierarchy.NameToRef;
        HashMap<String, String> childToParentMap = hierarchy.childToParentMap;
        HashMap<String, ArrayList<String>> parentToChildrenMap = hierarchy.parentToChildrenMap;
        */

        ClassHierarchyTree hierarchy = ClassRef.getHierarchy();




        // constructor and __init() implementation logic:

        // assume a node called <>NODE has been created, I'll just make it a theoretical list
        List<String> CONSTRUCTORNODE = new ArrayList<String>();
    // for each new class found (e.g.) class "C",
        String foundName = "C";
        String className = "__C";
        CONSTRUCTORNODE.add(className);

        // fill this in to create the constructor method in the tree:
        String constructorSignature = className + "::" + className + "() : __vptr(&__vtable) {\n}";
        CONSTRUCTORNODE.add(constructorSignature);


        // first create the default no-arg __init(), and for each additional constructor you find,
        // follow the same template to create __init(args...)

        List<String> INITNODEDEFAULT = new ArrayList<String>();

        String initSignature = "static void " + className + "::" + "__init(" + foundName + " o " +  /*list of args here*/ ")";

        INITNODEDEFAULT.add(initSignature);

        // get parent name:
        String parentName = hierarchy.childToParentMap.get(className);

        String parentInitCall = ""; // no special logic for printing if no parent other than Object
        // however you keep the info in the GNode, have something like this?
        if (parentName != null) // null parent means that the parent is Object
            parentInitCall = parentName + "::__init(" + "(" + parentName.substring(2) + ") o " +  /*,list of args here, empty in default*/ ")";

        // for each assignment, check symbol table whether "THIS" comes into play

        INITNODEDEFAULT.add(parentInitCall);

        String[] fakeVars = {"x", "y", "z"};

        HashMap<String,Boolean> fakeSymbolTable = new HashMap<String, Boolean>();

        fakeSymbolTable.put("x", THIS);
        fakeSymbolTable.put("y", LOCAL);
        fakeSymbolTable.put("z", THIS);

        for (String fakeVar : fakeVars)
        {
            // for variable assignments
            // assume here that field name in object is same name as var

            if (fakeSymbolTable.get(fakeVar) == THIS)
            {
                String ASSIGNMENT = "o->" + fakeVar +  " = " + fakeVar + "\n";
                INITNODEDEFAULT.add(ASSIGNMENT);
            }
            else
            {
                // DO LOCAL VARIABLE STUFF
            }
        }


        System.out.println("CONSTRUCTOR NODE CONTENTS: " + CONSTRUCTORNODE);
        System.out.println("INIT NODE CONTENTS: " + INITNODEDEFAULT);


        /*
            other possible logic/sub GNODEs or constant markers?

            ASSIGNMENT =
            EQUALITY



         */













    }
}
