package com.ilona.scraper;

public class FailureException extends Exception {

	public FailureException() {
		super();
	}

	public FailureException(String message) {
		super(message);
	}

	public FailureException(Throwable cause) {
		super(cause);
	}

}
