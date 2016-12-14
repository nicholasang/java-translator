package edu.nyu.oop;

import java.util.ArrayList;
import java.util.List;

import edu.nyu.oop.util.RecursiveVisitor;
import edu.nyu.oop.util.SymbolTable;
import xtc.Constants;

import xtc.lang.JavaEntities;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.tree.Attribute;
import xtc.tree.Printer;
// import xtc.util.SymbolTable;
 // import xtc.util.Runtime;
 import xtc.type.*;


public class SymbolTableBuilder extends RecursiveVisitor {
    private SymbolTable table;

    enum Modifier {
        STATIC,
        PRIVATE,
        STATIC_PRIVATE,
        NONE
    }

    // used to distinguish anonymous blocks (which need a new Scope)
    // from Blocks that don't need a new Scope (e.g. the Block inside a
    // MethodDeclaration, because a Scope was already created when
    // visiting the MethodDeclaration)
    private Node ignoreIfBlock = null;
    // ^ so, basically, when visiting Blocks, check this and if the node
    // you're visiting == this node, ignore / skip

    public SymbolTableBuilder(SymbolTable table) {
        this.table = table;
    }

    public void visitClassDeclaration(Node n) {
    // - no Block (rather, ClassBody)
        table.enterNewScope(n);
        visit(n);
        table.exitCurrentScope();
    }

    public void visitConstructorDeclaration(Node n) {
    // - has Block
        table.enterNewScope(n);
        ignoreIfBlock = n.getNode(5);
        visit(n);
        table.exitCurrentScope();
    }

    public void visitMethodDeclaration(Node n) {
    // - has Block
        table.enterNewScope(n);

        boolean isStatic = false, isPrivate = false;
        setModifiers(n.getNode(0), isStatic, isPrivate);

        String returnType = (String) dispatch(n.getNode(2)); // Type or VoidType
        String name = n.getString(3);
        List<String> paramTypes = visitFormalParameters(n.getNode(4));



        // TODO
        ignoreIfBlock = n.getNode(7);
        visit(n);
        table.exitCurrentScope();
    }

    public visitFormalParameters(Node n) {
        // blahblah

        table.addParameterToCurrentScope(name, type, isPrivate, isStatic)
    }

    public void visitForStatement(Node n) {
    // - has Block
        table.enterNewScope(n);
        ignoreIfBlock = n.getNode(1);
        visit(n);
        table.exitCurrentScope();
    }

    public void visitWhileStatement(Node n) {
    //- has Block
        table.enterNewScope(n);
        ignoreIfBlock = n.getNode(1);
        visit(n);
        table.exitCurrentScope();
    }

    public void visitConditionalStatement(Node n) {
    // - MAY have Block (i.e. one-liner if without brackets has no Block)
    // NOTE: elif / else is nested inside the first if (after that if's Block)
    // elif is nested as another ConditionalStatement; else is just nested as a Block
    // so be careful to exit the first if-scope before entering these

        table.enterNewScope(n);
        ignoreIfBlock = n.getNode(1);
        visit(n.getNode(1)); // visits the innards of the if statement
        table.exitCurrentScope();

        // (see note at top of method) visits an "else if" or "else" if there is one
        if (n.get(2) != null) {
            visit(n.getNode(2));
        }
    }


    public void visitBlock(Node n) {
    // use ignoreIfBlock to differentiate anonymous blocks from other blocks
    // and thus decide whether to create a new scope
        if (ignoreIfBlock != null && ignoreIfBlock == n) {
            visit(n);
        }
        else {
            ignoreIfBlock = null;
            table.enterNewScope(n);
            visit(n);
            table.exitCurrentScope();
        }
    }

    public void setModifiers(Node modifiers, boolean isStatic, boolean isPrivate) {
        Modifier modifier = visitModifiers(modifiers);
        isStatic = false;
        isPrivate = false;
        if (modifier == Modifier.STATIC_PRIVATE) {
            isStatic = true;
            isPrivate = true;
        }
        else if (modifier == Modifier.STATIC) {
            isStatic = true;
        }
        else if (modifier == Modifier.PRIVATE) {
            isPrivate = true;
        }

    }

    public void visitFieldDeclaration(Node n) {
        boolean isStatic = false, isPrivate = false;
        setModifiers(n.getNode(0), isStatic, isPrivate);

        String type = (String) dispatch(n.getNode(1)); // Type or VoidType
        List<String> names = visitDeclarators(n.getNode(2));

        for (int i = 0; i < names.size(); i++) {
            table.addSymbolToCurrentScope(names.get(i), type, isPrivate, isStatic);
        }
    }

    public List<String> visitDeclarators(Node n) {
        int numOfDeclarators = n.size();

        List<String> names = new ArrayList<String>();
        for (int i = 0; i < numOfDeclarators; i++) {
            names.add( visitDeclarator(n.getNode(i)) );
        }
        return names;
    }

    public String visitDeclarator(Node n) {
        return n.getString(0);
    }

