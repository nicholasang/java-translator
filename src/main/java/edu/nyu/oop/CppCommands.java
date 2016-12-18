package edu.nyu.oop;

import xtc.tree.GNode;

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

    public static List<GNode> convertToCpp(List<GNode> javaRoots) {
//        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        MakeCppAst visitor = new MakeCppAst();
        for (int i = 0; i < javaRoots.size(); i++) {
            visitor.visit(javaRoots.get(i));
        }

        printCpp(javaRoots, visitor.mainClassNode);
        printMain(visitor.mainClassNode, visitor.packageNode);

        return javaRoots;
    }

    public static void printCpp(List<GNode> javaRoots, GNode mainClass) {
//        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        String[] printAtEnd = new String[1];
        printAtEnd[0] = "";
        try {
            FileWriter output = new FileWriter("output/output.cpp");

            output.write("#include \"output.h\"\n#include \"java_lang.h\"\nusing namespace std;\n#include <iostream>\nusing namespace java::lang;\n"); //#include <io.stream> \n#pragma once \n

            PrintOutputCpp visitor = new PrintOutputCpp(output, printAtEnd, mainClass);

            for (int i = 0; i < javaRoots.size(); i++) {
                visitor.visit(javaRoots.get(i));

            }
            output.write(printAtEnd[0]);
            output.flush();
            output.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    public static void printMain(GNode mainClassDeclaration, GNode packageDeclaration) {
//        XtcTestUtils.prettyPrintAst(mainClassDeclaration);
        try {
            FileWriter output = new FileWriter("output/main.cpp");

            PrintMainCpp printer = new PrintMainCpp(output);
            printer.print(mainClassDeclaration, packageDeclaration);

            output.flush();
            output.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
