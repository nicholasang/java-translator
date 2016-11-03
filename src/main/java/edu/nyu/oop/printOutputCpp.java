package edu.nyu.oop;

import xtc.tree.Node;
import xtc.tree.GNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Created by alex on 10/27/16.
 */
public class printOutputCpp extends xtc.tree.Visitor{


    FileWriter pen = null;
    String printThisAfter = " ";
    String printThisBefore = "";
    String printAtEnd = "";

    public printOutputCpp(FileWriter outFileWrite){
        pen = outFileWrite;
    }

    public void visit(Node n){
        for (Object o: n){
            if (o instanceof Node){ dispatch((Node) o);}
        }
    }

    /*
     * Print string contents from indices start to end
     */
    public void printContents(int start, int end, Node n){
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
*/
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

    public void visitNullLiteral(GNode n){
        try{
            pen.write("0");
        }
        catch(Exception e){
            System.out.println(e);
        }
        visit(n);
    }
}