    public String visitType(Node n) {
        return n.getNode(0).getString(0);
    }

    public String visitVoidType(Node n) {
        return "void";
    }

    public Modifier visitModifiers(Node n) {
        int numOfModifiers = n.size();
        boolean isStatic = false;
        boolean isPrivate = false;
        for (int i = 0; i < numOfModifiers; i++) {
            String modifier = n.getNode(i).getString(0);
            if (modifier.equals("static")) {
                isStatic = true;
            }
            else if (modifier.equals("private")) {
                isPrivate = true;
            }
        }

        if (isStatic && isPrivate) {
            return Modifier.STATIC_PRIVATE;
        }
        else if (isStatic) {
            return Modifier.STATIC;
        }
        else if (isPrivate) {
            return Modifier.PRIVATE;
        }
        else {
            return Modifier.NONE;
        }
    }

    public void visitBasicForControl(Node n) {
        visitFieldDeclaration(n);
    }

    public void visitFormalParameters(Node n) {

    }


    /*
     create new symbols:
         visitForLoopInnards
         visitWhileLoopInnards
         visitConditional
         visitFormalParameters
         visitFieldDeclaration

     helper:
         qualified identifier
         type(s)
    */


}


// public class SymbolTableBuilder extends RecursiveVisitor {
//     private SymbolTable table;
//
//     private boolean isMethodBlock = false;
//
//     public SymbolTableBuilder(SymbolTable table) {
//         this.table = table;
//     }
//
//     // is this needed? not *really* a "scope"
//     // public void visitCompilationUnit(GNode n) {
//     //     visit(n);
//     // }
//
//
//     // creating scopes:
//
//     public void visitPackageDeclaration(GNode n) {
//         table.enter(getPackageName(n));
//         table.mark(n);
//         visit(n);
//         table.exit();
//     }
//
//     public void visitClassDeclaration(GNode n) {
//         table.enter(getClassName(n));
//         table.mark(n);
//         visit(n);
//         table.exit();
//     }
//
//     public void visitMethodDeclaration(GNode n) {
//         table.enter(getMethodHeader(n));
//         table.mark(n);
//         isMethodBlock = true;
//         visit(n);
//         table.exit();
//     }
//
//     public void visitBlock(GNode n) {
//         if (isMethodBlock) {
//             isMethodBlock = false;
//             visit(n);
//         }
//         else {
//             table.enter(getNameForAnonymous("block"));
//             table.mark(n);
//             visit(n);
//             table.exit();
//         }
//     }
//
//     public void visitForStatement(GNode n) {
//         table.enter(getNameForAnonymous("for"));
//         table.mark(n);
//         visit(n);
//         table.exit(n);
//     }
//
//     public void visitWhileStatement(GNode n) {
//         table.enter(getNameForAnonymous("while"));
//         table.mark(n);
//         visit(n);
//         table.exit(n);
//     }
//
//
//     // creating symbols:
//
//     public void visit
//
//     /*
//     create new scopes:
//         visitCompilationUnit
//         visitPackageDeclaration
//         visitClassDeclaration
//         visitMethodDeclaration
//         visitForLoopDeclaration (ForStatement)
//         visitWhileStatement
//         visitBlockDeclaration (anonymous blocks)
//
//     create new symbols:
//         visitForLoopInnards
//         visitWhileLoopInnards
//         visitConditional
//         visitFormalParameters
//         visitFieldDeclaration
//
//     helper:
//         qualified identifier
//         type(s)
//     */
//
//
// // helper-y methods just to make the code more readable:
//     private String getPackageName(GNode packageDeclaration) {
//         String name = (String) dispatch(packageDeclaration.getNode(1));
//         return JavaEntities.packageNameToScopeName(name);
//     }
//
//     private String getClassName(GNode classDeclaration) {
//         return classDeclaration.getString(1);
//     }
//
//     private String getMethodHeader(GNode methodDeclaration) {
//         return JavaEntities.methodSymbolFromAst(methodDeclaration);
//     }
//
//     private String getNameForAnonymous(String nodeType) {
//         return table.freshname(nodeType);
//     }
//
// }

