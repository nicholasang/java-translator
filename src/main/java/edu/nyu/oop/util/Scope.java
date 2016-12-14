// import java.util.*;




// public class Scope {
// 	String name
// 	Scope parent;
// 	Map<String, Scope> nestedScopes;

// }

// public static class Scope {
// 		String name;
// 		Scope parent;
// 		Map<String, Scope> nestedScopes;
// 		Map<String, Symbol> symbols;
// 		Symbol associatedSymbol; // only not null if a method
// 		Map<String, List<Symbol>> overloadedMethodsStagedForMangling;

// 		public Scope(String name) {
// 			this(name, null);
// 		}

// 		public Scope(String name, Scope parent) {
// 			this(name, parent, null);
// 		}

// 		public Scope(String name, Scope parent, Symbol symbol) {
// 			this.name = name;
// 			this.parent = parent;
// 			this.childScopes = new HashMap<String, Scope>();
// 			this.symbols = new HashMap<String, Scope>();
// 			this.associatedSymbol = symbol;
// 			this.overloadedMethodsStagedForMangling = new HashMap<String, List<Symbol>>();
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