package edu.nyu.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import java.util.List;
import java.util.ArrayList;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import edu.nyu.oop.util.JavaFiveImportParser;
import edu.nyu.oop.util.NodeUtil;
import edu.nyu.oop.util.XtcProps;
import org.slf4j.Logger;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Tool;
import xtc.lang.JavaPrinter;
import xtc.parser.ParseException;
import java.util.*;

import java.util.HashMap;


/*
 * Phase 1
 * Generates all the Java ASTs from the source files to be translated.
 * Resolves any dependencies (other classes or imports)
 */
public class GenerateJavaASTs {
    private GenerateJavaASTs() {}

    // parses the java files and returns Java ASTs
    public static List<GNode> beginParse(GNode n) {
        HashMap<String, Boolean> fileNamesFound = new HashMap<String, Boolean>();
        List<GNode> jAsts = new ArrayList<GNode>();
        beginParse(n, fileNamesFound, jAsts);
        return jAsts;
    }

    private static void beginParse(GNode n, HashMap<String, Boolean> fileNamesFound, List<GNode> jAsts) {

        //enqueue nodes and find dependencies until no files left to explore
        Queue<GNode> nodesToCheck = new ArrayDeque<GNode>();
        nodesToCheck.add(n);
        while(!nodesToCheck.isEmpty()) {

            GNode next = nodesToCheck.poll();

            //test if seen to avoid cyclical dependencies
            String loc = next.getLocation().file;
            if(fileNamesFound.get(loc) != null) {
                continue;
            }

            //if unseen, mark as seen to avoid cycles,
            //add to list of dependencies, examine all dependency children
            fileNamesFound.put(loc, true);

            jAsts.add(next);

            nodesToCheck.addAll(JavaFiveImportParser.parse(next));
        }
    }
}
