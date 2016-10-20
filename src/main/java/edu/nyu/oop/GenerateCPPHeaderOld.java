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



@Deprecated
public class GenerateCPPHeaderOld
{
    private GenerateCPPHeaderOld()
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
            /*
            //SomeBigWrapperNode
            GNode cppHeaderAst = GNode.create("SomeBigWrapperNode", 3);

            //preprocessing directives


            //GNode preDirs = GNode.create("PreprocessorDirectives", 1);
            //GNode preDirectives = createMappingNode("PreprocessorDirectives", , null);


            GNode preDirs = createMappingNode("PreprocessorDirectives", 1);
            putDataMapping(preDirs, "Names", Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>"));

            cppHeaderAst.addNode(preDirs);

            {


                XtcTestUtils.prettyPrintAst(cppHeaderAst);

                //I added a WEE for good luck at one point
                //s.add("WEE");
            }


            XtcTestUtils.prettyPrintAst(cppHeaderAst);

            //save classes

            //UsingNameSpace    ... there doesn't seem to be a way to access certain info by name like Name = so and so...
            //LET's use a HashMap key-value pair system for accessing most nodes!

            //LinkedHashMap preserves insertion order, even if you re-insert a key after updating its value (could be useful)

            GNode usingNamespaceNode = createMappingNode("UsingNamespace", 1);
            putDataMapping(usingNamespaceNode, "Names", Arrays.asList("java::lang"));


            cppHeaderAst.add(usingNamespaceNode);

            //access like so

            //list of data fields
            System.out.println(dataListAtKey(usingNamespaceNode, "Names"));
            //data field 0
            System.out.println(dataAtIndex(usingNamespaceNode, 0));


            //positive: less memorization of indices when multiple fields like "name", "type", etc. exist



            GNodeA(
                HashMap[key : val]
                GNodeB(
                )
            )



            XtcTestUtils.prettyPrintAst(cppHeaderAst);


            //next: NameSpace nodes, then ClassWrapper nodes

            //Plan: go in layers/stages

            //save reference to all class declarators as sub-roots of tree, parse each (another BFS-like idea?

            */









            //SomeBigWrapperNode


            GNode cppHeaderAst = GNode.create("SomeBigWrapperNode");



            GNode preDir = createMappingNodeV2("PrecompilerDeclarations");


            putDataMapping(preDir, "Names", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")));


            cppHeaderAst.add(preDir);


            GNode usingNS = createMappingNodeV2("UsingNamespace");
            putDataMapping(usingNS, "Names", "java::lang");

            cppHeaderAst.add(usingNS);

            XtcTestUtils.prettyPrintAst(cppHeaderAst);



        }



        private GNode createMappingNodeV2(String constructType)
        {

            GNode construct = GNode.create(constructType, 10);
            LinkedHashMap<String, ValueWrapper> dataMap = new LinkedHashMap<String, ValueWrapper>();

            dataMap.put("all", new ValueWrapper(new ArrayList<ValueWrapper>(), true));

            construct.add(dataMap);

            return construct;
        }

        private void putDataMapping(GNode construct, String infoKind, Object value)
        {
            LinkedHashMap<String, ValueWrapper> dataMap = (LinkedHashMap<String, ValueWrapper>)construct.get(0);

            ((ArrayList<ValueWrapper>)((Object)(dataMap.get("all")).get())).add(new ValueWrapper(value));

            dataMap.put(infoKind, new ValueWrapper(value));
        }

        private void putConstructChild(GNode construct, GNode child)
        {
            construct.add(child);
        }

        private GNode getConstructChild(GNode construct, long i)
        {
            if(i < 1)return null;

            return (GNode)construct.get((int)i);
        }

        private ArrayList<ValueWrapper> getAllValues(GNode node)
        {
            try
            {
                LinkedHashMap<String, ValueWrapper> dataMap = (LinkedHashMap<String, ValueWrapper>) node.get(0);
                return (ArrayList<ValueWrapper>)dataMap.get("all").get();
            }
            catch(Exception ex)
            {
                return null;
            }
        }

