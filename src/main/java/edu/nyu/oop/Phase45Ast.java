package edu.nyu.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import java.util.ArrayList;

/**
 * Created by Alex on 12/7/16.
 */
public class Phase45Ast extends Visitor{
    GNode root = GNode.create("root", null);

    public Phase45Ast(Node inNode){
        root.set(0, rootVisit(inNode));
    }

    public ArrayList<Object> rootVisit(Node n) {
        ArrayList<Object> rootChildren = new ArrayList<Object>();
        for (Object o: n) {
            if (o instanceof Node) rootChildren.add(dispatch((Node) o));
        }
        return rootChildren;
    }

    public void visit(Node n) {
        for (Object o: n) {
            if (o instanceof Node) dispatch((Node) o);
        }
    }

    //create GNode.Fixed# for # of variables up to 8

    //make class GNode
    public GNode visitClassDeclaration(GNode n){
        GNode Class = GNode.create("Class");
        for (int i = 0; i < n.size(); i++){
            Class.add(dispatch((Node)n.get(i)));
        }
        return Class;
 /*       //fill with specifics of node's children
        ArrayList<GNode> methods = new ArrayList<GNode>();
        //TODO actually assign correct indices to spots
        //TODO OR fill with nulls and place after?
        GNode Class = GNode.create("Class", dispatch((Node)n.get(0)), methods);
        //TODO visit on any methods to add to list
        return Class;
 */   }
        //class signature
        //arraylist of globals
        //arraylist of constructors
            //if empty, we will print a default
        //arraylist of methods
    //make method GNode
        public GNode visitMethodDeclaration(GNode n) {
            GNode Method = GNode.create("Method");
            for (int i = 0; i < n.size(); i++) {
                Method.add(dispatch((Node) n.get(i)));
            }
            return Method;
        }
        //method signature
        //arraylist of sequence of commands
        //command item = a line basically
        //commands for: variable declarations,
                    //operations
        public GNode visitPackageDeclaration(GNode n) {
            GNode Package = GNode.create("Package");
            for (int i = 0; i < n.size(); i++) {
                Package.add(dispatch((Node) n.get(i)));
            }
            return Package;
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
}
