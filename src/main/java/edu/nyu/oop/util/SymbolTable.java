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

	public String toString() {
		String string = "root";
		Collection<Scope> scopes = root.getAllScopes();
		string += "\n" + "  nested scopes:";
		for (Scope scope : scopes) {
			string += "\n" + scope.toString("   ");
		}
		return string;
	}

	private static class Scope {
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

	private static class ClassScope extends Scope {
		static Map< String, ClassScope > classScopes = new HashMap< String, ClassScope >();
		static {
			ClassScope object = new ClassScope();
			object.name = "Object";
			// TODO: add methods and stuff (?)
			classScopes.put("Object", object);
		}

		String superClass;
		String name;
		private Map< String, Set< Scope > > methods;
        private List<ConstructorScope> constructors;

		public ClassScope() {
			super();
			this.methods = new HashMap<String, Set<Scope>>();
			this.constructors = new ArrayList<ConstructorScope>();
		}

		public ClassScope(GNode n) {
			this(n, null);
		}

		public ClassScope(GNode n, Scope parent) {
			super(n, parent);
			this.methods = new HashMap<String, Set<Scope>>();
			this.constructors = new ArrayList<ConstructorScope>();

			this.name = n.getString(1);
			classScopes.put(this.name, this);

			if (n.get(3) != null) {
				this.superClass = n.getNode(3).getNode(0).getNode(0).getString(0);
			}
			else {
				this.superClass = "Object";
			}
		}

		public String toString(String indent) {
			String string = indent + "Class " + this.name;
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

		public ClassScope getSuperClassScope() {
			if (this.superClass == "Object") {
				// TODO: Change this....?
				return classScopes.get("Object");
			}
			else {
				return classScopes.get(this.superClass);
			}
		}

		public boolean hasOverloadedMethods(String name) {
			if (methods.containsKey(name)) {
				if (methods.get(name).size() > 1) {
					return true;
				}
			}
			return false;
		}

		public boolean hasMethod(String name) {
			return methods.containsKey(name);
		}

		public Set<Scope> getMethods(String name) {
			return methods.get(name);
		}

		public void addMethod(String name, Scope method) {
			if (methods.containsKey(name)) {
				methods.get(name).add(method);
			}
			else {
				Set<Scope> set = new HashSet<Scope>();
				set.add(method);
				methods.put(name, set);
			}
		}

		public void addConstructor(ConstructorScope constructor) {
            constructors.add(constructor);
        }

        public List<ConstructorScope> getConstructors() {
            return constructors;
        }
	}

	private static class MethodScope extends Scope {
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

    private static class ConstructorScope extends Scope {
        ClassScope classScope;
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

    }

	private static class Symbol {
		String name;
		String uniqueName; // ? - in case of class with same name field + method
		String type;
        boolean isPrivate;
        boolean isStatic;

		public Symbol(String name, String type) {
			this.name = name;
			this.type = type;
			this.isPrivate = false;
            this.isStatic = false;
		}

        public Symbol(String name, String type, boolean isPrivate, boolean isStatic) {
            this.name = name;
            this.type = type;
            this.isPrivate = isPrivate;
            this.isStatic = isStatic;
        }
	}

}