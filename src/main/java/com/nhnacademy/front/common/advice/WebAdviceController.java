package com.nhnacademy.front.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
import com.nhnacademy.front.account.member.exception.LoginProcessException;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.common.exception.EmptyResponseException;
import com.nhnacademy.front.common.exception.ValidationFailedException;

@ControllerAdvice
public class WebAdviceController {

	@ExceptionHandler(LoginProcessException.class)
	public ResponseEntity<String> loginProcessException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(ValidationFailedException.class)
	public ResponseEntity<String> validationFailedException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(EmptyRequestException.class)
	public ResponseEntity<String> emptyRequestException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(RegisterProcessException.class)
	public ResponseEntity<String> registerProcessException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(RegisterNotEqualsPasswordException.class)
	public ResponseEntity<String> registerNotEqualsPasswordException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(SaveJwtTokenProcessException.class)
	public ResponseEntity<String> saveJwtTokenProcessException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(EmptyResponseException.class)
	public ResponseEntity<String> emptyResponseException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

}
