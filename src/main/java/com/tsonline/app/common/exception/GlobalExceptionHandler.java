package com.tsonline.app.common.exception;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tsonline.app.common.dto.ErrorResponse;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final MessageSource messageSource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> {
					// Getting the field name and customize it
					String defaultFieldName = error.getField();
					String customFieldName = messageSource.getMessage("field. " + defaultFieldName ,
																		null, defaultFieldName, Locale.getDefault());
					// Setting the custom message according to param values
					String finalMessage = messageSource.getMessage(error, Locale.getDefault());
					finalMessage = finalMessage.replace("{0}", customFieldName);
					
					ErrorResponse err = new ErrorResponse();
		            err.setField(customFieldName);
		            err.setErrorMessage(finalMessage);
		            return err;
				}).collect(Collectors.toList());
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(message);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateEntryException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateEntryException(DuplicateEntryException ex) {
		String message = ex.getMessage();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(message);
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ErrorResponse> handleBusinessRuleException(BusinessRuleException ex) {
		String message = ex.getMessage();
		HttpStatus status = ex.getStatus();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(message);
		return new ResponseEntity<>(errorResponse, status);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupportException(HttpRequestMethodNotSupportedException ex) {
		String message = ex.getMessage();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(message);
		return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}
}
