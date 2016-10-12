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

public class generateDependencyASTs
{
    private generateDependencyASTs(){};

    public static List<GNode> beginParse(GNode n)
    {
        HashMap<String, Boolean> fileNamesFound = new HashMap<String, Boolean>();
        List<GNode> dependencyASTs = new ArrayList<GNode>();
        beginParse(n, fileNamesFound, dependencyASTs);

        return dependencyASTs;
    }

    private static void beginParse(GNode n, HashMap<String, Boolean> fileNamesFound, List<GNode> dependencyASTs)
    {

        Queue<GNode> ad = new ArrayDeque<GNode>();
        ad.add(n);
        while(!ad.isEmpty())
        {
            GNode underExamination = ad.poll();

            String loc = underExamination.getLocation().file;
            if(fileNamesFound.get(loc))
            {
                continue;
            }

            fileNamesFound.put(loc, true);

            dependencyASTs.add(underExamination);

            ad.addAll(JavaFiveImportParser.parse(underExamination));
        }

    }



}
