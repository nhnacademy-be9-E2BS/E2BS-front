package com.nhnacademy.front.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nhnacademy.front.account.member.exception.LoginProcessException;

@ControllerAdvice
public class WebAdviceController {

	@ExceptionHandler(LoginProcessException.class)
	public ResponseEntity<String> loginProcessException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

}
