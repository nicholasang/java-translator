package edu.nyu.oop;

import xtc.tree.GNode;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
/**
 * Created by alex on 10/27/16.
 */
public class CppCommands {

    public static List<GNode> convertToCpp(List<GNode> javaRoots) {
//        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        List<GNode> CppRoots = new ArrayList<GNode>();
  //      MakeCppAst visitor = new MakeCppAst();
        GNode mainClassNode = null;
        GNode packageNode = null;
        for (int i = 0; i < javaRoots.size(); i++) {
            Phase45Ast visitor = new Phase45Ast(javaRoots.get(i));
            CppRoots.add(visitor.Root);
            if (visitor.packageNode != null){
                packageNode = visitor.packageNode;
            }
            if (visitor.mainClassNode != null){
                mainClassNode = visitor.mainClassNode;
            }
 //           visitor.visit(javaRoots.get(i));
        }
        XtcTestUtils.prettyPrintAst(CppRoots.get(0));
//        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        printCpp(CppRoots, mainClassNode);
//       printMain(mainClassNode, packageNode);

   //     printCpp(javaRoots, visitor.mainClassNode);
   //    printMain(visitor.mainClassNode, visitor.packageNode);

        return javaRoots;
    }

    public static void printCpp(List<GNode> javaRoots, GNode mainClass) {
 //       XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        String[] printAtEnd = new String[1];
        printAtEnd[0] = "";
        try {
            FileWriter output = new FileWriter("output/output.cpp");

            output.write("#include \"output.h\"\n#include \"java_lang.h\"\nusing namespace std;\n#include <iostream>\nusing namespace java::lang;\n"); //#include <io.stream> \n#pragma once \n

            NewPrintCpp visitor = new NewPrintCpp(output, printAtEnd, mainClass);

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
 //       XtcTestUtils.prettyPrintAst(mainClassDeclaration);
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
