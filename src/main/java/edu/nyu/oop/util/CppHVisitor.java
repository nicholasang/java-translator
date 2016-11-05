package edu.nyu.oop.util;

import java.util.Arrays;

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
    private Writer writeOut;
    private boolean isVtable;
    private boolean isConstructor;
    private String  curAccess;

    /*
     * checks and returns the accessModifier e.g. public, private, or protected
     */
    private String checkAccess(String accessModifier) {
        boolean noAccessChange = this.curAccess.equals("");
        if(noAccessChange && accessModifier.equals("public") || this.curAccess.equals(accessModifier)) {
            return "";
        }

        switch(accessModifier) {
        case "public"    : {
            this.curAccess = "public";
            return "public:\n";
        }
        case "private"   : {
            this.curAccess = "private";
            return "private:\n";
        }
        case "protected" : {
            this.curAccess = "protected";
            return "protected:\n";
        }
        default: {
            return "";
        }
        }

    }

    //visit the header AST with a depth-first search, output to header source file
    public void visit(CppAst headerAst) {
        try {
            this.headerAst = headerAst;
            // writes to output.h
            this.writeOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + "/output/output.h")));
            this.isVtable = false;
            this.curAccess = "";
            this.isConstructor = false;

            //toString returns only the value, easier during printing phase
            DataField.togglePrintValOnly();

            visit(headerAst.getRoot());

            StringBuilder sb = new StringBuilder("\n");
            for(int i = MappingNode.getAllOfType("Namespace").size(); i > 0; --i) {
                sb.append("}\n");
            }

            this.writeOut.write(sb.toString());

            this.writeOut.flush();
            this.writeOut.close();

            DataField.togglePrintValOnly();

        } catch(IOException ex) {
            if(DataField.onlyPrintsVal()) {
                DataField.togglePrintValOnly();
            }
            System.err.println(ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
        }
    }

    public void visit(Node n) throws IllegalAccessError {
        if(this.headerAst == null) throw new IllegalAccessError();

        for (Object o : n) {
            if (o instanceof Node) {
                dispatch((Node) o);
            }
        }
    }

    // visits preprocessor directives
    public void visitPreprocessorDirectives(GNode n) throws IOException {

        ArrayList<DataField> directives = MappingNode.getAllLocalDataFields(n);
        for(DataField directive : directives) {
            this.writeOut.write(directive.toString() + "\n");
        }
        this.writeOut.write("\n");

        visit(n);
    }

    // visits using namespace
    public void visitUsingNamespace(GNode n) throws IOException {
        this.writeOut.write("using namespace " + MappingNode.getInstanceOf(n, "Name", 0) + ";\n\n");
        visit(n);
    }

    // visits namespace
    public void visitNamespace(GNode n) throws IOException {
        this.writeOut.write("namespace " + MappingNode.getInstanceOf(n, "Name", 0) + " {\n");
        visit(n);
    }

    // visits forward declarations
    public void visitForwardDeclaration(GNode n) throws IOException {
        this.writeOut.write(MappingNode.getInstanceOf(n, "Type", 0) + " " + MappingNode.getInstanceOf(n, "Declaration", 0) + ";\n");
    }

    // visits typedefs
    public void visitTypeDefinition(GNode n) throws IOException {
        this.writeOut.write("typedef " + MappingNode.getInstanceOf(n, "Type", 0) + " " + MappingNode.getInstanceOf(n, "Definition", 0) + ";\n\n");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // struct for class and v-table
    public void visitStruct(GNode n) throws IOException {
        this.writeOut.write("\n" + MappingNode.getInstanceOf(n, "Type", 0) + " " +
                            MappingNode.getInstanceOf(n, "Name", 0) + " {\n");
        visit(n);

        this.writeOut.write("};\n");

        this.isVtable = !this.isVtable;
        this.curAccess = "";

    }

    // visits fields
    public void visitField(GNode n) throws IOException {
        String accessModifier = MappingNode.getInstanceOf(n, "AccessModifier", 0).toString();

        Object toOutput = MappingNode.getInstanceOf(n, "FullName", 0);
        String name;
        if(toOutput != null) {
            this.writeOut.write(checkAccess(accessModifier) + toOutput + ";\n");
        } else if ((name = MappingNode.getInstanceOf(n, "Name", 0).toString()).equals("__vtable") ) {
            this.writeOut.write("static " +
                                MappingNode.getInstanceOf(n, "Type", 0) + " " +name + ";\n");
        } else {
            this.writeOut.write(checkAccess(accessModifier) +
                                MappingNode.getInstanceOf(n, "Type", 0) + " " +name + ";\n");
        }
        visit(n);
    }



    // constructors
    public void visitConstructor(GNode n) throws IOException {
        String accessModifier = MappingNode.getInstanceOf(n, "AccessModifier", 0).toString();
        this.writeOut.write("\n" + checkAccess(accessModifier) + MappingNode.getInstanceOf(n, "Name", 0) + "(");
        this.isConstructor = true;
        visit(n);

        this.isConstructor = false;

        if(!this.isVtable) {
            this.writeOut.write(");\n\n");
        }
    }

    //only in the class struct
    public void visitMethod(GNode n) throws IOException {
        this.writeOut.write("static " + MappingNode.getInstanceOf(n, "ReturnType", 0) + " " +
                            MappingNode.getInstanceOf(n, "Name", 0) + "(");

        visit(n);

        this.writeOut.write(");\n");
    }

    // Parameters
    public void visitParameterList(GNode n) throws IOException {

        ArrayList<GNode> parameterEntries = MappingNode.getAllLocalConstructs(n);

        StringBuilder sb = new StringBuilder();

        if(this.isVtable) { //in vtable
            this.writeOut.write(")");
        } else { //in class struct

            if (this.isConstructor) { //constructor
                for (int p = 0, len = parameterEntries.size(); p < len; ++p) {
                    ArrayList<DataField> parameterInfo = MappingNode.getAllLocalDataFields(parameterEntries.get(p));

                    sb.append(parameterInfo.get(0).toString()).append(" ").append(parameterInfo.get(1)).append((p < len - 1) ? ", " : "");
                }
            } else { //method
                for (int p = 0, len = parameterEntries.size(); p < len; ++p) {
                    ArrayList<DataField> parameterInfo = MappingNode.getAllLocalDataFields(parameterEntries.get(p));

                    sb.append(parameterInfo.get(0).toString()).append((p < len - 1) ? ", " : "");
                }
            }
            this.writeOut.write(sb.toString());
        }
        visit(n);
    }

    // visits initialization list, initfield, and initfieldwith
    public void visitInitializationList(GNode n) throws IOException {
        ArrayList<GNode> initFields = MappingNode.getAllLocalConstructs(n);
        StringBuilder sb = new StringBuilder(":\n");
        for (int in = 0, len = initFields.size(); in < len; ++in) {
            String name = MappingNode.getInstanceOf(initFields.get(in), "Name", 0).toString();

            GNode initFieldWith = (GNode) MappingNode.getInstanceOf(initFields.get(in), "InitFieldWith", 0);

            sb.append(name).append("((").
            append(MappingNode.getInstanceOf(initFieldWith, "Type", 0)).append(")").
            append(MappingNode.getInstanceOf(initFieldWith, "Name", 0)).
            append((in < len - 1) ? "),\n " : ")");
        }

        this.writeOut.write((sb.append("\n{\n}\n")).toString());

        visit(n);
    }
}
