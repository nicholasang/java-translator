package edu.nyu.oop;

import xtc.tree.Node;
import xtc.tree.GNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import edu.nyu.oop.util.NodeUtil;
/**
 * Created by alex on 10/27/16.
 */
public class printOutputCpp extends xtc.tree.Visitor {

    GNode mainClass;
    FileWriter pen = null;
    String printThisAfter = " ";
    String printThisBefore = "";
    String printAtEnd = "";
    String[] printLater = null;
    String pack = "";
    String ClassName = "";
    String returnType = null;

    public printOutputCpp(FileWriter outFileWrite, String[] printLater, GNode mainClass) {
        this.printLater = printLater;
        pen = outFileWrite;
        this.mainClass = mainClass;
    }

    public void visit(Node n) {
        if (n != null) {
            for (Object o: n) {
                if (o instanceof Node) {
                    dispatch((Node) o);
                } else if (o instanceof String) {
                    try {
                        pen.write(o.toString() + " ");
                    } catch(Exception e) {
                        System.out.print(e);
                    }
                }
            }
        }
    }

    /*
     * Print string contents from indices start to end
     */
    /*    public void printContents(int start, int end, Node n){
            try {
                for (int i = 0; i < end; i++) {
                    if (n.get(i) instanceof String) {
                        String word = n.get(i).toString();
                        if (i + 1 < end) {
                            pen.write(printThisBefore + word + printThisAfter);

                        } else {
                            pen.write(printThisBefore + word + printAtEnd);
                            printAtEnd = "";
                        }
                    }
                }
                printThisBefore = "";
                printThisAfter = " ";
            }catch(Exception e){
                System.out.println(e);
            }
        }

        public void visitDimensions(GNode n){
            printThisAfter = "";
            printAtEnd = "] ";
            printContents(0, n.size(), n);
            visit(n);
        }

    /*    public void visit(GNode n){
            try{
                pen.write("_");
            }catch(IOException e) {
                System.out.println(e);
            }
            visit(n);
        }
    //
        public void visitPackageDeclaration(GNode n){
            printThisBefore = "namespace ";
            printThisAfter = "{ ";
            printAtEnd = "{ ";

            visit(n);
        }

        public void visitClassDeclaration(GNode n){
            System.out.println("Class dec visited");
            printThisAfter = " class ";
            visit(n);

        }

        public void visitModifier(GNode n){
            printAtEnd = " ";
            printContents(0, n.size(), n);
            visit(n);
        }

        public void visitQualifiedIdentifier(GNode n){
            printAtEnd = " ";
            printContents(0, n.size(), n);
            visit(n);
        }

        public void visitPrimitiveType(GNode n){
            printAtEnd = " ";
            printContents(0, n.size(), n);
            visit(n);
        }
    */
    /*public void visitType(GNode n){
        if(n.size() >= 2 && n.get(1).toString().contains("Dimensions")){
            printAtEnd = "";
        }
        else{
            printAtEnd = " ";
        }

        printContents(0, n.size(), n);
        visit(n);
    }*/

    ////////////////new stuff//////////////////////////////

    public void visitNullLiteral(GNode n) {
        try {
            pen.write("0");
        } catch(Exception e) {
            System.out.println(e);
        }
        visit(n);
    }

    public void visitVoidType(GNode n) {
        penPrint("void ");
        visit(n);
    }

    public void visitDeclarators(GNode n) {
        visit(n);
        penPrint("; \n");
    }

    public void visitExpressionStatement(GNode n) {
        visit(n);
        penPrint("; \n");
    }

    public void visitClassBody(GNode n) {
        visit(n);
        penPrint("}; ");
    }

    public void visitArguments(GNode n) {
        if(n.size() == 0) {
            penPrint("()");
        } else {

            visit(n);

        }


    }

    public void visitReturnStatement(GNode n) {
        penPrint(" return new " + returnType + " (");
        returnType = null;
        visit(n);
        penPrint("); \n");
    }

    public void visitFormalParameters(GNode n) {
        penPrint("(");
        visit(n);
        penPrint(") \n");
    }

    public void visitBlock(GNode n) {
        penPrint(" {");
        visit(n);
        penPrint("} \n");
    }

    public void visitMethodDeclaration(GNode n) {
        //String returnType = "";//((GNode)((GNode)n.get(2)).get(0)).get(0).toString();
        GNode search = (GNode)n.get(2);
        if(search.size() > 0) {
            while (search.get(0) instanceof Node) {
                search = (GNode)search.get(0);
            }
            returnType = search.get(0).toString();
        }
        if(returnType != null){
            if (returnType.startsWith("__")){
                penPrint("\n" + returnType.substring(2) + " ");
            }
            else{
                penPrint("\n" + returnType + " ");
            }
        }
        else{
            penPrint("\n");
        }
        penPrint("__" + ClassName + "::" +  n.get(3).toString() + "(");
        if (((GNode)n.get(4)).size() == 0) {
            penPrint(ClassName + "__this");
        } else {
            dispatch((GNode)n.get(4));
        }
        penPrint("){\n");
        for (int i = 5; i < n.size(); i++) {
            visit((GNode)n.get(i));
        }

    }

    public void visitPackageDeclaration(GNode n) {
        for(int i = 0; i < n.size(); i++) {
            printLater[0] = "}" + printLater[0];
        }
        for (Object o : ((GNode)n.get(1))) {
            if (o instanceof String) {
                pack += o.toString().substring(10, o.toString().length() - 4) + ".";
            }
        }
        visit(n);
    }

    public void visitClassDeclaration(GNode n) {
        if (! n.equals(mainClass)) {
            initializeClass(n);
            visit(n);
            ClassName = "";
        }
    }

    /*  public void visitCallExpression(GNode n){
          visit(n);
          penPrint("; ");
      }
    */
    //to avoid needing try/catch blocks everywhere
    public void penPrint(String words) {
        try {
            pen.write(words);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void initializeClass(GNode n) {
        ClassName = n.get(1).toString();
        //ClassName = ClassName.substring(6, ClassName.length() - 3);
        penPrint("__" + ClassName + "::__" + ClassName + "():__vptr(&__vtable){\n");
        Node constructor = NodeUtil.dfs(n, "ConstructorDeclaration");

        if (constructor != null){
            if(((GNode)constructor.get(5)).size() != 0){
                dispatch(constructor);
            }
        }
        penPrint("}\n\n__" + ClassName + "_VT __" + ClassName + "::__vtable;\n");
        penPrint("\nClass __" + ClassName + "::__class() {\n" +
                "static Class k = \n"
                + "new __Class(__rt::literal(\"class " + pack + ClassName + "\"), __Object::__class());\n"
                + "return k;}\n\n");

        //erase stuff so it doesn't print twice
        for (int i = 0; i < 5; i++) {
            n.set(i, null);
        }

        if (((GNode)n.get(5)).get(0) == constructor) {
            ((GNode)n.get(5)).set(0, null);
        }
    }


}


// put ; when exiting: Declarators, ExpressionStatement
// put } when exiting: Class bodies?,