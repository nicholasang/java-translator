package edu.nyu.oop.util;

import xtc.tree.GNode;
import edu.nyu.oop.PrintMainCpp;

public class SourceMainOutputCommand implements SourceOutputCommand {
    private PrintMainCpp printer; // receiver
    private GNode mainClassDeclaration; // required arg(s)
    private GNode packageDeclaration;

    public SourceMainOutputCommand(PrintMainCpp printer, GNode mainClassDeclaration, GNode packageDeclaration) {
        this.printer = printer;
        this.mainClassDeclaration = mainClassDeclaration;
        this.packageDeclaration = packageDeclaration;
    }


    @Override
    public void outputSourceExecute() {
        this.printer.print(this.mainClassDeclaration, this.packageDeclaration);
    }
}