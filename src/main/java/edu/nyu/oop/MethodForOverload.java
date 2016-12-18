package edu.nyu.oop.util;

import java.util.*;


public class MethodForOverload {
	public String returnType;
	public String name;
	public List<String> argumentNames; // in order
	public List<String> argumentTypes; // in order
	public boolean isPrivate;
	public boolean isStatic;
	// public String class;

	public MethodForOverload(String name, String returnType, List<String> argList, List<String> argTypeList) {
		this(name, returnType, argList, argTypeList, false, false);
	}

	public MethodForOverload(String name, String returnType, List<String> argList, List<String> argTypeList, boolean isStatic, boolean isPrivate) {
		this.name = name;
		this.returnType = returnType;
		this.argumentNames = argList;
		this.argumentTypes = argTypeList;
		this.isPrivate = isPrivate;
		this.isStatic = isStatic;
	}

}