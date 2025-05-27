package com.nhnacademy.front.account.admin.exception;

public class AdminSettingsNonMembersFailedException extends RuntimeException {
	public AdminSettingsNonMembersFailedException(String message) {
		super(message);
	}
}
