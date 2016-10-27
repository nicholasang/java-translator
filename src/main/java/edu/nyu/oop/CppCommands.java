package edu.nyu.oop;

import xtc.tree.GNode;

import java.io.File;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Created by alex on 10/27/16.
 */
public class CppCommands {
    public static List<GNode> convertToCpp(List<GNode> javaRoots){
        MakeCppAst visitor = new MakeCppAst();
        for (int i = 0; i < javaRoots.size(); i++){
            visitor.visit(javaRoots.get(i));
        }
        //visitor.visit(javaRoot.get(0));
        return javaRoots;
    }

    public static void printCpp(List<GNode> javaRoots) throws FileNotFoundException{
        //TODO figure out how to make it print file in output folder
        File output = new File("out.cpp");
        printOutputCpp visitor = new printOutputCpp(output);
        for (int i = 0; i < javaRoots.size(); i++){
            visitor.visit(javaRoots.get(i));
        }
    }
}
