package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import java.util.ArrayList;

/**
 * Created by Alex on 12/7/16.
 */
public class Phase45Ast extends Visitor{

    public boolean mainMethodDeclarationFound = false;
    public GNode mainClassNode;
    public GNode packageNode;

    public GNode Root = GNode.create("Root");

    public boolean isArgument = false;

    public Phase45Ast(Node inNode){
        PackageNode(Root,inNode);
    }

    public GNode visit(Node n) {
   //     if (n.size() == 1 && n.get(0) instanceof Node){
  //          return (GNode)dispatch((Node) n.get(0));
  //      }
//        else{
            GNode blank = GNode.create(n.getName());
            PackageNode(blank,n);
            return blank;
     //   }
    }

    //create GNode.Fixed# for # of variables up to 8

/*    //make class GNode
    public GNode visitClassDeclaration(GNode n){
        GNode Class = GNode.create("ClassDeclaration");
        PackageNode(Class,n);
        if (mainClassNode == null && mainMethodDeclarationFound) {
            mainClassNode = Class;
        }
        return Class;
    }
        //class signature
        //arraylist of globals
        //arraylist of constructors
            //if empty, we will print a default
        //arraylist of methods
    //make method GNode
        public GNode visitMethodDeclaration(GNode n) {
            GNode Method = GNode.create("MethodDeclaration");
            if (n.size() >= 4) {
                if (n.getString(3).equals("main")) {
                    mainMethodDeclarationFound = true;
                }
            }

            PackageNode(Method,n);
            return Method;
        }
        //method signature
        //arraylist of sequence of commands
        //command item = a line basically
        //commands for: variable declarations,
                    //operations
        public GNode visitPackageDeclaration(GNode n) {
            GNode Namespace = GNode.create("Namespace");
            for (int i = 0; i < ((Node)n.get(1)).size(); i++) {
                if (((Node)n.get(1)).get(i) instanceof String) {
                    ((Node)n.get(1)).set(i,"namespace " + ((Node)n.get(1)).get(i).toString() + " { \n");
                }
            }
            PackageNode(Namespace,n);
            packageNode = Namespace;
            return Namespace;
        }

    //special methods
        //ex: print
        // main
    //variable declarations
        //variable name
        //initial val (if none, give default of null or 0?)
    //object declarations
        //object name
        //args
    //operations
        //function calls
        //casting
        //set =
        //if statements
        //loops
    //loop
        //for
        //while
    //for loop
        //index variable
        //initial set
        //up to
        //increment
        //content
    //while loop
        //conditions
        //content
    //conditionals
        //condition --> variables + signs + && or ||
        //content

    public GNode visitPrimitiveType(GNode n) {
        if (n.get(0).equals("int")) {
            n.set(0, "int32_t");
        } else if(n.get(0).equals("long")) {
            n.set(0, "int64_t");
        } else if(n.get(0).equals("boolean")) {
            n.set(0, "bool");
        }

        GNode Primitive = GNode.create("Primitive");
        PackageNode(Primitive,n);
        return Primitive;
    }

    public GNode visitModifier(GNode n) {
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

        GNode Modifier = GNode.create("Modifier");
        PackageNode(Modifier,n);
        return Modifier;
    }



    public GNode visitNewClassExpression(GNode n) {
        n.set(0, "new ");

        if (n.size() >= 3) {
            Node newClassName = n.getNode(2);
            newClassName.set(0, "__" + newClassName.getString(0));
        }

        GNode NewClassExpression = GNode.create("NewClassExpression");
        PackageNode(NewClassExpression,n);
        return NewClassExpression;
    }


    public GNode visitCallExpression(GNode n) {
        //0 = calling item (if there is one) that may be nested
        //1 = ???
        //2 = name of function
        //3 = arguments

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
                    sb += "->data";
                }
                if (arg.equals("println")) {
                    n.set(2, "std::cout << " + sb + "<< endl;");
                } else {
                    n.set(2, "std::cout << " + sb);
                }
//                return;
            }

        }
        GNode CallExpression = GNode.create("CallExpression");
        PackageNode(CallExpression,n);
        return CallExpression;
    }

    public GNode visitPrimaryIdentifier(GNode n) {
        if (n.get(0) instanceof String) {
            switch (n.get(0).toString()) {
                //and whatever else becomes null cascade here
                case "System":
                    n.set(0, null);
                    break;
            }

        }
        GNode PrimaryIdentifier = GNode.create("PrimaryIdentifier");
        PackageNode(PrimaryIdentifier,n);
        return PrimaryIdentifier;
    }

    public GNode visitQualifiedIdentifier(GNode n) {
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
        GNode QualifiedIdentifier = GNode.create("QualifiedIdentifier");
        PackageNode(QualifiedIdentifier,n);
        return QualifiedIdentifier;
    }

    public GNode visitSelectionExpression(GNode n) {
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
        GNode SelectionExpression = GNode.create("SelectionExpression");
        PackageNode(SelectionExpression,n);
        return SelectionExpression;
    }

    public GNode visitType(GNode n) {
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
        }
        GNode Type = GNode.create("Type");
        PackageNode(Type,n);
        return Type;

    }

    public GNode visitFieldDeclaration(GNode n) {
        String type = n.getNode(1).getNode(0).getString(0);

        Node declarator = NodeUtil.dfs(n, "Declarator");
        declarator.set(1, "(" + type + ")");

        GNode FieldDeclaration = GNode.create("FieldDeclaration");
        PackageNode(FieldDeclaration,n);
        return FieldDeclaration;
    }

    public GNode visitDeclarator(GNode n) {
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

        GNode Declarator = GNode.create("Declarator");
        PackageNode(Declarator,n);
        return Declarator;
    }

    public GNode visitArguments(GNode n) {
        isArgument = true;
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

        GNode Arguments = GNode.create("Arguments");
        PackageNode(Arguments,n);
        isArgument = false;
        return Arguments;
        //if empty

    }

    public GNode visitStringLiteral(GNode n) {
        if (n.get(0) != null) {
            String str = n.getString(0);
            if(!str.startsWith("(new") && !isArgument){
                n.set(0, "(new __String(" + str + "))");
            }
            else if (isArgument && str.startsWith("(new") && !str.endsWith("data")){
                n.set(0, str + "->data");
            }

        }
        GNode StringLiteral = GNode.create("StringLiteral");
        PackageNode(StringLiteral,n);
        return StringLiteral;
    }

    public GNode visitDimensions(GNode n) {
        GNode Dimensions = GNode.create("Dimensions");
        PackageNode(Dimensions,n);
        return Dimensions;
    }

    public GNode visitFormalParameter(GNode n) {
        GNode FormalParameter = GNode.create("FormalParameter");
        PackageNode(FormalParameter,n);
        return FormalParameter;
    }

    public GNode visitBlock(GNode n) {
        GNode Block = GNode.create("Block");
        PackageNode(Block,n);
        return Block;
    }

    public GNode visitModifiers(GNode n) {
        GNode Modifiers = GNode.create("Modifiers");
        PackageNode(Modifiers,n);
        return Modifiers;
   }
*/
 /*   public GNode visit(GNode n) {
        GNode  = GNode.create("");
        PackageNode(,n);
        return ;
    }
*/

//////////////////////utility functions//////////////////////

    //for print functions, puts together the items to print

    public String findPrintItems(String line, GNode n) {

        for (Object o : n) {


            if (o instanceof String) {
                if(o.toString().equals("+") && (line.endsWith("\" ") || line.endsWith("\"))"))) {
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
                dispatch((Node)o); //this gets called twice?
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

    private void PackageNode(GNode gn, Node n){
        for (int i = 0; i < n.size(); i++) {
            if (n.get(i) instanceof Node && n.get(i) != null){
                Object retNode = dispatch((Node) n.get(i));
                //if (retNode != null){
                    gn.add(retNode);
                //}
            }
            //else if (n.get(i) != null){
            else{
                gn.add(n.get(i));
            }
        }
    }
}
