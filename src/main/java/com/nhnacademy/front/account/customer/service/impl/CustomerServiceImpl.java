package com.nhnacademy.front.account.customer.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.customer.adaptor.CustomerAdaptor;
import com.nhnacademy.front.account.customer.exception.CustomerProcessException;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.service.CustomerService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerAdaptor customerAdaptor;


	@Override
	public void createCustomer(RequestCustomerRegisterDTO request) {
		try {
			ResponseEntity<Void> result = customerAdaptor.registerCustomer(request);
			
			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CustomerProcessException("비회원 생성 실패: " + result.getStatusCode());
			}

		} catch (FeignException ex) {
			throw new CustomerProcessException("비회원 생성 실패: " + ex.getMessage());
		}
	}

	@Override
	public boolean loginCustomer(RequestCustomerLoginDTO request) {
		try {
			ResponseEntity<Boolean> result = customerAdaptor.loginCustomer(request);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CustomerProcessException("비회원 로그인 실패: " + result.getStatusCode());
			}
			return Boolean.TRUE.equals(result.getBody());
		} catch (FeignException ex) {
			throw new CustomerProcessException("비회원 로그인 실패: " + ex.getMessage());
		}
	}

}
