


package edu.nyu.oop;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import edu.nyu.oop.util.JavaFiveImportParser;
import edu.nyu.oop.util.NodeUtil;
import edu.nyu.oop.util.XtcProps;
import org.slf4j.Logger;

import xtc.tree.Location;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.util.Tool;
import xtc.lang.JavaPrinter;
import xtc.parser.ParseException;

import java.net.*;

import javax.tools.*;

import java.util.*;


/**
 * This is the entry point to your program. It configures the user interface, defining
 * the set of valid commands for your tool, provides feedback to the user about their inputs
 * and delegates to other classes based on the commands input by the user to classes that know
 * how to handle them. So, for example, do not put translation code in Boot. Remember the
 * Single Responsiblity Principle https://en.wikipedia.org/wiki/Single_responsibility_principle
 */
public class OldBoot extends Tool {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return XtcProps.get("app.name");
    }

    @Override
    public String getCopy() {
        return XtcProps.get("group.name");
    }

    @Override
    public void init() {
        super.init();
        // Declare command line arguments.
        runtime.
        bool("printJavaAst", "printJavaAst", false, "Print Java Ast.").
        bool("printJavaCode", "printJavaCode", false, "Print Java code.").
        bool("printJavaImportCode", "printJavaImportCode", false, "Print Java code for imports and package source.").
        bool("parseJava", "parseJava", false, "Parse source file dependencies.");/*;*/

        //DELETE THIS ONE LATER
        //bool("fvms", "fvms", false, "testing method finding, finding \"virtual\" methods in superclasses excluding those from Object");

    }

    @Override
    public void prepare() {
        super.prepare();
        // Perform consistency checks on command line arguments.
        // (i.e. are there some commands that cannot be run together?)
        logger.debug("This is a debugging statement."); // Example logging statement, you may delete
    }

    @Override
    public File locate(String name) throws IOException {
        File file = super.locate(name);
        if (Integer.MAX_VALUE < file.length()) {
            throw new IllegalArgumentException("File too large " + file.getName());
        }
        if(!file.getAbsolutePath().startsWith(System.getProperty("user.dir"))) {
            throw new IllegalArgumentException("File must be under project root.");
        }
        return file;
    }

    @Override
    public Node parse(Reader in, File file) throws IOException, ParseException {
        return NodeUtil.parseJavaFile(file);
    }

    @Override
    public void process(Node n) {
        if (runtime.test("printJavaAst")) {
            runtime.console().format(n).pln().flush();
        }

        if (runtime.test("printJavaCode")) {
            new JavaPrinter(runtime.console()).dispatch(n);
            runtime.console().flush();
        }

        if (runtime.test("printJavaImportCode")) {
            /*
            List<GNode> nodes = JavaFiveImportParser.parse((GNode) n);
            for(Node node : nodes) {
                runtime.console().pln();
                new JavaPrinter(runtime.console()).dispatch(node);
            }
            runtime.console().flush();
            */
        }

        if (runtime.test("parseJava")) {
            String workingDir = System.getProperty("user.dir");

            Location nLocation = n.getLocation();
            Location longLocation = new Location(workingDir + "/" + nLocation.file, nLocation.line, nLocation.column);
            n.setLocation(longLocation);

            List<GNode> allAsts = GenerateJavaASTs.beginParse((GNode) n);

            runtime.console().pln();
            for(Node node : allAsts) {
                runtime.console().pln(node.getLocation().file);
            }
            runtime.console().pln().flush();

            // TODO: Generate Inheritance hierarchy tree here when finding methods


            System.out.println(allAsts);
            System.out.println(allAsts.size());
            //TEMPORARILY DEACTIVATED, use the test class with hard-coded sample data for the schematic testing
            List<CppAst> allCPPAsts = CppHeaderAstGenerator.generateNew(allAsts);


            //XtcTestUtils.prettyPrintAst(allAsts.get(0));

            //use this for now
            //HardCodedTestCppHeaderAstGenerator.generateNew(allAsts);

        }

        /*
        //DELETE THIS LATER (find superclass's "virtual" methods excluding object)
        if(runtime.test("fvms")) {

            String workingDir = System.getProperty("user.dir");

            System.out.println(System.getProperty("user.dir"));

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            File dir = new File("/src/test/java/inputs/test001/");
            File[] files = dir.listFiles();
            ArrayList<SimpleJavaFileObject> simpleFiles = new ArrayList<SimpleJavaFileObject>();

            for(File f : files) {
                //simpleFiles.add(new SimpleJavaFileObject(f.toURI(), JavaFileObject.Kind.SOURCE));
            }

            JavaCompiler.CompilationTask c = compiler.getTask(null,null,null,null,null, simpleFiles);

            File root = new File(workingDir + "/src/test/java");

            // Load and instantiate compiled class.

            Class<?> cls;
            try {
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
                cls = Class.forName("inputs.test001.Test001", true, classLoader);

                Object instance = cls.newInstance();

                System.out.println(instance.getClass().getName());

            } catch(Exception ex) {
                System.out.println(Arrays.toString(ex.getStackTrace()));
                System.exit(-1);
            }

            System.out.println("HUH");


        }
        */

    }






    // if (runtime.test("Your command here.")) { ... don't forget to add it to init()




    /**
     * Run Boot with the specified command line arguments.
     *
     * @param args The command line arguments.
     */
    //public static void main(String[] args) {
    //new OldBoot().run(args);
    //}
}
