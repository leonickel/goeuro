package com.leonickel.exception;

public class NoCitiesFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3692569596463375264L;

	public NoCitiesFoundException(String message) {
		super(message);
	}
}
