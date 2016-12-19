package edu.nyu.oop;


import edu.nyu.oop.util.ClassScope;
import edu.nyu.oop.util.MethodScope;
import edu.nyu.oop.util.Scope;
import edu.nyu.oop.util.SymbolTable;
import xtc.tree.Node;

import java.util.*;

/*
to use:
1: create a new MethodOverloadResolver and pass in an already-built SymbolTable
2: whenever you come accross a CallExpression (except for those that are for System.out.println)
   call resolve(callExpressionNode).
   - this will return a String, which is the correct mangled name to use in place of the
     original method name
   - note: if it turns out we need some other info about the method (for some reason),
     lemme know and I can easily change this to return other things
3: you must "keep track" of the scope using the SymbolTable in your phase 4 visitor:
   - at the start of *every* visit method, call: table.enterNodeForScope(node)
   - at the end, call: table.exitNodeForScope(node)
   - you may get access to the SymbolTable by getting SymbolTable.table (a public static field
     that'll store the symbol table so any class can access it)
*/

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

	// from an Arguments Node (and using the symbol table),
	// return an (in order) list of the static types of the arguments
	// that are passed into a method
	public List<String> getArgumentTypeList(Node argNode) {
		// NOTE: this doesn't work for arrays as arguments
		// but none of the test cases have this so it's okay

		// AdditiveExpression -> resolve (type1 + type2) (byte + byte == int!!)
		List<String> argTypes = new ArrayList<String>();

		int numArgs = argNode.size();
		for (int i = 0; i < numArgs; i++) {
			Node arg = argNode.getNode(i);
			String type = getArgType(arg);
			argTypes.add(type);
		}
		return argTypes;
	}

	public String getArgType(Node arg) {
		// TODO: handle a CallExpressino as an argument

		String type = "";
		switch (arg.getName()) {
			case "NewClassExpression":
				type = arg.getNode(2).getString(0);
				break;
			case "CastExpression":
				type = arg.getNode(0).getNode(0).getString(0);
				break;
			case "PrimaryIdentifier":
				String name = arg.getString(0);
				type = table.typeOfSymbol(name);
				break;
			case "FloatingPointLiteral":
				type = "double";
				break;
			case "IntegerLiteral":
				type = "int";
				break;
			case "CharacterLiteral":
				type = "char";
				break;
			case "StringLiteral":
				type = "String";
				break;
			case "AdditiveExpression":
				String type1 = getArgType(arg.getNode(0));
				String type2 = getArgType(arg.getNode(2));
				if (type1.equals("double") || type2.equals("double")) {
					type = "double";
				}
				else if (type1.equals("float") || type2.equals("float")) {
					type = "float";
				}
				else if (type1.equals("long") || type2.equals("long")) {
					type = "long";
				}
				else {
					type = "int";
				}
				break;
		}
		return type;
	}

	public String resolveCall(Node callExpression) {
		return resolve(callExpression).getMangledName();
	}

	public MethodScope resolve(Node callExpression) {
		String name = callExpression.getString(2);
		Object calledOnObject = callExpression.get(0);
		Node calledOn;
		if (calledOnObject instanceof Node) {
			calledOn = (Node) calledOnObject;
		}
		else {
			calledOn = null;
		}
		Node argNode = callExpression.getNode(3);
		Scope currentScope = table.getCurrentScope();

		MethodScope methodScope = resolve(name, calledOn, argNode, currentScope);
		// System.out.println(methodScope.name);
		return methodScope;
	}

	public MethodScope resolve(String name, Node calledOn, Node argNode, Scope currentScope) {

		List<String> argTypes = getArgumentTypeList(argNode);

		List<MethodScope> accessibleMethods;
		ClassScope classScope = findEnclosingClassScope(currentScope);
		if (calledOn == null) {
			accessibleMethods = calledOnNull(name, classScope);
		}
		else if (calledOn.getName().equals("CallExpression")) {
			MethodScope previousCall = resolve(calledOn);
			String calledOnName = previousCall.returnType;
			String className = table.typeOfSymbol(calledOnName);
			classScope = ClassScope.classScopes.get(className);
			accessibleMethods = calledOnObject(name, classScope);
		}
		else if (calledOn.getName().equals("ThisExpression")) {
			accessibleMethods = calledOnThis(name, classScope);
		}
		else if (calledOn.getName().equals("CastExpression")) { // note: takes precendence over new class expression
			// get name of class from casting
			String className = calledOn.getNode(0).getNode(0).getString(0);
			classScope = ClassScope.classScopes.get(className);
			accessibleMethods = calledOnObject(name, classScope);
		}
		else if (calledOn.getName().equals("NewClassExpression")) {
			// get name of class (from name of constructor)
			String className = calledOn.getNode(2).getString(0);
			classScope = ClassScope.classScopes.get(className);
			accessibleMethods = calledOnObject(name, classScope);
		}
		else { // calledOn is PrimaryIdentifier
			String calledOnName = calledOn.getString(0);
			if (ClassScope.classScopes.containsKey(calledOnName)) {
				classScope = ClassScope.classScopes.get(calledOnName);
				accessibleMethods = calledOnClass(name, classScope);
			}
			else {
				if (table.symbolExistsBelowClassScope(calledOnName)) {
					// called on a var within a method
					String className = table.typeOfSymbol(calledOnName);
					classScope = ClassScope.classScopes.get(className);
					accessibleMethods = calledOnObject(name, classScope);
				}
				else { // is class var, so same as using "this.var"
					accessibleMethods = calledOnThis(name, classScope);
				}
			}
		}

		List<MethodScope> correctArityMethods = filterByArity(accessibleMethods, argTypes.size());

		Map< Integer, MethodScope> distances = computeDistances(correctArityMethods, argTypes);

		Integer min  = distances.keySet().iterator().next();
		for (Integer num : distances.keySet()) {
			if (num < min) {
				min = num;
			}
		}
		MethodScope closestMethod = distances.get(min);

		// tada!

		return closestMethod;
	}



		// this class instance public
		// this class instance private
		// this class static public
		// this class static private
		// superclass instance public

		// if calledOn == null:
			// searchUp = current class (scope)
			// accessPrivate = true;
			// searchStatic = true; <- no superclasses
			// searchInstance = true;
			// search:
				// current class's static methods
				// current class instance methods (all)
				// superclasss' public instance methods
		// if calledOn == ThisExpression:
			// searchUp = current class (scope)
			// accessPrivate = true;
			// searchStatic = false;
			// searchInstance = true;
			// search:
				// current class's instance methods (all)
				// superclasss' public instance methods
		// elif calledOn == a class name
			// searchUp = that class name
			// don't search superclasses!
			// accessPrivate = false
			// search:
				// class's public static methods
		// elif calledOn == a variable name
			// searchUp = that object's class
			// accessPrivate = false
			// search:
				// class's public instance methods
				// superclasss' public instance methods


	public List<MethodScope> calledOnNull(String name, ClassScope classScope) {
		// search:	class static methods (all)
		//			class instance methods (all)
		//			superclass public instance methods
		List<MethodScope> methods = getAllOverloadedMethods(name, classScope);
		List<MethodScope> accessibleMethods = new ArrayList<MethodScope>();
		for (MethodScope method : methods) {
			if (method.classScope == classScope) {
				accessibleMethods.add(method);
			}
			else {
				if (!method.isStatic && !method.isPrivate) {
					accessibleMethods.add(method);
				}
			}
		}
		return accessibleMethods;
	}

	public List<MethodScope> calledOnThis(String name, ClassScope classScope) {
		// search:	class instance methods (all)
		//			superclass public instance methods
		List<MethodScope> methods = getAllOverloadedMethods(name, classScope);
		List<MethodScope> accessibleMethods = new ArrayList<MethodScope>();
		for (MethodScope method : methods) {
			if (method.classScope == classScope) {
				if (!method.isStatic) {
					accessibleMethods.add(method);
				}
			}
			else {
				if (!method.isStatic && !method.isPrivate) {
					accessibleMethods.add(method);
				}
			}
		}
		return accessibleMethods;
	}

	public List<MethodScope> calledOnClass(String name, ClassScope classScope) {
		// search:	class public static methods
		List<MethodScope> methods = getAllOverloadedMethods(name, classScope);
		List<MethodScope> accessibleMethods = new ArrayList<MethodScope>();
		for (MethodScope method : methods) {
			if (method.classScope == classScope) {
				if (method.isStatic && !method.isPrivate) {
					accessibleMethods.add(method);
				}
			}
			else {
				if (method.isStatic && !method.isPrivate) {
					accessibleMethods.add(method);
				}
			}
		}
		return accessibleMethods;
	}

	public List<MethodScope> calledOnObject(String name, ClassScope classScope) {
		// search:	class public instance methods
		//			superclass public instance methods
		List<MethodScope> methods = getAllOverloadedMethods(name, classScope);
		List<MethodScope> accessibleMethods = new ArrayList<MethodScope>();
		for (MethodScope method : methods) {
			if (method.classScope == classScope) {
				if (!method.isStatic && !method.isPrivate) {
					accessibleMethods.add(method);
				}
			}
			else {
				if (!method.isStatic && !method.isPrivate) {
					accessibleMethods.add(method);
				}
			}
		}
		return accessibleMethods;
	}

	public List<MethodScope> filterByArity(List<MethodScope> methods, int numArgs) {
		List<MethodScope> correctArity = new ArrayList<MethodScope>();
		for (MethodScope method : methods) {
			if (method.getParameterTypes().size() == numArgs) {
				correctArity.add(method);
			}
		}
		return correctArity;
	}

	public Map<Integer, MethodScope> computeDistances(List<MethodScope> methods, List<String> argTypes) {
		Map<Integer, MethodScope> distances = new HashMap<Integer, MethodScope>();
		for (MethodScope method : methods) {
			List<String> paramTypes = method.getParameterTypes();
			int distance = 0;
			boolean addToMap = true;
			for (int i = 0; i < argTypes.size(); i++) {
				int typeDist = computeDistance(argTypes.get(i), paramTypes.get(i));
				if (typeDist >= 0) {
					distance += typeDist;
				}
				else { // if distance is negative, means the two types are incompatible
					   // so this method can't be used
					addToMap = false;
					break;
				}
			}
			if (addToMap) {
				distances.put(distance, method);
			}
		}

		return distances;
	}

	// returns -1 if the distane is infinite aka paramType is not a supertype of argType
	public int computeDistance(String argType, String paramType) {
		if (isPrimitive(argType)) { // remember: no auto-boxing

			// some fancy use of switch statements below - here's the basic outline of primitive type "widening":
			// byte/char -> short -> int -> long -> float -> double
			// and boolean is on it's own
			int counter = 0;
	        switch (argType) {
	        	case "char":
		        case "byte":
			        if (paramType.equals("char") || paramType.equals("byte")) {
	        			break;
	        		}
	        		counter++;
		        case "short":
		            if (paramType.equals("short")) {
	        			break;
	        		}
	        		counter++;
		        case "int":
		            if (paramType.equals("int")) {
	        			break;
	        		}
	        		counter++;
		        case "long":
		            if (paramType.equals("long")) {
	        			break;
	        		}
	        		counter++;
		        case "float":
		        	if (paramType.equals("float")) {
	        			break;
	        		}
	        		counter++;
		        case "double":
		        	if (paramType.equals("double")) {
	        			break;
	        		}
	        		counter = -1;
			        break;
		        case "boolean":
			        if (paramType.equals("boolean")) {
	        			break;
	        		}
	        		counter = -1;
		            break;
	        }
	        return counter;
		}
		else {
			// look at argType's entire inheritance hierarchy
			// find where (+ if) paramType is in there
			// # of steps up == distance
			int counter = 0;
			String type = argType;
			while (type != null && (!type.equals(paramType))) {
				counter ++;
				type = superclass(type);
			}
			if (type == null) {
				return -1;
			}
			else {
				return counter;
			}

		}

	}

	public String superclass(String className) {
		ClassScope classScope = ClassScope.classScopes.get(className);
		return classScope.superClass;
	}

	public List<MethodScope> getAllOverloadedMethods(String name, ClassScope scope) {
		return getAllOverloadedMethods(name, scope, new ArrayList<MethodScope>());
	}

	public List<MethodScope> getAllOverloadedMethods(String name, ClassScope scope, List<MethodScope> methods) {
		if (scope.hasMethod(name)) {
			Set<MethodScope> scopeMethods = scope.getMethods(name);
			for (MethodScope newMethod : scopeMethods) {
				if (! hasBeenOverriden(newMethod, methods)) {
					methods.add(newMethod);
				}
			}
		}

		ClassScope superClass = scope.getSuperClassScope();
		if (superClass != null) {
			methods = getAllOverloadedMethods(name, superClass, methods);
		}

		return methods;
	}

	public boolean hasBeenOverriden(MethodScope newMethod, List<MethodScope> methods) {
		for (MethodScope method : methods) {
			if (haveSameParams(method, newMethod)) {
				return true;
			}
		}
		return false;
	}

	public boolean haveSameParams(MethodScope one, MethodScope two) {
		List<String> paramList1 = one.getParameterTypes();
		List<String> paramList2 = two.getParameterTypes();

		if (paramList1.size() != paramList2.size()) {
			return false;
		}
		for (int i = 0; i < paramList1.size(); i++) {
			if (! paramList1.get(i).equals(paramList2.get(i))) {
				return false;
			}
		}
		return true;
	}

	public ClassScope findEnclosingClassScope(Scope scope) {
		while (scope != null && (!(scope instanceof ClassScope))) {
			scope = scope.parent;
		}
		return (ClassScope) scope;
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
            cType = type;
            break;
        }

        return cType;
	}

	public boolean isPrimitive(String type) {
		// returns whether the type is a (Java) primitive or not
		boolean isPrimitive = false;
        switch (type) {
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