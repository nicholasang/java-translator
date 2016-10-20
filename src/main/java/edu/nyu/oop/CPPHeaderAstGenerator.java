package edu.nyu.oop;

import xtc.tree.GNode;

import edu.nyu.oop.customUtil.InheritanceHierarchyTreeGenerator.*;
import edu.nyu.oop.customUtil.MappingNodeEntry.DataFieldList;
import edu.nyu.oop.customUtil.MappingNodeEntry.NodeEntry;

import edu.nyu.oop.customUtil.*;

import java.util.*;

public class CPPHeaderAstGenerator
{
    private CPPHeaderAstGenerator(){}

    public static List<MappingNodeEntry> allEntries;

    public static void generate(GNode javaRoot, InheritanceHierarchyTree tree)
    {

        allEntries = new ArrayList<MappingNodeEntry>();

        JavaAstVisitor jav = new JavaAstVisitor();


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

        XtcTestUtils.prettyPrintAst(cppHeaderAst);

        //jav.visit(javaRoot, cppHeaderAst);

    }


    public static GNode createMappingNode(String constructType)
    {

        GNode construct = GNode.create(constructType);
        LinkedHashMap<String, MappingNodeEntry> dataMap = new LinkedHashMap<String, MappingNodeEntry>();


        DataFieldList all = (DataFieldList)MappingNodeEntry.createDataFieldList();
        all.setAsAllMarker();
        dataMap.put("ALL", all);

        construct.add(dataMap);

        return construct;
    }

    /*
    public static void GNode createMappingNodeOneShot(String constructType, String fieldNameKey, List<String> values)
    {

    }

    public static void GNode createMappingNodeOneShot(String constructType, String fieldNameKey, String value)
    {

    }
    */

    public static void addDataFieldMapping(GNode node, String fieldNameKey, ArrayList<String> values)
    {
        LinkedHashMap<String, MappingNodeEntry> dataMap = (LinkedHashMap<String, MappingNodeEntry>)node.get(0);

        DataFieldList dfl = (DataFieldList)dataMap.get(fieldNameKey);

        DataFieldList all = (DataFieldList)dataMap.get("ALL");


        if(dfl == null)
        {
            dfl = new DataFieldList(values);
            dataMap.put(fieldNameKey, dfl);
            allEntries.add(dfl);

            all.append(values);
        }
        else
        {
            for(String value : values)
            {
                dfl.append(value);

                all.append(values);

            }
        }

    }

    public static void addDataFieldMapping(GNode node, String fieldNameKey, String value)
    {

        LinkedHashMap<String, MappingNodeEntry> dataMap = (LinkedHashMap<String, MappingNodeEntry>)node.get(0);

        DataFieldList dfl = (DataFieldList)dataMap.get(fieldNameKey);

        DataFieldList all = (DataFieldList)dataMap.get("ALL");


        if(dfl == null)
        {
            dfl = new DataFieldList(value);
            dataMap.put(fieldNameKey, dfl);
            allEntries.add(dfl);

            all.append(value);

        }
        else
        {
            dfl.append(value);

            all.append(value);

        }
    }
}
