package edu.nyu.oop.util.CommandDesignWIP;

import edu.nyu.oop.CppAst;
import edu.nyu.oop.util.CppHVisitor;

public class SourceHeaderOutputCommand implements SourceOutputCommand {
    private CppHVisitor visitor; // receiver
    private CppAst header; // required arg(s)

    public SourceHeaderOutputCommand(CppHVisitor visitor, CppAst header) {
        this.visitor = visitor;
        this.header = header;
    }

    @Override
    public void outputSourceExecute() {
        this.visitor.visit(this.header);
    }
}
