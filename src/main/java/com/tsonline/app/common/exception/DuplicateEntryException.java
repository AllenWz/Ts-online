package com.tsonline.app.common.exception;

public class DuplicateEntryException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DuplicateEntryException() {
		super();
	}

	public DuplicateEntryException(String message) {
		super(message);
	}
}
