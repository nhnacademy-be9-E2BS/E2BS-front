package com.nhnacademy.front.common.error.exception;

public class EmptyRequestException extends RuntimeException {
	public EmptyRequestException(String message) {
		super(message);
	}
}
