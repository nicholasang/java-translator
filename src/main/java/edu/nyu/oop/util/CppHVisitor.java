package edu.nyu.oop.util;

import edu.nyu.oop.CppAst;
import edu.nyu.oop.ClassRef;
import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.ArrayList;

import static edu.nyu.oop.util.MappingNode.*;

import java.io.*;

//4-tran
public class CppHVisitor extends xtc.tree.Visitor {
    CppAst headerAst;
    Writer writeOut;


    public void visit(CppAst headerAst) {
        try {
            this.headerAst = headerAst;

            this.writeOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + "/output/output.h")));
            //toString returns only the value, easier during printing phase

            DataField.togglePrintValOnly();

            visit(headerAst.getRoot());

            this.writeOut.flush();
            this.writeOut.close();

            DataField.togglePrintValOnly();
        } catch(IOException ex) {
            System.err.println(ex.getMessage() + "\n" + ex.getStackTrace());
        }
    }

    public void visit(Node n) {
        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }

    public void visitPreprocessorDirectives(GNode n) throws IOException {

        ArrayList<DataField> directives = MappingNode.getAllLocalDataFields(n);
        for(DataField directive : directives) {
            this.writeOut.write(directive.toString() + "\n");
        }
        this.writeOut.write("\n");

        visit(n);
    }

    public void visitUsingNamespace(GNode n) throws IOException {
        this.writeOut.write("using namespace " + MappingNode.getInstanceOf(n, "Name", 0) + ";\n\n");
        visit(n);
    }

    public void visitNamespace(GNode n) throws IOException {
        this.writeOut.write("namespace " + MappingNode.getInstanceOf(n, "Name", 0) + " {\n");
        visit(n);
    }

    public void visitForwardDeclaration(GNode n) throws IOException {
        this.writeOut.write(MappingNode.getInstanceOf(n, "Type", 0) + " " + MappingNode.getInstanceOf(n, "Declaration", 0) + ";\n");
    }

    public void visitTypeDefinition(GNode n) throws IOException {
        this.writeOut.write("typedef " + MappingNode.getInstanceOf(n, "Type", 0) + " " + MappingNode.getInstanceOf(n, "Definition", 0) + ";\n\n");

    }
}
