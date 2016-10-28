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
    public static List<GNode> convertToCpp(List<GNode> javaRoots){
    MakeCppAst visitor = new MakeCppAst();
        for (int i = 0; i < javaRoots.size(); i++){
            visitor.visit(javaRoots.get(i));
        }

        printCpp(javaRoots);

        //visitor.visit(javaRoot.get(0));
        return javaRoots;
    }

    public static void printCpp(List<GNode> javaRoots){
        XtcTestUtils.prettyPrintAst(javaRoots.get(0));
        try{
            FileWriter output = new FileWriter("output/output.cpp");

            printOutputCpp visitor = new printOutputCpp(output);
            for (int i = 0; i < javaRoots.size(); i++){
                visitor.visit(javaRoots.get(i));
            }
            output.flush();
            output.close();
        }catch(IOException e) {
            System.out.println(e);
        }


    }
}
