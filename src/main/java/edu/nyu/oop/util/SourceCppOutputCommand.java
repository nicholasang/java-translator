package edu.nyu.oop.util;

import edu.nyu.oop.PrintOutputCpp;
import xtc.tree.GNode;
import java.util.List;

public class SourceCppOutputCommand implements SourceOutputCommand {
    private PrintOutputCpp printer; // receiver
    private List<GNode> javaRoots; // required arg(s)


    public SourceCppOutputCommand(PrintOutputCpp printer, List<GNode> javaRoots) {
        this.printer = printer;
        this.javaRoots = javaRoots;
    }

    @Override
    public void outputSourceExecute() {
        printer.start();
        for (int i = 0; i < this.javaRoots.size(); i++) {
            printer.visit(this.javaRoots.get(i));

        }
        printer.finish();

    }
}
