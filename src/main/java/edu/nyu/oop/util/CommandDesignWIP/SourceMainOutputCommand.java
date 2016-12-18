package edu.nyu.oop.util.CommandDesignWIP;

import xtc.tree.GNode;

import edu.nyu.oop.PrintMainCpp;
// import edu.nyu.oop.util.SourceOutputCommand;

import java.io.FileWriter;
import java.io.IOException;

public class SourceMainOutputCommand implements SourceOutputCommand {
    private PrintMainCpp printer;
    private GNode mainClassDeclaration;
    private GNode packageDeclaration;

    private String outputPath;

    // some required arg(s)

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