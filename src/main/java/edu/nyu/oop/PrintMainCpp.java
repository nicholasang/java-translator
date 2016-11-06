package edu.nyu.oop;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.Node;
import xtc.tree.GNode;
import java.util.*;
import java.io.FileWriter;

/**
 * Created by kplajer on 11/2/16. (with a lot of copying from PrintOutputCpp)
 */

public class PrintMainCpp {

    printOutputCpp printVisitor;
    FileWriter pen = null;
    String printThisAfter = " ";
    String printThisBefore = "";
    String printAtEnd = "";

    public PrintMainCpp(FileWriter outFileWrite) {
        pen = outFileWrite;
        printVisitor = new printOutputCpp(pen);
    }

    public void print(Node mainClassDeclaration, Node packageDeclaration) {
        List<Node> fields = NodeUtil.dfsAll(mainClassDeclaration, "FieldDeclaration");

        String namespace = "";
        if (packageDeclaration.size() >= 1) {
            Node packageQualifiedID = packageDeclaration.getNode(1);

            for (int i = 0; i < packageQualifiedID.size(); i++) {
                namespace += packageQualifiedID.getString(i) + "::";
            }

            namespace = namespace.substring(0, ((namespace.length() < 2) ? 0 : namespace.length() - 2));
        }

        printInitialStuff(namespace);

        printStaticFields(fields);

        printMainMethod(mainClassDeclaration);
    }

    private void printMainMethod(Node mainClassDeclaration) {
        try {
            pen.write("int main(int argc, char* argv[])\n{ ");
        } catch (Exception e) {
            System.out.println(e);
        }

        Node mainBlock = NodeUtil.dfs(mainClassDeclaration, "Block");

        // reuse methods from output printer
        printVisitor.visit(mainBlock);

        try {
            pen.write("}");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void printStaticFields(List<Node> fields) {
        for (Node field : fields) {
            printVisitor.visit(field);
        }
    }

    private void printInitialStuff(String namespace) {
        try {
            pen.write("#include <iostream>\n" +
                      "#include \"output.h\"\n" +
                      "#include \"java_lang.h\"\n" +
                      "using namespace std;\n" +
                      "using namespace java::lang; ");
            pen.write("using namespace " + namespace + "; ");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
