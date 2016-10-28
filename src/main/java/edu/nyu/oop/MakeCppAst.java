package edu.nyu.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * Created by alex on 10/26/16.
 */
public class MakeCppAst extends Visitor{
    public void visit(Node n){
        for (Object o: n){
            if (o instanceof Node) dispatch((Node) o);
        }
    }
    public void visitPrimitiveType(GNode n){

        if (n.get(0).equals("int")){
            n.set(0, "int32_t");
        }
        else if(n.get(0).equals("long")){
            n.set(0, "int64_t");
        }
        else if(n.get(0).equals("boolean")){
            n.set(0, "bool");
        }
        System.out.println(n.get(0));
        visit(n);
    }

    public void visitModifier(GNode n){
        switch((String) n.get(0)){
            case "final": n.set(0, "const"); break;

        }
    }


}
