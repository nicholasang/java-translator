package edu.nyu.oop;

import xtc.tree.GNode;
//import xtc.tree.Node;
import xtc.tree.Visitor;
import java.lang.Comparable;

/**
 * Created by stud on 10/26/16.
 */
public class MakeCppAst extends Visitor{

    public void visitPrimitiveType(GNode n){

        if (n.get(0).equals("int")){
            n.set(0, "int32_t");
            System.out.println(n.get(0));
        }
    }
}
