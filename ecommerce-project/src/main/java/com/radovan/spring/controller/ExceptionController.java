package com.radovan.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InsufficientStockException;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.exceptions.SuspendedUserException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(ExistingEmailException.class)
	public ResponseEntity<String> handleExistingEmailException(ExistingEmailException exc) {
		return ResponseEntity.internalServerError().body("Email exists already!!!");
	}

	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException exc) {
		return ResponseEntity.internalServerError().body("Not enough stock!!!");
	}

	@ExceptionHandler(InvalidCartException.class)
	public ResponseEntity<String> handleInvalidCartException(InvalidCartException exc) {
		return ResponseEntity.internalServerError().body("Invalid cart!!!");
	}

	@ExceptionHandler(InvalidUserException.class)
	public ResponseEntity<String> handleInvalidUserException(InvalidUserException exc) {
		return ResponseEntity.internalServerError().body("Invalid user!!!");
	}

	@ExceptionHandler(SuspendedUserException.class)
	public ResponseEntity<String> handleSuspendedUserException(SuspendedUserException exc) {
		return ResponseEntity.internalServerError().body("User suspended!!!");
	}

}
