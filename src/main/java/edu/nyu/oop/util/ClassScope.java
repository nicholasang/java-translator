package edu.nyu.oop.util;

import java.util.*;

import xtc.Constants;
import xtc.tree.GNode;


public class ClassScope extends Scope {
	public static Map< String, ClassScope > classScopes = new HashMap< String, ClassScope >();
	static {
		ClassScope object = new ClassScope();
		object.name = "Object";
		object.superClass = null;
		// TODO: add methods and stuff (?)
		classScopes.put("Object", object);
	}

	public String superClass;
	public String name;
	private Map< String, Set< MethodScope > > methods;
    private List<ConstructorScope> constructors;
    public boolean isMainMethodClass;

	public ClassScope() {
		super();
		this.methods = new HashMap<String, Set<MethodScope>>();
		this.constructors = new ArrayList<ConstructorScope>();
	}

	public ClassScope(GNode n) {
		this(n, null);
	}

	public ClassScope(GNode n, Scope parent) {
		super(n, parent);
		this.methods = new HashMap<String, Set<MethodScope>>();
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

	public Set<MethodScope> getMethods(String name) {
		return methods.get(name);
	}

	public void addMethod(String name, MethodScope method) {
		if (methods.containsKey(name)) {
			methods.get(name).add(method);
		}
		else {
			Set<MethodScope> set = new HashSet<MethodScope>();
			set.add(method);
			methods.put(name, set);
			if (name.equals("main")) {
				isMainMethodClass = true;
			}
		}
	}

	public void addConstructor(ConstructorScope constructor) {
        constructors.add(constructor);
    }

    public List<ConstructorScope> getConstructors() {
        return constructors;
    }
}