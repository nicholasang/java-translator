// import java.util.*;

// public class SymbolTable {

// 	public static class Scope {
// 		String name;
// 		String mangledName;
// 		Node node;
// 		Scope parent;
// 		Map<String, Scope> childScopes;
// 		Map<String, Symbol> symbols;
// 		Symbol associatedSymbol; // only not null if a method
// 		// Map<String, List<Scope>> overloadedMethodsStagedForMangling;
// 		Map<String, List<Scopes> overloadedMethods;

// 		public Scope(String name, Node node) {
// 			this(name, node, null);
// 		}

// 		public Scope(String name, Node node, Scope parent) {
// 			this(name, node, parent, null);
// 		}

// 		public Scope(String name, Node node, Scope parent, Symbol symbol) {
// 			this.name = name;
// 			this.mangledName = name;
// 			this.node = node;
// 			this.parent = parent;
// 			this.childScopes = new HashMap<String, Scope>();
// 			this.symbols = new HashMap<String, Scope>();
// 			this.associatedSymbol = symbol;
// 			this.overloadedMethods = new Map<String, List<Scope>>();
// 		}

// 		public boolean hasSymbol(String name) {
// 			return symbols.containsKey(name);
// 		}

// 		public boolean hasSymbol(Symbol symbol) {
// 			return symbols.containsValue(symbol);
// 		}

// 		public boolean hasChildScope(String name) {
// 			return childScopes.containsKey(name);
// 		}

// 		public boolean hasChildScope(Scope scope) {
// 			return childScopes.containsValue(scope);
// 		}

// 		public boolean hasChildScopes() {
// 			return (childScopes.size() == 0);
// 		}

// 		public void addChildScope(Scope child) {
// 			childScopes.put(child.name, child);
// 		}

// 		public boolean addSymbol(Symbol symbol) {
// 			symbols.put(symbol.name, symbol);
// 		}
// 	}

// 	public static class Symbol {
// 		String name;
// 		String mangledName;
// 		SymbolType symbolType;
// 		Scope associatedScope; // applies to methods only
// 		List<Symbol> parameters; // applies to methods only
// 		String type; // applies to fields only

// 		enum SymbolType {
// 			FIELD,
// 			METHOD
// 		}

// 		private Symbol(String name, Scope scope) {
// 			this.name = name;
// 			this.mangledName = name;
// 			this.symbolType = METHOD;
// 			this.associatedScope = scope;
// 			scope.associatedSymbol = this;
// 		}

// 		private Symbol(String name, String type) {
// 			this.name = name;
// 			this.mangledName = name;
// 			this.symbolType = FIELD;
// 			this.type = type;
// 		}

// 		public boolean addParam(String name, String type) {
// 			if (this.symbolType != METHOD) {
// 				return false;
// 			}
// 			Symbol newParam = new Symbol(name, type);
// 			this.parameters.add(newParam);
// 			return true;
// 		}

// 		public boolean isField() {
// 			if (this.symbolType == FIELD) {
// 				return true;
// 			}
// 			return false;
// 		}

// 		public boolean isMethod() {
// 			if (this.symbolType == METHOD) {
// 				return true;
// 			}
// 			return false;
// 		}
// 	}

// 	private Scope rootScope;
// 	private Scope currentScope;

// 	public SymbolTable() {
// 		this("");
// 	}

// 	public SymbolTable(String root) {
// 		this.rootScope = new Scope(root);
// 		currentScope = rootScope;
// 	}

// 	public Scope getRoot() {
// 		return rootScope;
// 	}

// 	public Scope getCurrentScope() {
// 		return currentScope;
// 	}

// 	// methods for creating/modifying the symbol table

// 	public Scope enterNewScope(String name, Node node) {
// 		Scope newScope = createNewScope(name, node);
// 		currentScope = newScope;
// 	}

// 	public Scope createNewScope(String name, Node node) {
// 		Scope newScope = new Scope(name, node, currentScope);
// 		currentScope.childScopes.put(name, newScope);
// 	}

// 	public boolean createNewField(String name, String type) {
// 		if (currentScopeHasField(name)) {
// 			return false;
// 		}
// 		currentScope.symbols.put(name, new Symbol(name, type));
// 		return true;
// 	}

// 	public void createNewMethod(String name, Node node) {
// 		if (methodIsOverload(name)) {
// 			for (Symbol param : method.parameters) {
// 				name += param.type;
// 			}
// 		}

