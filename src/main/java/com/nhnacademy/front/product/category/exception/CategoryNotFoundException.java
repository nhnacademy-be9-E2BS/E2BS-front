package com.nhnacademy.front.product.category.exception;

public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException() {
		super("카테고리를 찾을 수 없습니다.");
	}
}
