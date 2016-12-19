package edu.nyu.oop;

import org.junit.*;
import org.slf4j.Logger;

import static edu.nyu.oop.XtcTestUtils.*;
import static edu.nyu.oop.util.NodeUtil.*;
import static org.junit.Assert.*;
import edu.nyu.oop.util.SymbolTable;
import edu.nyu.oop.util.Scope;
import edu.nyu.oop.util.ClassScope;
import edu.nyu.oop.util.MethodScope;
import edu.nyu.oop.util.ConstructorScope;
import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.*;

// Testing method overload resolution
// to run:
//   test-only *MethodOverloadTest*

public class MethodOverloadTest {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(JunitTestExample.class);

    static SymbolTable table;
    static List<Node> methodCalls;
    static MethodOverloadResolver resolver;

    @BeforeClass
    public static void beforeClass() {
    	GNode ast = (GNode) loadTestFile("src/test/java/inputs/MethodOverloadTest/MethodOverloadTest.java");

        table = new SymbolTable();
        SymbolTableBuilder builder = new SymbolTableBuilder(table);
        table = builder.buildTable(ast);

        resolver = new MethodOverloadResolver(table);

        methodCalls = dfsAll(ast, "CallExpression");

        Collection<ClassScope> classes = ClassScope.classScopes.values();
        Node mainClass = null;
        for (ClassScope classScope : classes) {
            if (classScope.isMainMethodClass) {
                mainClass = classScope.associatedNode;
            }
        }
        Node mainMethod = dfs(mainClass, "MethodDeclaration");
        table.enterScopeForNode((GNode) mainMethod);

    }

    @Test
    public void testCalls() {
        assertEquals(12, methodCalls.size());
    }

    @Test
    public void testCall0() {
        MethodScope method = resolver.resolve(methodCalls.get(0));
        List<String> params = method.getParameterTypes();
        assertEquals("A", method.classScope.name);
        assertEquals("A", params.get(0));
        assertEquals("Object", params.get(1));
    }


    @Test
    public void testCall1() {
        MethodScope method = resolver.resolve(methodCalls.get(1));
        List<String> params = method.getParameterTypes();
        assertEquals("A", method.classScope.name);
        assertEquals("A", params.get(0));
        assertEquals("Object", params.get(1));
    }

    @Test
    public void testCall2() {
        MethodScope method = resolver.resolve(methodCalls.get(2));
        List<String> params = method.getParameterTypes();
        assertEquals("B", method.classScope.name);
        assertEquals("B", params.get(0));
        assertEquals("Object", params.get(1));
    }

    @Test
    public void testCall3() {
        MethodScope method = resolver.resolve(methodCalls.get(3));
        List<String> params = method.getParameterTypes();
        assertEquals("B", method.classScope.name);
        assertEquals("Object", params.get(0));
        assertEquals("B", params.get(1));
    }

    // @Test
    public void testCall4() {
        MethodScope method = resolver.resolve(methodCalls.get(4));
        List<String> params = method.getParameterTypes();
        assertEquals("B", method.classScope.name);
        assertEquals("Object", params.get(0));
        assertEquals("Object", params.get(1));
    }

    @Test
    public void testCall5() {
        MethodScope method = resolver.resolve(methodCalls.get(5));
        List<String> params = method.getParameterTypes();
        assertEquals("B", method.classScope.name);
        assertEquals("B", params.get(0));
        assertEquals("Object", params.get(1));
    }

    // @Test
    public void testCall6() {
        MethodScope method = resolver.resolve(methodCalls.get(6));
        List<String> params = method.getParameterTypes();
        assertEquals("B", method.classScope.name);
        assertEquals("Object", params.get(0));
        assertEquals("B", params.get(1));
    }

    @Test
    public void testCall7() {
        MethodScope method = resolver.resolve(methodCalls.get(7));
        List<String> params = method.getParameterTypes();
        assertEquals("B", method.classScope.name);
        assertEquals("Object", params.get(0));
        assertEquals("Object", params.get(1));
    }

    @Test
    public void testCall8() {
        MethodScope method = resolver.resolve(methodCalls.get(8));
        List<String> params = method.getParameterTypes();
        assertEquals("B", method.classScope.name);
        assertEquals("B", params.get(0));
        assertEquals("Object", params.get(1));
    }

    @Test
    public void testCall9() {
        MethodScope method = resolver.resolve(methodCalls.get(9));
        List<String> params = method.getParameterTypes();
        assertEquals("D", method.classScope.name);
        assertEquals("double", params.get(0));
    }

    @Test
    public void testCall10() {
        MethodScope method = resolver.resolve(methodCalls.get(10));
        List<String> params = method.getParameterTypes();
        assertEquals("C", method.classScope.name);
        assertEquals("int", params.get(0));
    }

    @Test
    public void testCall11() {
        MethodScope method = resolver.resolve(methodCalls.get(11));
        List<String> params = method.getParameterTypes();
        assertEquals("C", method.classScope.name);
        assertEquals("A", params.get(0));
    }

}