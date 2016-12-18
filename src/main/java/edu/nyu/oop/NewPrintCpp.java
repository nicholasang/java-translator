package edu.nyu.oop;

import xtc.tree.Node;
import xtc.tree.GNode;
import java.io.FileWriter;
import java.util.ArrayList;

import edu.nyu.oop.util.NodeUtil;
/**
 * Created by alex on 10/27/16.
 */
public class NewPrintCpp extends xtc.tree.Visitor {

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

    ArrayList<String> variablesInScope = new ArrayList<String>();
    public boolean inMain = false;

    public NewPrintCpp(FileWriter outFileWrite, String[] printLater, GNode root) {
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

    public void visitPackageDeclaration(GNode n){
        GNode identifiers = null;
        if (n.get(1) instanceof GNode){
             identifiers = (GNode)n.get(1);

        }
        else{
            //no namespaces/packages?
        }

    }

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

    public void visitNamespace(GNode n) {
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
        if (inConstructor) {
            penPrint("this->");
        } else {
            penPrint("__this->");
        }
        visit(n);
    }

    public void visitPrimaryIdentifier(GNode n) {
        if (! variablesInScope.contains(n.getString(0)) && ! inMain) {
            if (inConstructor) {
                penPrint("this->");
            } else {
                penPrint("__this->");
            }
        }
        visit(n);
    }

    public void visitConstructorDeclaration(GNode n) {
        inConstructor = true;
        penPrint("__" + ClassName + "::__" + ClassName);
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

        inConstructor = false;
        variablesInScope.clear();
    }

    public void finishUpClass(GNode n) {
        if (! haveNoArgConstructor) {
            penPrint("__" + ClassName + "::__" + ClassName + "() :__vptr(&__vtable) {\n");
            penPrint("}");
        }

        haveNoArgConstructor = false;
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

        //erase stuff so it doesn't print twice
        for (int i = 0; i < 5; i++) {
            n.set(i, null);
        }

    }


}


// put ; when exiting: Declarators, ExpressionStatement
// put } when exiting: Class bodies?,