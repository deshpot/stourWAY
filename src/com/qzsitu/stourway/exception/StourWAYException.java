package com.qzsitu.stourway.exception;

public class StourWAYException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8790617834865089910L;

	public StourWAYException(String message) {
		super(message);
	}
	
	public StourWAYException(String message, Throwable cause) {
		super(message, cause);
	}
}
