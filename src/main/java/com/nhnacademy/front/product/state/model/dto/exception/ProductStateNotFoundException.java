package com.nhnacademy.front.product.state.model.dto.exception;

public class ProductStateNotFoundException extends RuntimeException {
	public ProductStateNotFoundException(String message) {
		super(message);
	}
}
