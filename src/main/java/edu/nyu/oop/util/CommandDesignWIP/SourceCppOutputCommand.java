package edu.nyu.oop.util.CommandDesignWIP;

import edu.nyu.oop.PrintMainCpp;
import edu.nyu.oop.PrintOutputCpp;
import xtc.tree.GNode;
import xtc.tree.Visitor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SourceCppOutputCommand implements SourceOutputCommand {
    private PrintOutputCpp printer;
    private List<GNode> javaRoots; // required arg(s)


    public SourceCppOutputCommand(PrintOutputCpp printer, List<GNode> javaRoots) {
        this.printer = printer;
        this.javaRoots = javaRoots;
    }

    @Override
    public void outputSourceExecute() {
        // XtcTestUtils.prettyPrintAst(javaRoots.get(0));

        printer.start();

        for (int i = 0; i < this.javaRoots.size(); i++) {
            printer.visit(this.javaRoots.get(i));

        }
        printer.finish(); // for after-visiting printing

    }
}
