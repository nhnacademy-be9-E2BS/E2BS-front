package com.nhnacademy.front.account.oauth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.oauth.adaptor.PaycoLoginAdaptor;
import com.nhnacademy.front.account.oauth.exception.PaycoLoginException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaycoService {

	private final PaycoLoginAdaptor paycoLoginAdaptor;

	public void paycoLogin() {
		ResponseEntity<Void> getPaycoLogin = paycoLoginAdaptor.getPaycoLogin();
		if (!getPaycoLogin.getStatusCode().is2xxSuccessful()) {
			throw new PaycoLoginException("Payco 로그인 화면을 가져오지 못했습니다.");
		}
	}

}
