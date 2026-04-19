package com.tsonline.app.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;
	
	public BusinessRuleException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
}
