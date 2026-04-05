package com.tsonline.app.common.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tsonline.app.category.dto.ErrorResponse;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	
	private final MessageSource messageSource;
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			
			String defaultFieldName = ((FieldError)error).getField();
			String customFieldName = messageSource.getMessage("field." + defaultFieldName, null, defaultFieldName, Locale.getDefault());
			
			String rawMessage = error.getDefaultMessage();
			String finalMessage = rawMessage.replace("{0}", customFieldName);
			if (error.getArguments() != null && error.getArguments().length > 1) {
                finalMessage = finalMessage.replace("{1}", error.getArguments()[2].toString()) 
                                           .replace("{2}", error.getArguments()[1].toString());
            }
			
			errorResponse.setField(customFieldName);
			errorResponse.setErrorMessage(finalMessage);
		});
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
}
