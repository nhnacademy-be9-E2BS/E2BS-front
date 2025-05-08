package com.nhnacademy.front.common.exception;

public class EmptyRequestException extends RuntimeException {
	public EmptyRequestException(String message) {
		super(message);
	}
}
