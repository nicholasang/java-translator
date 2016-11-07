package edu.nyu.oop;

import java.io.FileNotFoundException;
import java.util.*;

import edu.nyu.oop.util.CppHVisitor;
import edu.nyu.oop.util.NodeUtil;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import xtc.tree.GNode;
import xtc.tree.Node;

import java.io.File;

import static org.junit.Assert.*;
import org.junit.*;

public class TestPhases4And5 {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(UnitTestPhases.class);

    private static final String testFilePath = "src/test/java/inputs/test005/Test005.java";
    private static final String outputFile = "output/output.cpp";
    private static final String mainFile = "output/main.cpp";

    @BeforeClass
    public static void beforeClass() {
        Node javaNode = NodeUtil.parseJavaFile(new File(testFilePath));

        //phase 1, 2, 3
        List<GNode> allAsts = GenerateJavaASTs.beginParse((GNode) javaNode);
        CppAst headerCppAst = CppHeaderAstGenerator.generateNew(allAsts);
        new CppHVisitor().visit(headerCppAst);

        //phase 4 + 5
        CppCommands.convertToCpp(allAsts);
    }

    @Test
    public void testOutputCpp() {
        File output = new File(outputFile);

        try {
            Scanner cpp = new Scanner(output);

            String line = cpp.nextLine();
            while ((! line.contains("namespace")) || line.contains(";")) {
                line = cpp.nextLine();
            }

            assertTrue(line.contains("inputs"));

            line = cpp.nextLine();
            while (! line.contains("namespace")) {
                line = cpp.nextLine();
            }

            assertTrue(line.contains("test005"));
            System.out.println("Pass - namespaces");

            line = cpp.nextLine();
            while (! line.contains("toString")) {
                line = cpp.nextLine();
            }

            assertTrue(line.contains("String __A::toString(A __this)"));

            line = cpp.nextLine();
            while (! line.contains("return")) {
                line = cpp.nextLine();
                assertFalse(line.contains("}"));
            }

            assertTrue(line.contains("new __String(\"A\")"));
            System.out.println("Pass - A's toString");

            line = cpp.nextLine();
            while (! line.contains("toString")) {
                line = cpp.nextLine();
            }

            assertTrue(line.contains("String __B::toString(B __this)"));

            line = cpp.nextLine();
            while (! line.contains("return")) {
                line = cpp.nextLine();
                assertFalse(line.contains("}"));
            }

            assertTrue(line.contains("new __String(\"B\")"));
            System.out.println("Pass - B's toString");

            cpp.close();

            System.out.println("output.cpp correct");

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }


    @Test
    public void testMainCpp() {
        File mainF = new File(mainFile);

        try {
            Scanner main = new Scanner(mainF);

            String line = main.nextLine();
            while ((! line.contains("{"))) {
                line = main.nextLine();
            }

            assertTrue(line.contains("B b =  (B) new  __B ();"));

            line = main.nextLine();
            assertTrue(line.contains("A a1 =  (A) new  __A ();"));

            System.out.println("Pass - Declaration and initialization of variables");

            line = main.nextLine();
            assertTrue(line.contains("A a2 =  (A) b"));
            System.out.println("Pass - casting to superclass");

            line = main.nextLine();
            assertTrue(line.contains("std::cout << a1->__vptr->toString(a1)->data<< endl"));
            line = main.nextLine();
            assertTrue(line.contains("std::cout << a2->__vptr->toString(a2)->data<< endl"));
            System.out.println("Pass - printing");

            main.close();

            System.out.println("main.cpp correct");

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

}
