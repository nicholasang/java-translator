package edu.nyu.oop;

import edu.nyu.oop.util.MethodScope;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import edu.nyu.oop.util.NodeUtil;
import java.lang.StringBuffer;
import edu.nyu.oop.util.SymbolTable;
import edu.nyu.oop.util.SymbolTable.*;

/**
 * Created by alex on 10/26/16.
 */
public class MakeCppAst extends Visitor {

    public SymbolTable table = SymbolTable.table;
    public MethodOverloadResolver resolver = new MethodOverloadResolver(table);

    public boolean mainMethodDeclarationFound = false;
    public boolean inMain = false;
    public GNode mainClassNode;
    public GNode packageNode;

    public void visit(Node n) {
        for (Object o: n) {
            if (o instanceof Node) {
                table.enterScopeForNode((GNode) o);
                dispatch((Node) o);
                table.exitScopeForNode((GNode) o);
            }
        }
    }
    public void visitPrimitiveType(GNode n) {

        if (n.get(0).equals("int")) {
            n.set(0, "int32_t");
        } else if(n.get(0).equals("long")) {
            n.set(0, "int64_t");
        } else if(n.get(0).equals("boolean")) {
            n.set(0, "bool");
        }
        //System.out.println(n.get(0));
        visit(n);
    }

    public void visitModifier(GNode n) {
        switch((String) n.get(0)) {
        case "final":
            n.set(0, "const");
            break;
        case "static":
            ;
        case "public":
            n.set(0, null);
            break;
        default:
            n.set(0, null);
        }
        visit(n);
    }


    public void visitPackageDeclaration(GNode n) {
        for (int i = 0; i < ((Node)n.get(1)).size(); i++) {
            if (((Node)n.get(1)).get(i) instanceof String) {
                ((Node)n.get(1)).set(i,"namespace " + ((Node)n.get(1)).get(i).toString() + " { \n");
            }
        }

        packageNode = n;

        visit(n);
    }

    public void visitNewClassExpression(GNode n) {
        n.set(0, "new ");

        if (n.size() >= 3) {
            Node newClassName = n.getNode(2);
            newClassName.set(0, "__" + newClassName.getString(0));
        }

        visit(n);
    }

    public void visitClassDeclaration(GNode n) {
        n.set(0,null);
        //n.get(1) = name of class

        visit(n);

        if (mainClassNode == null && mainMethodDeclarationFound) {
            mainClassNode = n;
        }
    }

    public void visitCallExpression(GNode n) {
        //0 = calling item (if there is one) that may be nested
        //1 = ???
        //2 = name of function
        //3 = arguments

        // visit(n);

        // XtcTestUtils.prettyPrintAst(n);

        // if it's a System.out.println() call
        if (n.get(0) != null && n.getNode(0).getName().equals("SelectionExpression")) {
            dispatch(n.getNode(0));

            String caller = "";

            if(((GNode)n.get(0)).get(0) instanceof String) {
                caller = ((GNode)n.get(0)).get(0).toString();

                if(caller != null) {
                    ((GNode)n.get(0)).set(0, caller + "->__vptr->");
                }
            }
            if((n.get(3) instanceof Node)) {
                if (((GNode)n.get(3)).size() == 0) {
                    n.set(2, n.get(2).toString() + "(" + caller + ")");
                    n.set(3, null);
                } else if (! n.getString(2).equals("print") && ! n.getString(2).equals("println")) {
                    n.set(2, n.get(2).toString() + "(" + caller + ", ");
                    Node args = n.getNode(3);
                    Node lastArg = args.getNode(args.size()-1);
                    lastArg.set(0, lastArg.getString(0) + ")");
                }
            }

            //if n.get(2) = println / print
            if(n.size() > 2 && n.get(2) instanceof String) {
                String arg = n.get(2).toString();
                if(arg.equals("print") || arg.equals("println")) {
                    String sb = findPrintItems("", (GNode)n.get(3));
                    if (sb.charAt(sb.length()-1) == ')') {
                        // sb += "->data";
                    }
                    if (arg.equals("println")) {
                        n.set(2, "std::cout << " + sb + "<< endl");
                    } else {
                        n.set(2, "std::cout << " + sb);
                    }
    //                return;
                }

            }
        }
        else {
            MethodScope method = resolver.resolve(n);
            String name = method.getMangledName();
            n.set(2, name);

            String caller = "";

            if(((GNode)n.get(0)).get(0) instanceof String) {
                caller = ((GNode)n.get(0)).get(0).toString();

                if(caller != null && !method.isStatic && !method.isPrivate) {
                    ((GNode)n.get(0)).set(0, caller + "->__vptr->");
                }
                else {
                    ((GNode)n.get(0)).set(0, caller + "->");
                }
            }
            if((n.get(3) instanceof Node)) {
                if (((GNode)n.get(3)).size() == 0) {
                    n.set(2, n.get(2).toString() + "(" + caller + ")");
                    n.set(3, null);
                } else if (! n.getString(2).equals("print") && ! n.getString(2).equals("println")) {
                    n.set(2, n.get(2).toString() + "(" + caller + ", ");
                    Node args = n.getNode(3);
                    Node lastArg = args.getNode(args.size()-1);
                    lastArg.set(0, lastArg.getString(0) + ")");
                }
            }

        }

        // XtcTestUtils.prettyPrintAst(n);

        // visit(n);
    }

