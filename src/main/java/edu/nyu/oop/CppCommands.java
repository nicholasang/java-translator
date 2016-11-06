package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;

import java.util.List;
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

            output.write("#include <io.stream>\n#pragma once\n");

            printOutputCpp visitor = new printOutputCpp(output, printAtEnd, mainClass);

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
