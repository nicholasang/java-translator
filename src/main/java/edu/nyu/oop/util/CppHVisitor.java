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

    //TODO: add tabs \t

    // preprocessor directives
    public void visitPreprocessorDirectives(GNode n) throws IOException {

        ArrayList<DataField> directives = MappingNode.getAllLocalDataFields(n);
        for(DataField directive : directives) {
            this.writeOut.write(directive.toString() + "\n");
        }
        this.writeOut.write("\n");

        visit(n);
    }

    // using namespace
    public void visitUsingNamespace(GNode n) throws IOException {
        this.writeOut.write("using namespace " + MappingNode.getInstanceOf(n, "Name", 0) + ";\n\n");
        visit(n);
    }

    // namespace
    public void visitNamespace(GNode n) throws IOException {
        this.writeOut.write("namespace " + MappingNode.getInstanceOf(n, "Name", 0) + " {\n");
        visit(n);
    }

    // forward declaration
    public void visitForwardDeclaration(GNode n) throws IOException {
        this.writeOut.write(MappingNode.getInstanceOf(n, "Type", 0) + " " +
                MappingNode.getInstanceOf(n, "Declaration", 0) + ";\n");
    }

    // type definition
    public void visitTypeDefinition(GNode n) throws IOException {
        this.writeOut.write("typedef " + MappingNode.getInstanceOf(n, "Type", 0) + " " +
                MappingNode.getInstanceOf(n, "Definition", 0) + ";\n\n");
    }

    // struct for class and v-table
    public void visitStruct(GNode n) throws IOException {
        this.writeOut.write("\n" + MappingNode.getInstanceOf(n, "Type", 0) + " " +
                MappingNode.getInstanceOf(n, "Name", 0) + " {\n");
        visit(n);
        this.writeOut.write("};\n");
    }

    // TODO: STATIC IS NOT WORKING
    // TODO: in the v-table struct, should it be (*)(equals) or (*equals)... is there a difference?
    // TODO: function pointers are incorrect in the v-table are in incorrect format
    // fields
    public void visitField(GNode n) throws IOException {
        if (MappingNode.getInstanceOf(n, "IsStatic", 0).equals("true")) {
            this.writeOut.write("static " + MappingNode.getInstanceOf(n, "Type", 0) + " " +
                    MappingNode.getInstanceOf(n, "Name", 0) + ";\n");
        }
        else {
            this.writeOut.write(MappingNode.getInstanceOf(n, "Type", 0) + " " +
                    MappingNode.getInstanceOf(n, "Name", 0) + ";\n");
        }
        visit(n);
    }

    // constructors
    // TODO: "constructor" for v-table should start with a ": __isa" on a new line, not as parameters of the constructor
    public void visitConstructor(GNode n) throws IOException {
        this.writeOut.write("\n");
        this.writeOut.write(MappingNode.getInstanceOf(n, "Name", 0) + "(");
        visit(n);
        this.writeOut.write(");\n\n");
    }

    // TODO: STATIC IS NOT WORKING
    // TODO: isn't hashCode, equals, etc supposed to be in struct __A/B/C/D as well?
    // TODO: methods in class struct do not have class type as parameter
    // methods
    public void visitMethod(GNode n) throws IOException {
        if (MappingNode.getInstanceOf(n, "IsStatic", 0).equals("true")) {
            this.writeOut.write("static " + MappingNode.getInstanceOf(n, "ReturnType", 0) + " " +
                    MappingNode.getInstanceOf(n, "Name", 0) + "(");
        }
        else {
            this.writeOut.write(MappingNode.getInstanceOf(n, "ReturnType", 0) + " " +
                    MappingNode.getInstanceOf(n, "Name", 0) + "(");
        }
        visit(n);
        this.writeOut.write(");\n");
    }

    // Parameters
    // TODO: NEEDS COMMAS BETWEEN PARAMETERS
    // TODO: REMOVE WHITESPACE AT END
    // TODO: struct __A has an extra A constructor
    public void visitParameter(GNode n) throws IOException {
        ArrayList<DataField> parameters = MappingNode.getAllLocalDataFields(n);
        for(DataField parameter : parameters) {
            this.writeOut.write(parameter.toString() + " ");
        }
        this.writeOut.write("");

        visit(n);
    }

    // init Field
    // TODO: LAST ONE SHOULDN'T HAVE COMMA
    // TODO: not sure how to get InitFieldWith to work
    public void visitInitField(GNode n) throws IOException {
        this.writeOut.write(MappingNode.getInstanceOf(n, "Name", 0) + "(");
        this.writeOut.write(MappingNode.getInstanceOf(n, "Type", 0) + " " +
                MappingNode.getInstanceOf(n, "Name", 0) + "),\n");
    }


}
