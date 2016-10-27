package edu.nyu.oop;

import org.junit.*;
import org.slf4j.Logger;

import static org.junit.Assert.*;

import xtc.tree.GNode;

/**
 * Created by kplajer on 10/13/16.
 */
public class Phase2Test {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(JunitTestExample.class);

    GNode headerAST = null;

    @BeforeClass
    public static void beforeClass() {
        // one-time initialization code before all test functions
        // The benefit of doing static init here is there is a failure you will get
        // informative error messages from JUnit and it will continue to run any other tests that it can.
        // As opposed to just blowing up and stopping test execution.
        logger.debug("BeforeClass - beforeClass");
    }

    @AfterClass
    public static void afterClass() {
        // one-time cleanup code after all test functions run
        logger.debug("@AfterClass - afterClass");
    }

    @Before
    public void setUp() {
        // run before each test function
        // the benefit if utilizing this function is to reduce code duplication
        // and you can also reset the state of your class before each test is executed.
        logger.debug("@Before - setUp");
    }

    @After
    public void tearDown() {
        // run after each test function
        logger.debug("@After - tearDown");
    }

    @Test
    public void testSomethingElse() {
        boolean b = true;
        assertEquals(b, true);
    }

}
