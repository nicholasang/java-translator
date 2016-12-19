package edu.nyu.oop;

import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.Objects;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.nyu.oop.util.NodeUtil;
/**
 * Created by alex on 10/27/16.
 */
public class PrintOutputCpp extends xtc.tree.Visitor {

    boolean inConstructor;
    String superClassName = "";
    boolean haveNoArgConstructor = false;
    GNode mainClass;
    FileWriter pen = null;
    String printThisAfter = " ";
    String printThisBefore = "";
    String printAtEnd = "";
    String[] printLater = null;
    String pack = "";
    String ClassName = "";
    String returnType = null;
    ArrayList<Object> isAnArg = new ArrayList<Object>();

    ArrayList<String> variablesInScope = new ArrayList<String>();
    public boolean inMain = false;

    public PrintOutputCpp(FileWriter outFileWrite, String[] printLater, GNode mainClass, boolean inMain) {
        this.printLater = printLater;
        pen = outFileWrite;
        this.mainClass = mainClass;
        this.inMain = inMain;
    }

    public void start() {
        if (!this.inMain) {
            try {
                this.pen.write("#include \"output.h\"\n#include \"java_lang.h\"\nusing namespace std;\n#include <iostream>\nusing namespace java::lang;\n");

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void visit(Node n) {


        if (n != null) {
            for (Object o : n) {
                if (o instanceof Node) {
                    dispatch((Node) o);
                } else if (o instanceof String) {
                    try {
                        pen.write(o.toString() + " ");
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }
            }
        }
    }



////////////////new stuff//////////////////////////////

    public void visitStringLiteral(GNode n) {
        if (n.get(0) != null) {
            if (! n.getString(0).contains("String")) {
                penPrint("(new __String(" + n.getString(0) + "))");
                // System.out.println(n);
            }
            else {

                penPrint(n.getString(0));
            }
        }
    }

    public void visitSelectionExpression(GNode n) {
        // System.out.println(n);
        if (n.get(0) != null) {
            if (n.getNode(0).get(0) != null && !n.getNode(0).getString(0).contains("System")) {
                penPrint(n.getNode(0).getString(0) + "->" + n.getString(1));
            }
        }
        visit(n);
    }

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
        penPrint(" ");
    }

    public void visitArguments(GNode n) {
        if(n.size() == 0) {
            penPrint("()");
        } else {

            visit(n);

        }


    }

    public void visitReturnStatement(GNode n) {
        penPrint(" return ");
        returnType = null;
        // System.out.println(n);
        visit(n);
        penPrint(";\n");
    }

    public int visitFormalParameters(GNode n) {
        penPrint("");
        visit(n);
        penPrint(" ");
        return n.size();
    }

    public void visitFormalParameter(GNode n) {
        variablesInScope.add(n.getString(3));
        visit(n);
    }

    public void visitBlock(GNode n) {
        penPrint(" {\n");
        visit(n);
        penPrint("\n}\n");
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
        if(returnType != null) {
            if (returnType.startsWith("__")) {
                penPrint("\n" + returnType.substring(2) + " ");
            } else {
                penPrint("\n" + returnType + " ");
            }
        } else {
            penPrint("void ");
        }
        penPrint("__" + ClassName + "::" +  n.get(3).toString() + "(");
        if (((GNode)n.get(4)).size() == 0) {
            penPrint(ClassName + " __this");
        } else {
            penPrint(ClassName + " __this, ");
            dispatch((GNode)n.get(4));
        }
        penPrint(")\n");
        for (int i = 5; i < n.size(); i++) {
            if (n.get(i) instanceof Node) {
                dispatch(n.getNode(i));
            }
        }

        variablesInScope.clear();
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
            finishUpClass(n);
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

    public void visitThisExpression(GNode n) {
        //if (inConstructor) {
        //    penPrint("this->");
        //} else {
        if (n.get(0) instanceof Node) {
            if (n.getNode(0).getName().equals("PrimaryIdentifier")) {
                if (n.getNode(0).getString(0).contains("__this")) {

                }
                else {
                    // System.out.println(n);
                    penPrint("__this->");
                }
            }
            else {
                // System.out.println(n);
                penPrint("__this->");
            }
        }
        else {
            // System.out.println(n);
            penPrint("__this->");
        }

        //}
        visit(n);
    }
//***changed because our working version has __this in constructor
    public void visitPrimaryIdentifier(GNode n) {
        // System.out.println(n);
        // if (n.get(0) != null && n.getString(0).contains("__this")) {

        // }
        // else {

            if (inConstructor){
                if(!isAnArg.contains(n.get(0)) && !inMain) {
                    // penPrint("__this->");
                }

            }
            else if (! variablesInScope.contains(n.getString(0)) && ! inMain) {
                if (inConstructor) {
                    if(!isAnArg.contains(n.get(0))){
                        // penPrint("this->");
                    }
                } else {
                    // penPrint("__this->");
                }
            }
        // }
        visit(n);
    }

    public void visitConstructorDeclaration(GNode n) {
        inConstructor = true;

        //only print if has params (empty printed automatically at end)
        if (n.size() >= 6 && ((Node)n.get(3)).getName().compareTo("FormalParameters") == 0){
            GNode fps = (GNode) n.get(3); //FormalParameters
            if (fps.size() > 0){
                //not an empty constructor
                penPrint("void __" + ClassName + "::__init(" + ClassName + " __this");
                for (int i = 0; i < fps.size(); i++){
                    GNode fp = (GNode) fps.get(i); //FormalParameter
                    if(fp.size() >=4 && ((Node)fp.get(1)).getName().compareTo("Type") == 0){
                        penPrint(", " + ((Node)((Node)fp.get(1)).get(0)).get(0).toString() + " ");
                        penPrint(fp.get(3).toString());
                        isAnArg.add(fp.get(3));
                    }
                }
                penPrint(")");
                dispatch((Node) n.get(5)); //dispatch on block
            }
        }
        isAnArg.clear();
 /*       penPrint("__" + ClassName + "::__" + ClassName);
        if (n.get(3) instanceof Node) {
            penPrint("(");
            Object num = dispatch(n.getNode(3));
            if (num instanceof Integer) {
                int numParams = (Integer) num;
                if (numParams == 0) {
                    haveNoArgConstructor = true;
                }
            }
            penPrint(")");
        }

        penPrint(":__vptr(&__vtable) \n");

        if (n.get(5) instanceof Node) {
            dispatch(n.getNode(5));
        }
*/
        inConstructor = false;
        variablesInScope.clear();
    }

//------------------------new
    public void visitDeclarator(GNode n){
        //if this is an object we need to construct/init
        if(n.get(2) != null && ((GNode)n.get(2)).getName().compareTo("NewClassExpression") == 0){
            String name = n.get(0).toString();
            name = name.substring(0,name.length() - 3); //get rid of " = "
            GNode cn = (GNode)n.get(2);  //cn = child node "NewClassExpression"
            String type = ((Node)cn.get(2)).get(0).toString();
            //if this is for main.cpp
            if(this.inMain){
                penPrint(n.get(0).toString() + n.get(1).toString() + " (new " + type + "());\n");
                penPrint(type + "::__init(" + name);
                GNode args = (GNode) cn.get(3);
                if (args != null && args.size() > 0){
                    for (int i = 0; i < args.size(); i++){

                        if(args.get(i) instanceof Node) {
                            if (((Node) args.get(i)).get(0) != null) {
                                penPrint(", " + ((Node)args.get(i)).get(0).toString());

                            }
                        }
                    }
                }
                penPrint(")");
            }
            else{
                //in output.cpp

            }
        }
        else if (n.get(2) == null) {
            // System.out.println(n);
            penPrint(n.getString(0));
        }
        else if (((GNode)n.get(2)).getName().equals("StringLiteral")) {
            penPrint(n.getString(0) + " " + n.getNode(2).getString(0));
        }
        else{
            visit(n);
        }

    }

    public void visitFieldDeclaration(GNode n){
        if (this.inMain){
            visit(n);
        }
        else {
            // System.out.println(n);
            visit(n);
        }
        //do not print this in output.cpp
    }



//------------------------end new
public void finishUpClass(GNode n) {

 //   if (! haveNoArgConstructor) {
        penPrint("__" + ClassName + "::__" + ClassName + "() :__vptr(&__vtable) {\n");
        penPrint("}");
 //   }

    // array specializations
    penPrint("}\n}\n\nnamespace __rt\n{"); // closes the other namespaces
    if (Objects.equals(superClassName, "")) {
        penPrint("template<>\n"
                + "java::lang::Class Array<inputs::" + pack.substring(7,14) + "::" + ClassName + ">::__class()"
                + "{\n"
                + "static java::lang::Class k =\n"
                + "new java::lang::__Class(literal(\"[Linputs." + pack.substring(7) + ClassName + ";\"),"
                + "\njava::lang::__Object::__class(),\n"
                + "inputs::" + pack.substring(7,14) + "::__" + ClassName + "::__class());\n" +
                "return k;\n}\n}" + "\n\nnamespace inputs {\nnamespace " + pack.substring(7,14) + "{");
    }
    else {
        penPrint("template<>\n"
                + "java::lang::Class Array<inputs::" + pack.substring(7, 14) + "::" + ClassName + ">::__class()"
                + "{\n"
                + "static java::lang::Class k =\n"
                + "new java::lang::__Class(literal(\"[Linputs." + pack.substring(7) + ClassName + ";\"),"
                + "\ninputs::" + pack.substring(7, 14) + "::__" + superClassName + "::__class(),\n"
                + "inputs::" + pack.substring(7, 14) + "::__" + ClassName + "::__class());\n" +
                "return k;");
    }
 //   haveNoArgConstructor = false;
    superClassName = "";
    ClassName = "";

}

    public void initializeClass(GNode n) {

        ClassName = n.get(1).toString();

        if (n.get(3) instanceof Node) {
            superClassName = n.getNode(3).getNode(0).getNode(0).getString(0);
        }

        penPrint("\n\n__" + ClassName + "_VT __" + ClassName + "::__vtable;\n");
        penPrint("\nClass __" + ClassName + "::__class() {\n" +
                 "static Class k = \n"
                 + "new __Class(__rt::literal(\"class " + pack + ClassName + "\"), __Object::__class());\n"
                 + "return k;}\n\n");
        penPrint("void __" + ClassName + "::__init(" + ClassName+" __this){\n}\n\n");

        //erase stuff so it doesn't print twice
        for (int i = 0; i < 5; i++) {
            n.set(i, null);
        }

    }

    public void finish() {

        if (this.inMain) return;
        try {
            this.pen.write(this.printLater[0]);
            // end bracket of the namespaces printed above
            // put array code here
            this.pen.flush();
            this.pen.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}


// put ; when exiting: Declarators, ExpressionStatement
// put } when exiting: Class bodies?,