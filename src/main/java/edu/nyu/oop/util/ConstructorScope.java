package edu.nyu.oop.util;

import java.util.*;

import xtc.Constants;
import xtc.tree.GNode;


public class ConstructorScope extends Scope {
    public ClassScope classScope;
    List< String > parameterTypes; // in order

    public ConstructorScope(GNode n) {
        this(n, null);
    }

    public ConstructorScope(GNode n, ClassScope parent) {
        super(n, parent);
        this.classScope = parent;
        this.parameterTypes = new ArrayList<String>();
        classScope.addConstructor(this);
    }

	public String toString(String indent) {
		String string = indent + "Constructor (";
			for (String type : parameterTypes) {
			string += type + ", ";
		}
        string += ")";
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

	public List<String> getParameterTypes() {
		return parameterTypes;
	}

}