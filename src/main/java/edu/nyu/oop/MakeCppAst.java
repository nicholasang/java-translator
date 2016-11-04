package edu.nyu.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import edu.nyu.oop.util.NodeUtil;
import java.lang.StringBuffer;

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
        //System.out.println(n.get(0));
        visit(n);
    }

    public void visitModifier(GNode n){
        switch((String) n.get(0)){
            case "final": n.set(0, "const"); break;

        }
        visit(n);
    }

    public void visitClassDeclaration(GNode n){
        //TESTING
        /*for (int i = 0; i < n.size(); i++){
            if (n.get(i) instanceof String){
                System.out.println(i + ": " + n.get(i));
            }
        }*/
        n.set(1, "class " + n.get(1) + "{");
        //n.get(1) = name of class
        visit(n);
    }

    //dealing with System.out.println
    public void visitCallExpression(GNode n){
        //n.get(2) = println / print
        if(n.size() > 2 && n.get(2) instanceof String){
            String arg = n.get(2).toString();
            if(arg.equals("print") || arg.equals("println")){
                String sb = findPrintItems("", (GNode)n.get(3));
                if (arg.equals("println")){
                    n.set(2, "cout << " + sb + "<< endl;");
                }
                else{
                    n.set(2, "cout << " + sb + ";");
                }
            }


        }
        visit(n);
    }

    public void visitPrimaryIdentifier(GNode n){
        if (n.get(0) instanceof String){
            switch (n.get(0).toString()){
                //and whatever else becomes null cascade here
                case "System": n.set(0, null); break;

            }

        }
        visit(n);
    }

    public void visitQualifiedIdentifier(GNode n){
        if(n.get(0) instanceof String){
            switch(n.get(0).toString()){
                case "String": n.set(0, "__String"); break;
                case "Object": n.set(0, "__Object"); break;
            }
        }
        visit(n);
    }

    public void visitSelectionExpression(GNode n){
        if (n.size() > 1 && n.get(1) instanceof String){
            switch (n.get(1).toString()){
                //and whatever else becomes null cascade here
                //sets primaryIdentifier with "system" and "out" to null
                case "out": n.set(1, null); n.set(0, null); break;
            }

        }
        visit(n);
    }

    public void visitType(GNode n){
        //look for a dimensions node in the children
        Node child = NodeUtil.dfs(n, "Dimensions");
        if (child != null){
            //if it is the second child
            if ((n.size() > 1) && (n.get(1) == child)){
                if (n.get(0) instanceof Node){
                    dispatch((Node) n.get(0));

                }
                //primitive type string
                String primType = ((Node)n.get(0)).get(0).toString();
                //dimensions string
                String dim = ((Node)n.get(1)).get(0).toString();
                ((Node)n.get(1)).set(0, null);
                //for each dimension "[", we need a "]"
                for (char c : dim.toCharArray()){
                    if (c == '['){
                        primType = primType + "[]";
                    }
                }
                //put that into the ast
                ((Node)n.get(0)).set(0, primType);

            }
        }
        else {visit(n);}
    }

    public void visitDeclarator(GNode n){
        String ln = "";
        if (NodeUtil.dfs(n, "ArrayInitializer") != null){
            for (Object o : n){
                if (o instanceof Node){
                    ln = findArrayItems(ln, (GNode)o);
                }
            }
            //cut off the last ", "
            if(ln.endsWith(", ")){
                ln = ln.substring(0, ln.length() - 2);
            }
            n.set(0, n.get(0).toString() + " = {" + ln + "}" );
        }

        else{
            n.set(0, n.get(0).toString() + " = ");
        }

        visit(n);
    }

    //for print functions, puts together the items to print
    public String findPrintItems(String line, GNode n){
        for (Object o : n){
            if (o instanceof String){
                if(o.toString().equals("+") && line.endsWith("\" ")){
                    System.out.println("found a plus");
                    line = line + "<< ";
                }
                else if (o.toString().startsWith("\"") && line.endsWith("+ ")){
                        line = line.substring(0, line.length() - 2) + "<< " + o.toString() + " ";
                }
                else{
                    line = line + o.toString() + " ";
                }
                n.set(n.indexOf(o), null);
                System.out.println("line: " + line);
            }
            else if (o instanceof Node) {
                line = findPrintItems(line, (GNode) o);
            }
        }
        return line;
    }

    //concatenating array items into a list with commas
    public String findArrayItems(String line, GNode n){
        for (Object o : n){
            if (o instanceof String){
                line = line + o.toString() + ", ";
                n.set(n.indexOf(o), null);
            }
            else if (o instanceof Node) {
                line = findArrayItems(line, (GNode) o);
            }
        }
        return line;
    }
}
