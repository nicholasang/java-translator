package edu.nyu.oop;

import edu.nyu.oop.util.ClassHierarchyTree;
import xtc.tree.GNode;
import java.util.*;

/*
 * Phase Two
 * encapsulates relationships between classes in different ASTs, parts of different ASTs,
 * and other classes in a ClassRef object, contains references to GNodes in a respective Java AST and C++ header AST
 */
public class ClassRef {
    private String name;
    private GNode jAst;
    private GNode jClassDeclaration;

    private GNode cppAst;
    private GNode cppAstLinkPoint;
    private CppAst cppHAst;
    private GNode cppHAstLinkPoint;

    private GNode cppHStructBody;
    private GNode cppHVtableBody;

    private ClassRef parentClassRef;
    private LayoutSchematic schematic;

    private static ClassHierarchyTree hierarchy;

    //constructors
    private ClassRef() {
    }

    public ClassRef(String name) {
        this.schematic = new LayoutSchematic(name);
        this.name = name;
    }

    // getters and setters
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public GNode getJAst() {
        return this.jAst;
    }
    public void setJAst(GNode jAst) {
        this.jAst = jAst;
    }

    public GNode getJClassDeclaration() {
        return this.jClassDeclaration;
    }
    public void setJClassDeclaration(GNode jClassBody) {
        this.jClassDeclaration = jClassBody;
    }

    //TODO: (.cpp-relevant, for later)
    public GNode getCppAst() {
        return this.cppAst;
    }
    public void setCppAst(GNode cppAst) {
        this.cppAst = cppAst;
    }
    public GNode getCppAstLinkPoint() {
        return this.cppAstLinkPoint;
    }
    public void setCppAstLinkPoint(GNode cppAstLinkPoint) {
        this.cppAstLinkPoint = cppAstLinkPoint;
    }

    //for CppAst header
    public CppAst getCppHAst() {
        return this.cppHAst;
    }
    public void setCppHAst(CppAst cppHAst) {
        this.cppHAst = cppHAst;
    }
    public GNode getCppHAstLinkPoint() {
        return this.cppHAstLinkPoint;
    }
    public void setCppHAstLinkPoint(GNode cppHAstLinkPoint) {
        this.cppHAstLinkPoint = cppHAstLinkPoint;
    }

    //direct links to the struct bodies
    public GNode getCppHStructBody() {
        return this.cppHStructBody;
    }
    public void setCppHStructBody(GNode cppHStructBody) {
        this.cppHStructBody = cppHStructBody;
    }
    //
    public GNode getCppHVtableBody() {
        return this.cppHVtableBody;
    }
    public void setCppHVtableBody(GNode cppHVtableBody) {
        this.cppHVtableBody = cppHVtableBody;
    }

    //parent ClassRef in inheritance hierarchy
    public ClassRef getParentClassRef() {
        return this.parentClassRef;
    }
    public void setParentClassRef(ClassRef parentClassRef) {
        this.parentClassRef = parentClassRef;
    }

    public LayoutSchematic getLayoutSchematic() {
        return this.schematic;
    }
    public void setLayoutSchematic(LayoutSchematic schematic) {
        this.schematic = schematic;
    }

    public static ClassHierarchyTree getHierarchy() {
        return hierarchy;
    }
    public static void setHierarchy(ClassHierarchyTree hierarchy) {
        ClassRef.hierarchy = hierarchy;
    }


    @Override
    public String toString() {
        return this.name;
    }

}
