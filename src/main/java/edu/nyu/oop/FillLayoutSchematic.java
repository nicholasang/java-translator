package edu.nyu.oop;

import java.util.*;

import edu.nyu.oop.util.NodeUtil;
import xtc.tree.GNode;
import xtc.tree.Node;

/**
 * Created by kplajer on 10/28/16.
 */
public class FillLayoutSchematic {

    public static void fillClasses(CppAst topNode) {
        List<ClassRef> classList = topNode.getClassRefs();

        for (ClassRef classRef : classList) {
            populateSchematic(classRef.getLayoutSchematic(), classRef.getJClassDeclaration());
        }
    }

    public static void populateSchematic(LayoutSchematic schematic, GNode classNode) {
        List<Node> methodNodes = NodeUtil.dfsAll(classNode, "MethodDeclaration");
        List<Node> fieldNodes = NodeUtil.dfsAll(classNode, "FieldDeclaration");


        for (Node methodNode : methodNodes) {
            LayoutSchematic.Method method = new LayoutSchematic.Method();

            if (methodNode.getNode(0) != null) { // access modifier and isStatic
                Node modifiers = methodNode.getNode(0);
                Node modifier = modifiers.getNode(0);
                if (modifier != null) {
                    if (modifier.getString(0).equals("static")) {
                        method.isStatic = true;
                    }
                    else {
                        method.accessModifier = modifier.getString(0);

                        modifier = modifiers.getNode(1);
                        if (modifier != null) {
                            if (modifier.getString(0).equals("static")) {
                                method.isStatic = true;
                            }

                        }
                    }
                }
            }

            Node type = methodNode.getNode(2);
            if (type.getName().equals("VoidType")) {
                method.returnType = "void";
            }
            else {
                method.returnType = type.getNode(0).getString(0);
            }

            method.name = methodNode.getString(3);

            Node parameters = methodNode.getNode(4);
            int paramIndex = 0;
            Node parameter = parameters.getNode(paramIndex);
            while (parameter != null) {
                paramIndex++;
                Node parameterType = parameter.getNode(1);
                method.parameterTypes.add(parameterType.getNode(0).getString(0));
            }

            schematic.classStruct.methodList.add(method);
        }

        for (Node field : fieldNodes) {

        }
    }
}
