package edu.nyu.oop;

import xtc.tree.GNode;

import java.io.File;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
/**
 * Created by alex on 10/27/16.
 */
public class CppCommands {
    public static List<GNode> convertToCpp(List<GNode> javaRoots) {
        MakeCppAst visitor = new MakeCppAst();
        for (int i = 0; i < javaRoots.size(); i++) {
            visitor.visit(javaRoots.get(i));
        }

        printCpp(javaRoots);

        //visitor.visit(javaRoot.get(0));
        return javaRoots;
    }

    public static void printCpp(List<GNode> javaRoots) {
        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        String[] printAtEnd = new String[1];
        printAtEnd[0] = "";
        try {
            FileWriter output = new FileWriter("output/output.cpp");

            output.write("#include \"output.h\"\n#include \"java_lang.h\"\nusing namespace std;\n#include <iostream>\nusing namespace java::lang;\n"); //#include <io.stream> \n#pragma once \n

            printOutputCpp visitor = new printOutputCpp(output, printAtEnd);
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
}
