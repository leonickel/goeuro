package com.leonickel.exception;

public class CityRetrievalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5600605680077811009L;

	public static final int UNKNOWN_ERROR_CODE = -1;
	public static final int CONNECT_TIMEOUT_CODE = 1;
	public static final int READ_TIMEOUT_CODE = 2;
	public static final int HTTP_CLIENT_ERROR_CODE = 3;
	
	private int code;
	
	public CityRetrievalException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}