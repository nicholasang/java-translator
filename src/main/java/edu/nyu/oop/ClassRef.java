package edu.nyu.oop;

import xtc.tree.GNode;

//instead of using several hash-maps, encapulate relationships between classes in
//different ASTs, parts of different ASTs, and other classes in a ClassRef object
public class ClassRef {
    private String name;
    private GNode jAst;
    private GNode jClassBody;

    //will probably have a CppProgramAst too
    private GNode cppAst;
    private CppHeaderAst cppHAst;
    private GNode cppHStructBody;
    private GNode cppHVtableBody;

    private ClassRef parentClassRef;
    private LayoutSchematic tables;

    public ClassRef() {
        this.tables = new LayoutSchematic();
    }

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

    public GNode getJClassBody() {
        return this.jClassBody;
    }
    public void setJClassBody(GNode jClassBody) {
        this.jClassBody = jClassBody;
    }

    public GNode getCppAst() {
        return this.cppAst;
    }
    public void setCppAst(GNode cppAst) {
        this.cppAst = cppAst;
    }

    public CppHeaderAst getCppHAstObj() {
        return this.cppHAst;
    }
    public void setCppHAstObj(CppHeaderAst cppHAst) {
        this.cppHAst = cppHAst;
    }

    public GNode getCppHStructBody() {
        return this.cppHStructBody;
    }
    public void setCppHStructBody(GNode cppHStructBody) {
        this.cppHStructBody = cppHStructBody;
    }

    public GNode getCppHVtableBodyBody() {
        return this.cppHVtableBody;
    }
    public void setCppHVtableBody(GNode cppHVtableBody) {
        this.cppHVtableBody = cppHVtableBody;
    }

    public ClassRef getParentClassRef() {
        return this.parentClassRef;
    }
    public void setParentClassRef(ClassRef parentClassRef) {
        this.parentClassRef = parentClassRef;
    }

    public LayoutSchematic getLayoutSchematic() {
        return this.tables;
    }
    public void setLayoutSchematic(LayoutSchematic tables) {
        this.tables = tables;
    }


    private static class LayoutSchematic {
        // TODO: Methods and Fields, public/package private and private/static
    }



}