// /**
//  * This is a simplified XTC symbol table builder.  Its purpose is to
//  * give you some insight into how a Symbol Table is built.
//  */
// public class SymbolTableBuilder extends RecursiveVisitor {
//     final private SymbolTable table;
//     // final private Runtime runtime;
//
//     public SymbolTableBuilder(final SymbolTable table) {
//         // this.runtime = runtime;
//         this.table = table;
//     }
//
//     public void visitCompilationUnit(GNode n) {
//         if (null == n.get(0))
//             visitPackageDeclaration(null);
//         else
//             dispatch(n.getNode(0));
//
//         table.enter(JavaEntities.fileNameToScopeName(n.getLocation().file));
//         table.mark(n);
//
//         for (int i = 1; i < n.size(); i++) {
//             GNode child = n.getGeneric(i);
//             dispatch(child);
//         }
//
//         table.setScope(table.root());
//     }
//
//     public void visitPackageDeclaration(final GNode n) {
//         String canonicalName = null == n ? "" : (String) dispatch(n.getNode(1));
//         table.enter(JavaEntities.packageNameToScopeName(canonicalName));
//         table.mark(n);
//     }
//
//     public void visitClassDeclaration(GNode n) {
//         String className = n.getString(1);
//         table.enter(className);
//         table.mark(n);
//         visit(n);
//         table.exit();
//     }
//
//     public void visitMethodDeclaration(GNode n) {
//         String methodName = JavaEntities.methodSymbolFromAst(n);
//         table.enter(methodName);
//         table.mark(n);
//         visit(n);
//         table.exit();
//     }
//
//     public void visitBlock(GNode n) {
//         table.enter(table.freshName("block"));
//         table.mark(n);
//         visit(n);
//         table.exit();
//     }
//
//     public void visitForStatement(GNode n) {
//         table.enter(table.freshName("forStatement"));
//         table.mark(n);
//         visit(n);
//         table.exit();
//     }
//
//     /**
//      * Visit a Modifiers = Modifier*.
//      */
//     public final List<Attribute> visitModifiers(final GNode n) {
//         final List<Attribute> result = new ArrayList<Attribute>();
//         for (int i = 0; i < n.size(); i++) {
//             final String name = n.getGeneric(i).getString(0);
//             final Attribute modifier = JavaEntities.nameToModifier(name);
//             if (null == modifier)
//                 // runtime.error("unexpected modifier " + name, n);
//             else if (result.contains(modifier))
//                 // runtime.error("duplicate modifier " + name, n);
//             else
//                 result.add(modifier);
//         }
//         return result;
//     }
//
//     public final List<Type> visitFieldDeclaration(final GNode n) {
//         @SuppressWarnings("unchecked")
//         final List<Attribute> modifiers = (List<Attribute>) dispatch(n.getNode(0));
//         Type type = (Type) dispatch(n.getNode(1));
//         return processDeclarators(modifiers, type, n.getGeneric(2));
//     }
//
//     public final List<Type> processDeclarators(final List<Attribute> modifiers,
//                                                final Type type, final GNode declarators) {
//         final List<Type> result = new ArrayList<Type>();
//         boolean isLocal = JavaEntities.isScopeLocal(table.current().getQualifiedName());
//         for (final Object i : declarators) {
//             GNode declNode = (GNode) i;
//             String name = declNode.getString(0);
//             Type dimType = JavaEntities.typeWithDimensions(type,
//                 countDimensions(declNode.getGeneric(1)));
//             Type entity = isLocal ? VariableT.newLocal(dimType, name) :
//                 VariableT.newField(dimType, name);
//             for (Attribute mod : modifiers)
//                 entity.addAttribute(mod);
//             if (null == table.current().lookupLocally(name)) {
//                 result.add(entity);
//                 table.current().define(name, entity);
//                 //entity.scope(table.current().getQualifiedName());
//             }
//         }
//         return result;
//     }
//
//     public final Type visitFormalParameter(final GNode n) {
//         assert null == n.get(4) : "must run JavaAstSimplifier first";
//         String id = n.getString(3);
//         Type dispatched = (Type) dispatch(n.getNode(1));
//         Type result = VariableT.newParam(dispatched, id);
//         if (n.getGeneric(0).size() != 0)
//             result.addAttribute(JavaEntities.nameToModifier("final"));
//         if (null == table.current().lookupLocally(id)) {
//             table.current().define(id, result);
//             result.scope(table.current().getQualifiedName());
//         } else
//             // runtime.error("duplicate parameter declaration " + id, n);
//         assert JavaEntities.isParameterT(result);
//         return result;
//     }
//
//     public final Type visitPrimitiveType(final GNode n) {
//         final Type result = JavaEntities.nameToBaseType(n.getString(0));
//         return result;
//     }
//
//     public final Type visitType(final GNode n) {
//         final boolean composite = n.getGeneric(0).hasName("QualifiedIdentifier");
//         final Object dispatched0 = dispatch(n.getNode(0));
//         assert dispatched0 != null;
//         final Type componentT = composite ? new AliasT((String) dispatched0) : (Type) dispatched0;
//         final int dimensions = countDimensions(n.getGeneric(1));
//         final Type result = JavaEntities.typeWithDimensions(componentT, dimensions);
//         return result;
//     }
//
//     public static int countDimensions(final GNode dimNode) {
//         return null == dimNode ? 0 : dimNode.size();
//     }
//
//     public final Type visitVoidType(final GNode n) {
//         return JavaEntities.nameToBaseType("void");
//     }
//
//     public final String visitQualifiedIdentifier(final GNode n) {
//         final StringBuffer b = new StringBuffer();
//         for (int i = 0; i < n.size(); i++) {
//             if (b.length() > 0)
//                 b.append(Constants.QUALIFIER);
//             b.append(n.getString(i));
//         }
//         return b.toString();
//     }
//}