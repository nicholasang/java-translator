package edu.nyu.oop;

import org.junit.*;
import org.slf4j.Logger;

import static edu.nyu.oop.XtcTestUtils.loadTestFile;
import static org.junit.Assert.*;
import edu.nyu.oop.util.SymbolTable;
import edu.nyu.oop.util.Scope;
import edu.nyu.oop.util.ClassScope;
import edu.nyu.oop.util.MethodScope;
import edu.nyu.oop.util.ConstructorScope;
import xtc.tree.Node;
import xtc.tree.GNode;

// Testing the Symbol Table
// to run:
//   test-only *SymbolTableTest*

public class SymbolTableTest {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(JunitTestExample.class);

    static SymbolTable table;

    @BeforeClass
    public static void beforeClass() {
    	GNode ast = (GNode) loadTestFile("src/test/java/inputs/SymbolTableTest/SymbolTableTest.java");

        table = new SymbolTable();
        SymbolTableBuilder builder = new SymbolTableBuilder(table);

        table = builder.buildTable(ast);
        // System.out.println(table);
    }

    @Test
    public void testClassHierarchy() {
        // Object, SymbolTableTest, A, B
        assertEquals(4, ClassScope.classScopes.size());
        assertEquals("Object", ClassScope.classScopes.get("A").superClass);
        assertEquals("A", ClassScope.classScopes.get("B").superClass);
    }

    @Test
    public void testClassB() {
        ClassScope b = ClassScope.classScopes.get("B");
        assertTrue(b.hasMethod("fool"));
        assertFalse(b.hasMethod("bar"));
        assertFalse(b.hasMethod("foo"));
        assertTrue(b.hasSymbol("x"));

        assertEquals(1, b.getConstructors().size());
    }

    @Test
    public void testConstructorB() {
        ClassScope b = ClassScope.classScopes.get("B");
        ConstructorScope constructor = b.getConstructors().get(0);
        assertEquals(1, constructor.getParameterTypes().size());
        assertEquals("int", constructor.getParameterTypes().get(0));
    }

    @Test
    public void testMethodsB() {
        ClassScope b = ClassScope.classScopes.get("B");
        MethodScope fool = b.getMethods("fool").iterator().next();

        assertEquals("int", fool.returnType);
        assertFalse(fool.isStatic);
        assertTrue(fool.isPrivate);
    }

    @Test
    public void testClassA() {
        ClassScope a = ClassScope.classScopes.get("A");
        assertTrue(a.hasMethod("foo"));
        assertTrue(a.hasMethod("bar"));
        assertTrue(a.hasSymbol("i"));

        assertEquals(1, a.getConstructors().size());
    }

    @Test
    public void testConstructorA() {
        ClassScope a = ClassScope.classScopes.get("A");
        ConstructorScope constructor = a.getConstructors().get(0);
        assertEquals(1, constructor.getParameterTypes().size());
        assertEquals("int", constructor.getParameterTypes().get(0));
    }

    @Test
    public void testMethodsA() {
        ClassScope a = ClassScope.classScopes.get("A");
        MethodScope foo = a.getMethods("foo").iterator().next();
        assertTrue(foo.hasSymbol("y"));
        assertEquals("short", foo.getSymbolType("y"));
        assertEquals("int", foo.returnType);
        assertFalse(foo.isStatic);
        assertFalse(foo.isPrivate);

        MethodScope bar = a.getMethods("bar").iterator().next();
        assertTrue(bar.hasSymbol("i"));
        assertEquals("float", bar.getSymbolType("i"));
        assertEquals("double", bar.returnType);
        assertTrue(bar.isStatic);
        assertFalse(bar.isPrivate);
        assertEquals(1, bar.getParameterTypes().size());
        assertEquals("int", bar.getParameterTypes().get(0));
    }
}