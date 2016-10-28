package edu.nyu.oop;


import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;

import java.util.*;

import edu.nyu.oop.util.NodeUtil;
import org.junit.AfterClass;
import org.slf4j.Logger;
import xtc.tree.GNode;

import xtc.tree.Location;
import xtc.tree.Node;


import java.io.File;

import org.junit.Test;

public class TestPhases
{
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(TestPhases.class);

    private static List<GNode> allAsts;
    private static final String TESTFILERELATIVEPATH = "src/test/java/inputs/testABCD/TestABCD.java";

    @Test
    public void TestPhase1()
    {
        Node n = NodeUtil.parseJavaFile(new File(TESTFILERELATIVEPATH));

        String workingDir = System.getProperty("user.dir");

        Location nLocation = n.getLocation();
        Location longLocation = new Location(workingDir + "/" + nLocation.file, nLocation.line, nLocation.column);
        n.setLocation(longLocation);

        allAsts = GenerateJavaASTs.beginParse((GNode) n);

        String[] correctSubPaths = {
                "translator-4Tran/src/test/java/inputs/testABCD/TestABCD.java",
                "translator-4Tran/src/test/java/inputs/testABCD/C.java",
                "translator-4Tran/src/test/java/inputs/testABCD/B.java"
        };

        for(int i = 0; i < correctSubPaths.length; ++i) {

            String toTest = allAsts.get(i).getLocation().file;
            org.junit.Assert.assertEquals(toTest.substring(toTest.length() - correctSubPaths[i].length()), correctSubPaths[i]);
        }
    }

    @AfterClass
    public static void afterClass() {
    }


}
