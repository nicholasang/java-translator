package edu.nyu.oop.customUtil;

import edu.nyu.oop.CppHeaderAstsGenerator;
import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.ArrayList;

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
            //namespace = CppHeaderAstsGenerator.createAndLinkDataFieldMappingNodeOneShot(parent, "Namespace", "Name", s);
            //parent = namespace;

            //could be a one-liner:
            parent = CppHeaderAstsGenerator.createAndLinkDataFieldMappingNodeOneShot(parent, "Namespace", "Name", s);

            CppHeaderAstsGenerator.cppHeaderMostRecentParent = parent;

        }
    }

    public void visitClassDeclaration(GNode n) {


        GNode localParent = (GNode) CppHeaderAstsGenerator.addNode(CppHeaderAstsGenerator.cppHeaderMostRecentParent, CppHeaderAstsGenerator.createMappingNode("ClassWrapper"));

        //System.out.println("ENTERING CLASS BODY");

        visit(n);

        CppHeaderAstsGenerator.cppHeaderMostRecentParent = localParent;

        //System.out.println("EXITING CLASS BODY");


    }



}