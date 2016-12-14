package edu.nyu.oop;

import xtc.tree.GNode;

import java.io.FileWriter;

/**
 * Created by Alex on 12/7/16.
 */
public class PrintMethod {
    FileWriter pen;
    GNode root;

    public PrintMethod(GNode n, FileWriter printer){
        root = n;
        pen = printer;
        run();
    }

    private void run(){

    }
}
