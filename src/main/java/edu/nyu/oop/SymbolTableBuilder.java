package edu.nyu.oop;

import java.util.ArrayList;
import java.util.List;

import edu.nyu.oop.util.RecursiveVisitor;
import edu.nyu.oop.util.SymbolTable;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;



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

	public void printTable() {
		System.out.println(table);
	}

	public SymbolTable buildTable(GNode astRoot) {
		visit(astRoot);
		return table;
	}

	public SymbolTable getTable() {
		return table;
	}

	public void visitClassDeclaration(GNode n) {
	// - no Block (rather, ClassBody)
		table.enterNewScope(n);
		visit(n);
		table.exitCurrentScope();
	}

	public void visitConstructorDeclaration(GNode n) {
	// - has Block
		table.enterNewScope(n);

		visitFormalParameters((GNode) n.getNode(3));

		ignoreIfBlock = n.getNode(5);
		visit(n.getNode(5));
		table.exitCurrentScope();
	}

	public void visitMethodDeclaration(GNode n) {
	// - has Block
		boolean isStatic = false, isPrivate = false;
		setModifiers((GNode) n.getNode(0), isStatic, isPrivate);

		String returnType = (String) dispatch(n.getNode(2)); // Type or VoidType
		String name = n.getString(3);

		table.enterNewMethodScope(n, isPrivate, isStatic, name, returnType);

		// this adds the param types to the method scope, plus adds
		// the params as symbols inside the scope
		visitFormalParameters((GNode) n.getNode(4));

		ignoreIfBlock = n.getNode(7);
		visit(n.getNode(7));
		table.exitCurrentScope();
	}

	public void visitFormalParameters(GNode n) {
	// - no Block
		int numParams = n.size();
		for (int i = 0; i < numParams; i++) {
			visitFormalParameter((GNode) n.getNode(i));
		}
	}

	public void visitFormalParameter(GNode n) {
		// modifiers don't matter, since parameters don't have modifiers...
		// so we just need to gather the type + name for each parameter

		String type = visitType((GNode) n.getNode(1));
		String name = n.getString(3);

		table.addParameterToCurrentScope(name, type);
	}

	public void visitForStatement(GNode n) {
	// - has Block
		table.enterNewScope(n);
		ignoreIfBlock = n.getNode(1);
		visit(n);
		table.exitCurrentScope();
	}

	public void visitWhileStatement(GNode n) {
	//- has Block
		table.enterNewScope(n);
		ignoreIfBlock = n.getNode(1);
		visit(n);
		table.exitCurrentScope();
	}

	public void visitConditionalStatement(GNode n) {
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


	public void visitBlock(GNode n) {
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

	public void setModifiers(GNode modifiers, boolean isStatic, boolean isPrivate) {
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

	public void visitFieldDeclaration(GNode n) {
		boolean isStatic = false, isPrivate = false;
		setModifiers((GNode) n.getNode(0), isStatic, isPrivate);

		String type = visitType((GNode) n.getNode(1)); // can't be VoidType
		List<String> names = visitDeclarators((GNode) n.getNode(2));

		for (int i = 0; i < names.size(); i++) {
			table.addSymbolToCurrentScope(names.get(i), type, isPrivate, isStatic);
		}
	}

	public List<String> visitDeclarators(GNode n) {
		int numOfDeclarators = n.size();

		List<String> names = new ArrayList<String>();
		for (int i = 0; i < numOfDeclarators; i++) {
			names.add( visitDeclarator((GNode) n.getNode(i)) );
		}
		return names;
	}

	public String visitDeclarator(GNode n) {
		return n.getString(0);
	}

	public String visitType(GNode n) {
		String type = n.getNode(0).getString(0);
		if (n.get(1) != null) {
			type += "[]";
		}
		return type;
	}

	public String visitVoidType(GNode n) {
		return "void";
	}

	public Modifier visitModifiers(GNode n) {
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

	public void visitBasicForControl(GNode n) {
		visitFieldDeclaration(n);
	}
}