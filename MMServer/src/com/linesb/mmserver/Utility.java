package com.linesb.mmserver;

/**
* Static class that stores utility methods for global use.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class Utility {
	
	public static boolean containsIllegal(final String string) {
		final String illegalString = "[~#@*+%{}<>\\[\\]|\"\\_^]";
	    return string.length() != string.replaceAll(illegalString, "").length();
	}
	
	public static boolean isWithinBoundaries(String string, int min, int max) {
		return string.length() >= min && string.length() <= max;
	}
}
