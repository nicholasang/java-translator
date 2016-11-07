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
    }

    public void print(Node mainClassDeclaration, Node packageDeclaration) {
        String[] printLaterArray = new String[1];
        printLaterArray[0] = "";
        printVisitor = new printOutputCpp(pen, printLaterArray, (GNode) mainClassDeclaration);
        printVisitor.inMain = true;

        List<Node> fields = NodeUtil.dfsAll(mainClassDeclaration, "FieldDeclaration");

        String namespace = "";
        if (packageDeclaration.size() >= 1) {
            Node packageQualifiedID = packageDeclaration.getNode(1);

            for (int i = 0; i < packageQualifiedID.size(); i++) {
                String namespaceString = packageQualifiedID.getString(i);
                namespaceString = namespaceString.substring("namespace ".length(), namespaceString.length() - " { ".length());
                namespace += namespaceString + "::";
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
            pen.write("return 0; }");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void printStaticFields(List<Node> fields) {
        for (Node field : fields) {
            for (int i = 0; i < field.getNode(0).size(); i++) {
                Node modifier = field.getNode(0).getNode(i);
                if ("static".equals(modifier.getString(0))) {
                    printVisitor.visit(field);
                }
            }
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
