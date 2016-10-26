package edu.nyu.oop;

import edu.nyu.oop.util.JavaAstVisitor;
import xtc.tree.GNode;

import java.util.ArrayList;
import java.util.List;

import java.util.*;


import edu.nyu.oop.util.MappingNode;
import edu.nyu.oop.util.MappingNode.DataField;

public abstract class HardCodedTestCppHeaderAstGenerator {
    public static CppAst generateNew(List<GNode> javaAsts) {
        //array of ClassRef
        ArrayList<ClassRef> cRefs = new ArrayList<ClassRef>();

        //create the new ast
        CppAst headerAst = new CppAst("BigWrapperNode");

        //set the global entry repository
        MappingNode.setEntryRepository(headerAst.getAllEntries());

        GNode preDirectives = MappingNode.createMappingNode("PreprocessorDirectives");
        //link with root (there are multiple ways of adding nodes...some in one shot while also adding fields
        MappingNode.addNode(headerAst.getRoot(), preDirectives);
        //add the preprocessor directives fields
        MappingNode.addDataFieldMultiVals(preDirectives, "Name", new ArrayList<String>(Arrays.asList("#pragma once", "#include \"java_lang.h\"", "#include <stdint.h>", "#include <string>")) );

        //usingnamespace, "one-shot create and link with parent node" example
        GNode usingNamespace = MappingNode.createAndLinkDataFieldOneShot(headerAst.getRoot(),"UsingNamespace", "Name", "java::lang");

        //this visitor
        //new visitor
        JavaAstVisitor classBodyInit = new JavaAstVisitor();

        classBodyInit.visit(headerAst.getRoot(), headerAst);

        XtcTestUtils.prettyPrintAst(headerAst.getRoot());

        return null;
    }
}
