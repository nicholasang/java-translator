package edu.nyu.oop.customUtil;

import java.util.*;

import edu.nyu.oop.CPPHeaderAstGenerator;
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

/*
public class JavaAstVisitor extends xtc.tree.Visitor {
    GNode cppHeaderAstRoot;

    public void visit(Node n, GNode cppHeaderAstRoot) {
        this.cppHeaderAstRoot = cppHeaderAstRoot;
        visit(n);
    }

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) dispatch((Node) o);
        }
    }

    public void visitCompilationUnit(GNode n) {
        visit(n);

    }

    public void visitPackageDeclaration(GNode n) {

        GNode qualifiedIdentifier = (GNode)n.get(1);

        ArrayList<String> packageNames = new ArrayList<String>(qualifiedIdentifier.size());

        for(int i = 0; i < qualifiedIdentifier.size(); ++i) {
            packageNames.add((String)qualifiedIdentifier.get(i));
        }

        GNode parent = this.cppHeaderAstRoot;
        GNode namespace = null;
        for(String s : packageNames) {
            //namespace = CPPHeaderAstGenerator.createMappingNodeOneShot("Namespace", "Name", s);

            if(parent != null) {
                parent.addNode(namespace);
            }

            parent = namespace;
        }


        //visit(n, parent);
    }

}
*/

public class JavaAstVisitor extends xtc.tree.Visitor {
    GNode cppHeaderAstRoot;

    public void visit(Node n, GNode cppHeaderAstRoot) {
        this.cppHeaderAstRoot = cppHeaderAstRoot;
        visit(n);
    }

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) dispatch((Node) o);
        }
    }

    public void visitCompilationUnit(GNode n) {
        visit(n);

    }

    public void visitPackageDeclaration(GNode n) {

        GNode qualifiedIdentifier = (GNode)n.get(1);

        ArrayList<String> packageNames = new ArrayList<String>(qualifiedIdentifier.size());

        for(int i = 0; i < qualifiedIdentifier.size(); ++i) {
            packageNames.add((String)qualifiedIdentifier.get(i));
        }

        GNode parent = this.cppHeaderAstRoot;
        GNode namespace = null;
        for(String s : packageNames) {
            //namespace = CPPHeaderAstGenerator.createAndLinkDataFieldMappingNodeOneShot(parent, "Namespace", "Name", s);
            //parent = namespace;

            //could be a one-liner:
            parent = CPPHeaderAstGenerator.createAndLinkDataFieldMappingNodeOneShot(parent, "Namespace", "Name", s);

            CPPHeaderAstGenerator.cppHeaderMostRecentParent = parent;

        }


        //visit(n, parent);
    }

}