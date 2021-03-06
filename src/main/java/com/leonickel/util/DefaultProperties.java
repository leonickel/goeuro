package com.leonickel.util;

public enum DefaultProperties {
	GO_EURO_URL("goeuro.url", "http://api.goeuro.com/api/v2/position/suggest/en/"),
	GO_EURO_URL_READ_TIMEOUT("goeuro.url.read-timeout", "10000"),
	GO_EURO_URL_CONNECT_TIMEOUT("goeuro.url.connect-timeout", "5000"),
	GO_EURO_URL_MAX_RETRY("goeuro.url.max-retry", "3"),
	
	CSV_FILE_NAME("csv.file-name", "result.csv"),
	CSV_OUTPUT_PATH("csv.output-path", "./"),
	CSV_SEPARATOR_VALUE("csv.separator-value", ",")
	;
	
	private String property;
	private String defaultValue;
	
	private DefaultProperties(String property, String defaultValue) {
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