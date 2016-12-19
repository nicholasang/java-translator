package edu.nyu.oop.util;

import java.util.*;

import xtc.Constants;
import xtc.tree.GNode;

public class Symbol {
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