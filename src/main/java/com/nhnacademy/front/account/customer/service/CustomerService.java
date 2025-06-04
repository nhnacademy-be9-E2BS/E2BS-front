package com.nhnacademy.front.account.customer.service;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.customer.adaptor.CustomerLoginAdaptor;
import com.nhnacademy.front.account.customer.adaptor.CustomerRegisterAdaptor;
import com.nhnacademy.front.account.customer.exception.CustomerLoginProcessingException;
import com.nhnacademy.front.account.customer.exception.CustomerPasswordCheckException;
import com.nhnacademy.front.account.customer.exception.CustomerRegisterProcessingException;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerLoginAdaptor customerLoginAdaptor;
	private final CustomerRegisterAdaptor customerRegisterAdaptor;

	private final PasswordEncoder passwordEncoder;

	public Long customerLogin(RequestCustomerLoginDTO requestCustomerLoginDTO) {
		try {
			ResponseEntity<Long> response = customerLoginAdaptor.customerLogin(requestCustomerLoginDTO);
			if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
				throw new CustomerLoginProcessingException("비회원 로그인 실패했습니다.");
			}

			return response.getBody();

		} catch (FeignException ex) {
			throw new CustomerLoginProcessingException("비회원 로그인 실패했습니다.");
		}
	}

	public Long customerRegister(RequestCustomerRegisterDTO requestCustomerRegisterDTO) {
		try {
			if (!requestCustomerRegisterDTO.getCustomerPassword()
				.equals(requestCustomerRegisterDTO.getCustomerPasswordCheck())) {
				throw new CustomerPasswordCheckException("비밀번호가 서로 일치하지 않습니다.");
			}

			String customerPassword = passwordEncoder.encode(requestCustomerRegisterDTO.getCustomerPassword());
			requestCustomerRegisterDTO.setCustomerPassword(customerPassword);
			requestCustomerRegisterDTO.setCustomerPasswordCheck(customerPassword);

			ResponseEntity<Long> response = customerRegisterAdaptor.customerRegister(requestCustomerRegisterDTO);
			if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
				throw new CustomerRegisterProcessingException("비회원 첫주문 실패했습니다.");
			}

			return response.getBody();
		} catch (FeignException ex) {
			throw new CustomerRegisterProcessingException("비회원 첫주문 실패했습니다.");
		}
	}

}
