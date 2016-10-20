package edu.nyu.oop;


import java.util.*;

import xtc.tree.GNode;
import xtc.tree.Node;
import java.util.List;
import java.util.ArrayList;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import edu.nyu.oop.util.JavaFiveImportParser;
import edu.nyu.oop.util.NodeUtil;
import edu.nyu.oop.util.XtcProps;
import org.slf4j.Logger;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Tool;
import xtc.lang.JavaPrinter;
import xtc.parser.ParseException;
import java.util.*;

import java.util.HashMap;





public class GenerateCPPHeader
{
    private GenerateCPPHeader()
    {
    }

    public static GNode getHeaderAST(GNode root, xtc.util.Runtime runtime)
    {
        HeaderRawContainer dotH = new HeaderRawContainer(root);

        System.exit(0); //DOING THIS ON PURPOSE TO FORCE THIS TO STOP AFTER ONE FILE

        return null;
    }

    private static class HeaderRawContainer
    {
        //won't be used?
        ArrayList<String> headerContents;

        HeaderRawContainer(GNode rootJAst)
        {
            //SomeBigWrapperNode
            GNode cppHeaderAst = GNode.create("SomeBigWrapperNode", 3);

            //preprocessing directives
            GNode preDirs = GNode.create("PreprocessorDirectives", 1);
            ArrayList<String> directives = new ArrayList<String>();
            directives.addAll(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>"));
            preDirs.add(0, directives);
            cppHeaderAst.addNode(preDirs);

            {
                //testing
                //have to save outside to modify?
                ArrayList<String> s = (ArrayList<String>) preDirs.get(0);

                XtcTestUtils.prettyPrintAst(cppHeaderAst);

                //I added a WEE for good luck at one point
                //s.add("WEE");
            }

            XtcTestUtils.prettyPrintAst(cppHeaderAst);

            //save classes

            //UsingNameSpace    ... there doesn't seem to be a way to access certain info by name like Name = so and so...
            //LET's use a HashMap key-value pair system for accessing most nodes!

            GNode uNS = GNode.create("UsingNamespace", 1);
            ArrayList<String> namespaces = new ArrayList<String>();
            HashMap<String, ArrayList<String>> varMap = new HashMap<String, ArrayList<String>>();
            namespaces.add("java::lang");
            varMap.put("Names", namespaces);
            uNS.add(0, varMap);
            cppHeaderAst.add(uNS);

            //access like so

            System.out.println(access(uNS, "Names").get(0));

            //positive: less memorization of indices when multiple fields like "name", "type", etc. exist


            XtcTestUtils.prettyPrintAst(cppHeaderAst);


            //next: NameSpace nodes, then ClassWrapper nodes

            //Plan: go in layers/stages

            //save reference to all class declarators as sub-roots of tree, parse each (another BFS-like idea?



        }

        private ArrayList<String> access(GNode node, String key)
        {
            HashMap<String, ArrayList<String>> map = (HashMap<String, ArrayList<String>>)node.get(0);

            return map.get(key);

        }

    }




}
