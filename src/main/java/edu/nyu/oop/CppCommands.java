package edu.nyu.oop;

import edu.nyu.oop.util.SourceCppOutputCommand;
import edu.nyu.oop.util.SourceMainOutputCommand;
import edu.nyu.oop.util.SourceOutputCommand;
import xtc.tree.GNode;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Created by alex on 10/27/16.
 *
 * Restructured by Karl on 12/18/16.
 */


/*
    Generates a C++ implementation AST,
    outputs source output commands for printing source (output.cpp and main.cpp)
 */
public class CppCommands {
    private static final String CPP_PATH = "output/output.cpp";
    private static final String MAIN_PATH = "output/main.cpp";

    public static List<SourceOutputCommand> convertToCpp(List<GNode> javaRoots) {
        MakeCppAst visitor = new MakeCppAst();
        for (int i = 0; i < javaRoots.size(); i++) {
            visitor.visit(javaRoots.get(i));
        }
//        XtcTestUtils.prettyPrintAst(javaRoots.get(0));

        List<SourceOutputCommand> cmd = new ArrayList<SourceOutputCommand>();

        try {
            cmd.add(new SourceCppOutputCommand(new PrintOutputCpp(new FileWriter(CPP_PATH), new String[] {""}, visitor.mainClassNode, false), javaRoots));
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

        try {
            cmd.add(new SourceMainOutputCommand(new PrintMainCpp(new FileWriter(MAIN_PATH)), visitor.mainClassNode, visitor.packageNode));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return cmd;
    }
}
