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

	public ??? resolveMethodCall(String name, List<String> arguments, String calledOn) {
		List<String> argTypes = getArgumentTypes(arguments);
		String callerClass = table.getSymbolType(calledOn);
		??? ??? = getAccessibleOverloadedMethodsInClass(name, callerClass);

	}

	public ??? resolveMethodCall(String name, List<String> arguments) {
		// assumptions:
		// - the first "parent" MethodScope from the currentScope in the symbol table is the
		//   method we are in right now (same/similar goes for ClassScope)
		List<String> argTypes = getArgumentTypes(arguments);
		??? ??? = getAccessibleOverloadedMethodsInCurrentScope(name);
	}

	// looks for all methods with this name that are accessible from an instance of the given class
	public ??? getAccessibleOverloadedMethodsInClass(String name, String class) {

	}

	// looks for all methods with this name that are accessible from the current scope (aka current class)
	public ??? getAccessibleOverloadedMethodsInCurrentScope(String name) {

	}

	public List<String> getArgumentTypes(List<String> arguments) {
		List<String> types = new ArrayList<String>();
		for (String name : arguments) {
			String type = table.typeOfSymbol(name);
			types.add(type);
		}
		return types;
	}




}