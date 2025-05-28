package com.nhnacademy.front.common.exception;

public class ServerErrorException extends RuntimeException {
	public ServerErrorException(String message) {
		super(message);
	}
}
