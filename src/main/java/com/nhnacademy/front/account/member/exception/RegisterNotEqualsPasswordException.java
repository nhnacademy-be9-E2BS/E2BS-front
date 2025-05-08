package com.nhnacademy.front.account.member.exception;

public class RegisterNotEqualsPasswordException extends RuntimeException {
	public RegisterNotEqualsPasswordException(String message) {
		super(message);
	}
}