// 		Scope methodScope = new Scope(name, node, currentScope);
// 		Symbol methodSymbol = new Symbol(name, methodScope);
// 		methodScope.associatedSymbol = methodSymbol;

// 		// Symbol newMethod = new Symbol(name, scope);
// 		Symbol oldMethod = currentScope.symbols.put(name, newMethod);
// 		currentScope.childScopes.put(name, newMethod.associatedScope);

// 		if (oldMethod != null) {
// 			if (currentScope.overloadedMethodsStagedForMangling.containsKey(name)) {
// 				List<Symbol> overloadedMethods = currentScope.overloadedMethodsStagedForMangling.get(name);
// 				overloadedMethods.add(newMethod);
// 			}
// 			else {
// 				List<Symbol> overloadedMethods = new ArrayList<Symbol>();
// 				overloadedMethods.add(oldMethod);
// 				overloadedMethods.add(newMethod);
// 				currentScope.overloadedMethodsStagedForMangling.put(name, overloadedMethods);
// 			}
// 		}
// 	}

// 	public void createNewMethod(String name) {
// 		Scope newScope = createNewScope(name, currentScope);
// 		createNewMethod(name, newScope);
// 	}

// 	// public void mangleMethodNames() {
// 	// 	mangleMethodNamesForScope(currentScope);
// 	// }

// 	// public void mangleMethodNamesForScope(Scope scope) {
// 	// 	Map<String, List<Symbol>> overloaded = scope.overloadedMethodsStagedForMangling;
// 	// 	for (String methodName : overloaded.keySet()) {
// 	// 		scope.symbols.remove(methodName);
// 	// 		scope.childScopes.remove(methodName);

// 	// 		List<Symbol> methods = overloaded.get(methodName);
// 	// 		for (Symbol method : methods) {
// 	// 			String newName = method.name;
// 	// 			for (Symbol param : method.parameters) {
// 	// 				newName += param.type;
// 	// 			}
// 	// 			method.name = newName;
// 	// 			method.associatedScope.name = newName;

// 	// 			scope.symbols.add(newName, method);
// 	// 			scope.childScopes.add(newName, method.associatedScope);
// 	// 		}
// 	// 	}
// 	// }

// 	// methods for traversing the symbol table (not modifying)

// 	public boolean atRoot() {
// 		return currentScope.parent == null;
// 	}

// 	public boolean exitCurrentScope() {
// 		if (atRoot()) {
// 			return false;
// 		}
// 		currentScope = currentScope.parent;
// 		return true;
// 	}

// 	public boolean hasChildScope(String name) {
// 		return currentScope.childScopes.containsKey(name);
// 	}

// 	public boolean currentScopeHasSymbol(String name) {
// 		return currentScope.symbols.containsKey(name);
// 	}

// 	public boolean currentScopeHasFieldSymbol(String name) {
// 		if (currentScope.symbols.containsKey(name)) {
// 			if (currentScope.symbols.get(name).isField()) {
// 				return true;
// 			}
// 		}
// 		return false;
// 	}

// 	public boolean currentScopeHasMethodSymbol(String name) {
// 		if (currentScope.symbols.containsKey(name)) {
// 			if (currentScope.symbols.get(name).isMethod()) {
// 				return true;
// 			}
// 		}
// 		return false;
// 	}

// 	public boolean currentScopeHasChildScope(String name) {
// 		return currentScope.childScopes.containsKey(name);
// 	}

// 	public boolean methodIsOverload(String name) {
// 		if (currentScopeHasChildScope(name)) {
// 			return true;
// 		}
// 		else if (currentScope.overloadedMethods.containsKey(name)) {
// 			return true;
// 		}
// 		return false;
// 	}

// 	public void addOverloadedMethod(Scope method) {
// 		if (currentScope.overloadedMethods.containsKey(method.name)) {
// 			List<Scope> methods = currentScope.overloadedMethods.get(name);
// 			methods.add(method);
// 		}
// 		else {
// 			List<Scope> methods = new List<Scope>();
// 			methods.add(method);
// 			currentScope.overloadedMethods.put(method.name, methods);
// 		}
// 	}

// 	// public boolean haveOverloadedMethods(String name) {
// 	// 	return currentScope.overloadedMethodsStagedForMangling.containsKey(name);
// 	// }

// }