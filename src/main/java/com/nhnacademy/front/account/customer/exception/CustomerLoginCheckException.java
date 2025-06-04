package com.nhnacademy.front.account.customer.exception;

public class CustomerLoginCheckException extends RuntimeException {
	public CustomerLoginCheckException() {
		super("이메일, 비밀번호가 일치하지 않습니다.");
	}
}