        private ValueWrapper getDataAtKey(GNode node, String key)
        {
            try
            {
                LinkedHashMap<String, ValueWrapper> dataMap = (LinkedHashMap<String, ValueWrapper>) node.get(0);
                return dataMap.get(key);
            }
            catch(Exception ex)
            {
                return null;
            }
        }

        private ValueWrapper getDataAtIndex(GNode node, long i)
        {
            try
            {
                LinkedHashMap<String, ValueWrapper> dataMap = (LinkedHashMap<String, ValueWrapper>) node.get(0);

                return ((ArrayList<ValueWrapper>)((Object)(dataMap.get("all")).get())).get((int)i);

            }
            catch(Exception ex)
            {
                return null;
            }
        }




        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        private GNode createMappingNode(String constructType, int numChildren)
        {
            if(numChildren < 2)numChildren = 2;

            GNode construct = GNode.create(constructType, numChildren);

            LinkedHashMap<String, List<String>> dataMap = new LinkedHashMap<String, List<String>>();
            construct.add(dataMap);

            LinkedHashMap<String, List<GNode>> childMap = new LinkedHashMap<String, List<GNode>>();
            construct.add(childMap);


            return construct;
        }


/*
        private void putDataMapping(GNode construct, String infoKind, List<String> values)
        {
            LinkedHashMap<String, List<String>> dataMap = (LinkedHashMap<String, List<String>>)construct.get(0);
            dataMap.put(infoKind, values);
        }
  */

        private void putChildMapping(GNode construct, String childKind, List<GNode> children)
        {
            LinkedHashMap<String, List<GNode>> childMap = (LinkedHashMap<String, List<GNode>>)construct.get(0);
            childMap.put(childKind, (List<GNode>)children);
        }


        //get list of data of certain type (using lists so portable between GNodes, regardless of number of data items
        private List<String> dataListAtKey(GNode node, String key)
        {
            try
            {
                LinkedHashMap<String, List<String>> dataMap = (LinkedHashMap<String, List<String>>) node.get(0);
                return dataMap.get(key);
            }
            catch(Exception ex)
            {
                return null;
            }
        }

        private String[] dataListAll(GNode node)
        {
            try
            {
                LinkedHashMap<String, List<String>> dataMap = (LinkedHashMap<String, List<String>>) node.get(0);
                String[] sA = (String[])dataMap.values().toArray();
                return sA;
            }
            catch(Exception ex)
            {
                return null;
            }
        }

        private Object dataAtIndex(GNode node, int i)
        {
            try
            {
                LinkedHashMap<String, List<String>> dataMap = (LinkedHashMap<String, List<String>>) node.get(0);

                System.out.println(dataMap);

                System.out.println("WEEE");

                Object[] s = dataMap.values().toArray();


                System.out.println("WEEEEE" + Arrays.toString(s));
                System.out.println("WEEEEEEEEEEEEEEEEEEEEEEEEEEE" + s[0]);
                System.exit(0);
                return s[i];
            }
            catch(Exception ex)
            {
                return null;
            }
        }

        private List<GNode> childListAtKey(GNode node, String key)
        {
            try
            {
                LinkedHashMap<String, List<GNode>> childMap = (LinkedHashMap<String, List<GNode>>) node.get(1);
                return childMap.get(key);
            }
            catch(Exception ex)
            {
                return null;
            }
        }

        private GNode childAtIndex(GNode node, int i)
        {
            try
            {
                LinkedHashMap<String, List<GNode>> childMap = (LinkedHashMap<String, List<GNode>>) node.get(1);
                return (GNode)childMap.values().toArray()[i];

            }
            catch(Exception ex)
            {
                return null;
            }
        }

    }




}
