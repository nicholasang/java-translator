package edu.nyu.oop.util;

import java.util.*;

import xtc.Constants;
import xtc.tree.GNode;


public class MethodScope extends Scope {
	ClassScope classScope;
	String name; // not mangled
	String uniqueName; // mangled
	List< String > parameterTypes; // in order
    String returnType;
    boolean isPrivate;
    boolean isStatic;

	public MethodScope(GNode n) {
		this(n, null);
	}

	public MethodScope(GNode n, ClassScope parent) {
		super(n, parent);
		this.classScope = parent;
		this.parameterTypes = new ArrayList<String>();

        classScope.addMethod(name, this);
	}

	public MethodScope(GNode n, ClassScope parent, boolean isPrivate, boolean isStatic, String name, String returnType) {
		super(n, parent);
		this.classScope = parent;
		this.name = name;
		this.parameterTypes = new ArrayList<String>();
		this.isPrivate = isPrivate;
		this.isStatic = isStatic;
		this.returnType = returnType;

        classScope.addMethod(name, this);
	}

	public String toString(String indent) {
		String string = indent + "Method " + this.name + " (";
		for (String type : parameterTypes) {
			string += type + ", ";
		}
        string += ") -> " + returnType;
		Collection<Symbol> symbols = this.getAllSymbols();
		string += "\n" + "  " + indent + "symbols:";
		for (Symbol symbol : symbols) {
			string += "\n" + "   " + indent + symbol.name + " : " + symbol.type;
		}

		Collection<Scope> scopes = this.getAllScopes();
		string += "\n" + "  " + indent + "nested scopes:";
		for (Scope childScope : scopes) {
			string += "\n" + childScope.toString(indent + "   ");
		}
		return string;
	}

	public String toString() {
		return toString("");
	}

	public void addParameter(String type) {
		parameterTypes.add(type);
	}

	public String getMangledName() {
		String mangledName = name;
		for (int i = 0; i < parameterTypes.size(); i++) {
			mangledName += parameterTypes.get(i);
		}
		return mangledName;
	}
}