package edu.nyu.oop.util;

import java.util.*;

import xtc.Constants;
import xtc.tree.GNode;


public class Scope {
	private static final boolean DEBUG = false;

	private Map< GNode, Scope > nestedScopes;
	private Map< String, Symbol > symbols;
	Scope parent;
	GNode associatedNode;

	public Scope() {
		this(null, null);
	}

	public Scope(GNode n) {
		this(n, null);
	}

	public Scope(GNode n, Scope parent) {
		this.associatedNode = n;
		this.parent = parent;
		this.nestedScopes = new HashMap<GNode, Scope>();
		this.symbols = new HashMap<String, Symbol>();
		if (DEBUG && n != null) {
			System.out.println(n.getName() + " created");
		}
		if (parent != null) {
			if (DEBUG && parent.associatedNode == null) {
				System.out.println(" parent is: ROOT" );
			}
			else if (DEBUG) {
				System.out.println(" parent is: " + parent.associatedNode.getName());
			}
		}
	}

	public String toString(String indent) {
		String string = indent + this.associatedNode.getName();
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

	public boolean hasParent() {
		return (parent == null);
	}

	public boolean addSymbol(Symbol symbol) {
		if (this.hasSymbol(symbol.name)) {
			return false;
		}
		symbols.put(symbol.name, symbol);
		return true;
	}

	public boolean hasSymbol(String name) {
		return symbols.containsKey(name);
	}

	public Symbol getSymbol(String name) {
		if (this.hasSymbol(name)) {
			return symbols.get(name);
		}
		return null;
	}

	public String getSymbolType(String name) {
		if (this.hasSymbol(name)) {
			return symbols.get(name).type;
		}
		return null;
	}

	public boolean addScope(Scope scope) {
		if (this.hasScope(scope.associatedNode)) {
			return false;
		}
		Scope old = nestedScopes.put(scope.associatedNode, scope);
		return true;
	}

	public boolean hasScope(GNode n) {
		return nestedScopes.containsKey(n);
	}

	public Scope getScope(GNode n) {
		if (this.hasScope(n)) {
			return nestedScopes.get(n);
		}
		return null;
	}

	public Collection<Scope> getAllScopes() {
		return nestedScopes.values();
	}

	public Collection<Symbol> getAllSymbols() {
		return symbols.values();
	}
}