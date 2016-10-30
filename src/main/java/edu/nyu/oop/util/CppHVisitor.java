package edu.nyu.oop.util;

import edu.nyu.oop.CppAst;
import edu.nyu.oop.ClassRef;
import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.ArrayList;

import static edu.nyu.oop.util.MappingNode.*;

//4-tran
public class CppHVisitor extends xtc.tree.Visitor
{
    CppAst headerAst;

    public void visit(CppAst headerAst) {
        this.headerAst = headerAst;
        visit(headerAst.getRoot());
    }

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) dispatch((Node) o);
        }
    }
}
