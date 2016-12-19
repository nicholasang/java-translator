package edu.nyu.oop.util;

import java.util.*;

import xtc.Constants;
import xtc.tree.GNode;


public class SymbolTable {
	private static final boolean DEBUG = false;

	private Scope root;
	private Scope currentScope;
	private Map< String, ClassScope > classScopes = new HashMap< String, ClassScope >();

	public SymbolTable() {
		this.root = new Scope();
		this.currentScope = root;
	}

	public void enterNewScope(GNode n) {
		if (DEBUG) {
			System.out.println("entering " + n.getName());
		}
		Scope scope;
		if (n.hasName("ClassDeclaration")) {
			scope = new ClassScope(n, currentScope);
		}
		else if (n.hasName("ConstructorDeclaration")) {
            // NOTE: this uses the idea that when you enter a construcor's scope, you are
            // always currently in that constructor's class scope
            if (currentScope instanceof ClassScope) {
                scope = new ConstructorScope(n, (ClassScope) currentScope);
            }
            else {

                scope = new ConstructorScope(n);
            }
        }
		else if (n.hasName("MethodDeclaration")) {
			// NOTE: this uses the idea that when you enter a method's scope, you are
			// always currently in that method's class scope
			if (currentScope instanceof ClassScope) {
                scope = new MethodScope(n, (ClassScope) currentScope);
			}
			else {
				scope = new MethodScope(n);
			}
		}
		else {
			scope = new Scope(n, currentScope);
		}
		n.setProperty(Constants.SCOPE, scope);
		currentScope.addScope(scope);
		currentScope = scope;
	}

	public void enterNewMethodScope(GNode n, boolean isPrivate, boolean isStatic, String name, String returnType) {
		if (DEBUG) {
			System.out.println("entering " + n.getName());
		}
		Scope scope;
		if (n.hasName("MethodDeclaration")) {
			// NOTE: this uses the idea that when you enter a method's scope, you are
			// always currently in that method's class scope
			if (currentScope instanceof ClassScope) {
	            scope = new MethodScope(n, (ClassScope) currentScope, isPrivate, isStatic, name, returnType);
			}
			else {
				scope = new MethodScope(n);
			}

			n.setProperty(Constants.SCOPE, scope);
			currentScope.addScope(scope);
			currentScope = scope;
		}
	}

    public void addSymbolToCurrentScope(String name, String type) {
        Symbol symbol = new Symbol(name, type);
        currentScope.addSymbol(symbol);
    }

	public void addSymbolToCurrentScope(String name, String type, boolean isPrivate, boolean isStatic) {
		Symbol symbol = new Symbol(name, type, isPrivate, isStatic);
		currentScope.addSymbol(symbol);
	}

	public void addParameterToCurrentScope(String name, String type, boolean isPrivate, boolean isStatic) {
		if (currentScope instanceof MethodScope) {
			((MethodScope) currentScope).addParameter(type);
		}
		else if (currentScope instanceof ConstructorScope) {
			((ConstructorScope) currentScope).addParameter(type);
		}
		addSymbolToCurrentScope(name, type, isPrivate, isStatic);
	}

	public void addParameterToCurrentScope(String name, String type) {
		if (currentScope instanceof MethodScope) {
			((MethodScope) currentScope).addParameter(type);
		}
		else if (currentScope instanceof ConstructorScope) {
			((ConstructorScope) currentScope).addParameter(type);
		}
		addSymbolToCurrentScope(name, type);
	}

	public void exitCurrentScope() {
		if (root != currentScope) {
			if (DEBUG) {
				System.out.println("exiting " + currentScope.associatedNode.getName());
			}
			currentScope = currentScope.parent;
		}
	}

	public boolean exitScopeForNode(GNode n) {
		// returns false if node doesn't have an associated scope
		// or that associated scope isn't the current scope
		// (or if we're currently in the root scope)
		if (n.hasProperty(Constants.SCOPE)) {
			Object scopeProperty = n.getProperty(Constants.SCOPE);
			if (scopeProperty instanceof Scope) {
				if (currentScope == (Scope) scopeProperty) {
					exitCurrentScope();
					return true;
				}
			}
		}
		return false;
	}

	public boolean enterScopeForNode(GNode n) {
		// returns false if node doesn't have an associated scope
		if (n.hasProperty(Constants.SCOPE)) {
			Object scopeProperty = n.getProperty(Constants.SCOPE);
			if (scopeProperty instanceof Scope) {
				currentScope = (Scope) scopeProperty;
				return true;
			}
		}
		return false;
	}

	// if false, need to use "this->"
	public boolean symbolExistsBelowClassScope(String name) {
		Scope scope = currentScope;
		while (scope != null && ! (scope instanceof ClassScope)) {
			if (scope.hasSymbol(name)) {
				return true;
			}
			scope = scope.parent;
		}
		return false;
	}

	public String typeOfSymbol(String name) {
		Scope scope = currentScope;
		while (scope != null) {
			if (scope.hasSymbol(name)) {
				return scope.getSymbolType(name);
			}
			scope = scope.parent;
		}
		return null;
	}

	public Scope getCurrentScope() {
		return currentScope;
	}

	public String toString() {
		String string = "root";
		Collection<Scope> scopes = root.getAllScopes();
		string += "\n" + "  nested scopes:";
		for (Scope scope : scopes) {
			string += "\n" + scope.toString("   ");
		}
		return string;
	}

}