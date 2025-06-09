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
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerLoginAdaptor customerLoginAdaptor;
	private final CustomerRegisterAdaptor customerRegisterAdaptor;

	private final PasswordEncoder passwordEncoder;

	public ResponseCustomerDTO customerLogin(RequestCustomerLoginDTO requestCustomerLoginDTO) {
		try {
			ResponseEntity<ResponseCustomerDTO> response = customerLoginAdaptor.customerLogin(requestCustomerLoginDTO);
			if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
				throw new CustomerLoginProcessingException();
			}

			return response.getBody();

		} catch (FeignException ex) {
			throw new CustomerLoginProcessingException();
		}
	}

	public ResponseCustomerDTO customerRegister(RequestCustomerRegisterDTO requestCustomerRegisterDTO) {
		try {
			if (!requestCustomerRegisterDTO.getCustomerPassword()
				.equals(requestCustomerRegisterDTO.getCustomerPasswordCheck())) {
				throw new CustomerPasswordCheckException();
			}

			String customerPassword = passwordEncoder.encode(requestCustomerRegisterDTO.getCustomerPassword());
			requestCustomerRegisterDTO.setCustomerPassword(customerPassword);
			requestCustomerRegisterDTO.setCustomerPasswordCheck(customerPassword);

			ResponseEntity<ResponseCustomerDTO> response = customerRegisterAdaptor.customerRegister(
				requestCustomerRegisterDTO);
			if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
				throw new CustomerRegisterProcessingException();
			}

			return response.getBody();
		} catch (FeignException ex) {
			throw new CustomerRegisterProcessingException();
		}
	}

}
