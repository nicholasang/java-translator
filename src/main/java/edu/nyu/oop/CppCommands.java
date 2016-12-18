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
 */


// ! ! !
/*
    have convert to Cpp return a list of SourceOutputCommands, one for cpp, one for main.
        please see SourceMainOutputCommand and SourceCppOutputCommand for the "mock-up" of what arguments / code should be to enable the mock-up to be correct
        idea is to pass a fully initialized PrintMainCpp or PrintOutputCpp to the Command constructor. Try to keep the one-element String array and FileWriter *inside* the
        printer. I tried to do this and got close, but things broke because PrintMainCpp creates a PrintOutputCpp, so my changes to one affected the other.
        You know this code better than I do. Is there a way to get the files in CommandDesignWIP working with a revision of your classes?
        Put printCpp in the print method that does everythng in PrintOutputCpp and the same for printMain in PintMainCpp
 */

public class CppCommands {
    private static final String CPP_PATH = "out/out.cpp";
    private static final String MAIN_PATH = "out/main.cpp";

    public static List<SourceOutputCommand> convertToCpp(List<GNode> javaRoots) {
//        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        MakeCppAst visitor = new MakeCppAst();
        for (int i = 0; i < javaRoots.size(); i++) {
            visitor.visit(javaRoots.get(i));
        }

        List<SourceOutputCommand> cmd = new ArrayList<SourceOutputCommand>();

        /*
        try {
            cmd.add(new SourceCppOutputCommand(new PrintOutputCpp(new FileWriter(CPP_PATH), new String[]{""}, visitor.mainClassNode, false), javaRoots));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            cmd.add(new SourceMainOutputCommand(new PrintMainCpp(new FileWriter(MAIN_PATH)), visitor.mainClassNode, visitor.packageNode));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        */

        return cmd;
    }
}
