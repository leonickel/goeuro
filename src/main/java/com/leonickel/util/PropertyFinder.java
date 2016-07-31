package com.leonickel.util;

public class PropertyFinder {

	public static String getPropertyValue(DefaultProperties property) {
		return System.getProperty(property.property(), property.defaultValue());
	}
}