    public void visitPrimaryIdentifier(GNode n) {
        if (n.get(0) instanceof String) {
            // switch (n.get(0).toString()) {
            // //and whatever else becomes null cascade here
            // case "System":
            //     n.set(0, null);
            //     break;
            // }
            if ("System".equals(n.get(0).toString())) {
                n.set(0, null);
            }
            else {
                if (!inMain && ! table.symbolExistsBelowClassScope(n.getString(0))) {
                    n.set(0, "__this->" + n.getString(0));
                }
            }

        }
        visit(n);
    }

    public void visitQualifiedIdentifier(GNode n) {
        if(n.get(0) instanceof String) {
//            switch(n.get(0).toString()) {
//            case "String":
//                n.set(0, "__String");
//                break;
//            case "Object":
//                n.set(0, "__Object");
//                break;
//
//            }

        }
        visit(n);
    }

    public void visitSelectionExpression(GNode n) {
        if (n.size() > 1 && n.get(1) instanceof String) {
            switch (n.get(1).toString()) {
            //and whatever else becomes null cascade here
            //sets primaryIdentifier with "system" and "out" to null
            case "out":
                n.set(1, null);
                n.set(0, null);
                break;
            }

        }
        visit(n);
    }

    public void visitType(GNode n) {
        //look for a dimensions node in the children
        Node child = NodeUtil.dfs(n, "Dimensions");
        if (child != null) {
            //if it is the second child
            if ((n.size() > 1) && (n.get(1) == child)) {
                if (n.get(0) instanceof Node) {
                    dispatch((Node) n.get(0));

                }
                //primitive type string
                String primType = ((Node)n.get(0)).get(0).toString();
                //dimensions string
                String dim = ((Node)n.get(1)).get(0).toString();
                ((Node)n.get(1)).set(0, null);
                //for each dimension "[", we need a "]"
                for (char c : dim.toCharArray()) {
                    if (c == '[') {
                        primType = primType + "[]";
                    }
                }
                //put that into the ast
                ((Node)n.get(0)).set(0, primType);

            }
        } else {
            visit(n);
        }
    }

    public void visitFieldDeclaration(GNode n) {
        String type = n.getNode(1).getNode(0).getString(0);

        Node declarator = NodeUtil.dfs(n, "Declarator");
        declarator.set(1, "(" + type + ")");

        visit(n);
    }

