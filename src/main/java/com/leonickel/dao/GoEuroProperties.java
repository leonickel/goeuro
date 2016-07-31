package com.leonickel.dao;

public enum GoEuroProperties {
	GO_EURO_URL("goeuro.url", "http://api.goeuro.com/api/v2/position/suggest/en/"),
	GO_EURO_URL_READ_TIMEOUT("goeuro.url.read-timeout", "5000"),
	GO_EURO_URL_CONNECT_TIMEOUT("goeuro.url.connect-timeout", "1000")
	;
	
	private String property;
	private String defaultValue;
	
	private GoEuroProperties(String property, String defaultValue) {
		this.property = property;
		this.defaultValue = defaultValue;
	}
	
	public String property() {
		return property;
	}
	
	public String defaultValue() {
		return defaultValue;
	}
}