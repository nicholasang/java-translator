package edu.nyu.oop.util;

import edu.nyu.oop.CppAst;
import edu.nyu.oop.ClassRef;
import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.ArrayList;

import static edu.nyu.oop.util.MappingNode.*;


//4-tran
public class InitVisitor extends xtc.tree.Visitor {
    CppAst cpph;
    GNode jAstRoot;

    public void visit(Node n, CppAst cppHeader) {
        this.cpph = cppHeader;
        this.jAstRoot = (GNode)n;
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

        GNode parent = this.cpph.getRoot();
        GNode namespace = null;
        for(String s : packageNames) {
            //namespace = CppHeaderAstsGenerator.createAndLinkDataFieldOneShot(parent, "Namespace", "Name", s);
            //parent = namespace;

            //could be a one-liner:
            parent = createAndLinkDataFieldOneShot(parent, "Namespace", "Name", s);


            cpph.setMostRecentParent(parent);

        }
    }

    public void visitClassDeclaration(GNode n) {

        //ArrayList<ClassRef> cR = cpph.getClassRefs();

    }



    //////////////////////OLD/////////////////////
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
                //namespace = CppHeaderAstsGenerator.createMappingNodeOneShot("Namespace", "Name", s);

                if(parent != null) {
                    parent.addNode(namespace);
                }

                parent = namespace;
            }


            //visit(n, parent);
        }

    }
    */





}