    public void visitDeclarator(GNode n) {
        String ln = "";
        if (NodeUtil.dfs(n, "ArrayInitializer") != null) {
            for (Object o : n) {
                if (o instanceof Node) {
                    ln = findArrayItems(ln, (GNode)o);
                }
            }
            //cut off the last ", "
            if(ln.endsWith(", ")) {
                ln = ln.substring(0, ln.length() - 2);
            }
            n.set(0, n.get(0).toString() + " = {" + ln + "}" );
        }

        else {
            if(n.get(2) != null) {
                n.set(0, n.get(0).toString() + " = ");
            }
        }

        visit(n);
    }

    public void visitArguments(GNode n) {
        if (n.size() > 0) {
            if (((GNode)n.get(0)) instanceof Node) {
                visit(n);
            }
            if(((GNode)n.get(0)).size() == 1) {
                if(((GNode) n.get(0)).get(0) != null) {
                    ((GNode) n.get(0)).set(0, "(" + ((GNode) n.get(0)).get(0).toString());
                }
            }
            for (int i = 1; i < n.size()-1; i++) {
                if (n.get(i) instanceof String) {
                    if(((GNode)n.get(i)).size() == 1) {
                        ((GNode)n.get(i)).set(0, ", " + ((GNode)n.get(i)).get(i).toString());
                    }
                }
            }
            // System.out.println("last arg: " + ((GNode)n.get(n.size()-1)).get(0).toString());
            if(((GNode)n.get(n.size()-1)).size() == 1) {
                if(((GNode)n.get(n.size()-1)).get(0) != null) {
                    ((GNode)n.get(n.size()-1)).set(0, ((GNode)n.get(n.size()-1)).get(0).toString() + ")");
                }
            }
        }
        //if empty
    }

    public void visitMethodDeclaration(GNode n) {
        if (n.size() >= 4) {
            if (n.getString(3).equals("main")) {
                mainMethodDeclarationFound = true;
                inMain = true;
                // XtcTestUtils.prettyPrintAst(n);
            }
            else {
                n.set(3, table.getMangledName());
                // XtcTestUtils.prettyPrintAst(n);
            }
        }

        visit(n);
        inMain = false;
    }

    public void visitStringLiteral(GNode n) {
        if (n.get(0) != null) {
            String str = n.getString(0);
            n.set(0, "(new __String(" + str + "))");
        }
        visit(n);
    }


//////////////////////utility functions//////////////////////

    //for print functions, puts together the items to print

    public String findPrintItems(String line, GNode n) {

        for (Object o : n) {


            if (o instanceof String) {
                if(o.toString().equals("+") && line.endsWith("\" ")) {
                    line = line + " << ";
                } else if (o.toString().startsWith("\"") && line.endsWith("+ ")) {
                    line = line.substring(0, line.length() - 2) + " << " + o.toString() + " ";
                } else if (o.toString().equals("+")) {
                    line = line + " + ";
                } else {
                    line = line + o.toString();
                }
                n.set(n.indexOf(o), null);
            }
            if (o instanceof Node) {
//                System.out.println(o.toString());
                /*if (NodeUtil.dfs((GNode)o, "Arguments") == o){
                    System.out.println("Found args: " + o.toString());
                }
                /*if(!((GNode)o.hasVariable())){
                    int index = n.indexOf(o);
                    (GNode)o = GNode.ensureVariable((GNode)o);
                    method.set(index, (GNode)o);
                }*/
                // System.out.println(o);
                dispatch((Node)o); //this gets called twice?
                // System.out.println(o);
                line = findPrintItems(line, (GNode) o);
            }
        }
        return line;
    }

    //concatenating array items into a list with commas
    public String findArrayItems(String line, GNode n) {
        for (Object o : n) {
            if (o instanceof String) {
                line = line + o.toString() + ", ";
                n.set(n.indexOf(o), null);
            } else if (o instanceof Node) {
                line = findArrayItems(line, (GNode) o);
            }
        }
        return line.substring(0, line.length()-2);
    }



}
