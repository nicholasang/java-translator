package edu.nyu.oop;

import xtc.tree.Node;
import xtc.tree.GNode;
import java.io.FileWriter;
/**
 * Created by Alex on 12/7/16.
 */
public class PrintClass extends xtc.tree.Visitor{

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

    public PrintClass(FileWriter inPen){
        pen = inPen;
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

    public void visitClassDeclaration(GNode n) {
        if (! n.equals(mainClass)) {
            initializeClass(n);
            visit(n);
            finishUpClass(n);
        }
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

    //to avoid needing try/catch blocks everywhere
    public void penPrint(String words) {
        try {
            pen.write(words);
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
