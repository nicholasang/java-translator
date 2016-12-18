package edu.nyu.oop;


public class MethodOverloadResolver {
	private SymbolTable table;

	public MethodOverloadResolver(SymbolTable table) {
		this.table = table;
	}

	// need to return:
	//	(mangled) method name to use when invoking
	//	whether to use __vptr or not (is static or private?)
	// takes in:
	//	current scope in some form (already updating ST? pass in associatedNode?)
	//	names of all the arguments used
	// 	name of method called
	//	name of object that method is called on (if it's called on an object)
	// inside:
	//	look for symbols in this scope, and then up the hierarchy of parent scopes
	//	now, have types for each symbol used (in a list)
	// 	look for all methods with this name that are ACCESSIBLE -> list
	//		if called on object, look at that object's methods / inherited methods
	//		if just called, look at current class's methods / inherited methods
	//	filter list by arity (# of args)
	//	look for method by type:
	//		if there is an exact match, use that!
	//		else:
	//			compute "distance" for each method from the actual args
	//				1 for each "step" up to a superclass
	//				auto-boxing/unboxing comes after all superclass / "widening" of primitives
	//			choose the method that is the "smallest distance" from the actual arguments

	// from an Arguments GNode (and using the symbol table),
	// return an (in order) list of the static types of the arguments
	// that are passed into a method
	public List<String> getArgumentTypeList(GNode argNode) {
		// NewClassExpression
		// CastExpression
		// PrimaryIdentifier -> symbolTable
		// FloatingPointLiteral
		// IntegerLiteral
		// AdditiveExpression -> resolve (type1 + type2) (byte + byte == int!!)

		// -> List<String> where each String is a type (aka class or primitive)
	}

	public _____ resolve(String name, String calledOn, List<String> argTypes, Scope currentScope) {
		// inside CallExpression:
		//	calledOn = string inside PrimaryIdentifier (n.getNode(0).getString(0))
		//		if it's not called on anything, that node is null (n.get(0) == null)
		//	name = string at index 2 (n.getString(2))
		//	argTypes = List<String> (each string is type)
		//	currentScope = table.currentScope


	}


	public ??? resolveMethodCall(String name, List<String> arguments, String calledOn) {
		List<String> argTypes = getArgumentTypes(arguments);
		String callerClass = table.getSymbolType(calledOn);
		??? ??? = getAccessibleOverloadedMethodsInClass(name, callerClass);

		// DIFFERENTIATE:
		// calledOn is an object -> instance
		// calledOn is a class -> static

		// filter by arity (# of args)
		// find "closest" match
	}

	public ??? resolveMethodCall(String name, List<String> arguments) {
		// assumptions:
		// - the first "parent" MethodScope from the currentScope in the symbol table is the
		//   method we are in right now (same/similar goes for ClassScope)
		List<String> argTypes = getArgumentTypes(arguments);
		??? ??? = getAccessibleOverloadedMethodsInCurrentScope(name);

		// may be instance OR static method (within this class)

		// filter by arity (# of args)
		// find "closest" match
	}

	// looks for all methods with this name that are accessible from an instance of the given class
	public ??? getAccessibleOverloadedMethodsInClass(String name, String class) {

	}

	// looks for all methods with this name that are accessible from the current scope (aka current class)
	public ??? getAccessibleOverloadedMethodsInCurrentScope(String name) {

	}

	public List<MethodScope> getAccessibleOverloadedMethodsInScope(String name, Scope scope, , boolean static) {
		ClassScope class = findEnclosingClassScope(scope);

		Set<MethodScope> methodScopes = getAllOverloadedMethods(name, class);
		for (MethodScope methods : methodScopes) {
			if (static) {
				if (! methodIsAccessibleStatic(method, scope)) {
					methodScopes.remove(method);
				}
			}
			else {
				if (! methodIsAccessibleInstance(method, scope)) {
					methodScopes.remove(method);
				}
			}
		}

	}

	// called statically (no instance methods)
	public boolean methodIsAccessibleStatic(MethodScope method, Scope fromScope, ClassScope calledOn) {
		// if method is static
			// if method is defined in the class it's called on:
				// if not private
					// can be used
				// if private
					// cannot be used
			// if method is called inside the class it is defined in:
			   // ()

		// else:
			// cannot be used


		if (method.classScope == findEnclosingClassScope(fromScope)) {
			if ()
			// if method is private, can access
			// if method is static, can access
			// cannot access any instance (MUST BE STATIC)
		}
		else {
			// cannot access any instance
			// cannot access private
			// cannot access public static!
		}
	}

	// called on an object (no static methods)
	public boolean methodIsAccessibleInstance(MethodScope method, Scope fromScope, String calledOn) {
		if (method.classScope == findEnclosingClassScope(fromScope)) {
			// if private, can access
			// if static, cannot access
		}
		else {
			// CAN ACCESS INSTANCE
			// cannot access private
			// cannot access static
		}
	}

	public Set<MethodScope> getAllOverloadedMethods(String name, ClassScope scope) {
		Set<MethodScope> methods = new HashSet<MethodScope>();
		if (scope.hasMethod(name)) {
			methods.addAll(scope.getMethods(name));
		}
		ClassScope superClass = scope.getSuperClassScope();
		methods.addAll( getAllOverloadedMethods(name, superClass) );
		return methods;
	}


	public ClassScope findEnclosingClassScope(Scope scope) {
		while (! scope instanceof ClassScope && scope != null) {
			scope = scope.parent;
		}
		return scope;
	}

	public List<String> getArgumentTypes(List<String> arguments) {
		List<String> types = new ArrayList<String>();
		for (String name : arguments) {
			String type = table.typeOfSymbol(name);
			types.add(type);
		}
		return types;
	}

	public String convertType(String type) {
		String cType;
        switch (type) {
        case "long":
            cType = "int64_t";
            break;
        case "int":
            cType = "int32_t";
            break;
        case "short":
            cType = "int16_t";
            break;
        case "byte":
            cType = "int8_t";
            break;
        case "boolean":
            cType = "bool";
            break;
        default:
            cType = javaType;
            break;
        }

        return cType;
	}

	public boolean isPrimitive(String type) {
		// returns whether the type is a (Java) primitive or not
		boolean isPrimitive = false;
        switch (javaType) {
	        case "long":
	        case "int":
	        case "short":
	        case "char":
	        case "byte":
	        case "float":
	        case "double":
	        case "boolean":
	            isPrimitive = true;
        }
        return isPrimitive;
	}


}