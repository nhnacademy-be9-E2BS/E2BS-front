package com.nhnacademy.front.account.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nhnacademy.front.account.customer.adaptor.CustomerLoginAdaptor;
import com.nhnacademy.front.account.customer.adaptor.CustomerRegisterAdaptor;
import com.nhnacademy.front.account.customer.exception.CustomerLoginProcessingException;
import com.nhnacademy.front.account.customer.exception.CustomerPasswordCheckException;
import com.nhnacademy.front.account.customer.exception.CustomerRegisterProcessingException;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerDTO;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	private CustomerLoginAdaptor customerLoginAdaptor;

	@Mock
	private CustomerRegisterAdaptor customerRegisterAdaptor;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private CustomerService customerService;

	@Test
	@DisplayName("비회원 로그인 테스트")
	void customerLogin() {
		// given
		RequestCustomerLoginDTO request = new RequestCustomerLoginDTO("test@domain.com", "password");
		ResponseCustomerDTO responseDto = new ResponseCustomerDTO("홍길동", 1L);
		ResponseEntity<ResponseCustomerDTO> response = ResponseEntity.ok(responseDto);

		when(customerLoginAdaptor.customerLogin(request)).thenReturn(response);

		// when
		ResponseCustomerDTO result = customerService.customerLogin(request);

		// then
		assertEquals(result, responseDto);
	}

	@Test
	@DisplayName("비회원 로그인 테스트 - 실패(HTTP 응답 실패)")
	void customerLogin_Fail_StatusNot2xx() {
		// given
		RequestCustomerLoginDTO request = new RequestCustomerLoginDTO("test@domain.com", "password");
		ResponseEntity<ResponseCustomerDTO> response = ResponseEntity.ok(null);

		when(customerLoginAdaptor.customerLogin(request)).thenReturn(response);

		// when & then
		assertThrows(CustomerLoginProcessingException.class, () -> customerService.customerLogin(request));
	}

	@Test
	@DisplayName("비회원 로그인 실패 - 실패(FeignException)")
	void customerLogin_Fail_FeignException() {
		// given
		RequestCustomerLoginDTO request = new RequestCustomerLoginDTO(
			"test@domain.com", "password");

		when(customerLoginAdaptor.customerLogin(request)).thenThrow(mock(FeignException.class));

		// when & then
		assertThrows(CustomerLoginProcessingException.class, () -> customerService.customerLogin(request));
	}

	@Test
	@DisplayName("비회원 가입 테스트")
	void customerRegister() {
		// given
		RequestCustomerRegisterDTO request = new RequestCustomerRegisterDTO(
			"email@domain.com", "홍길동", "1234", "1234");

		ResponseCustomerDTO responseDto = new ResponseCustomerDTO("홍길동", 1L);
		ResponseEntity<ResponseCustomerDTO> response = ResponseEntity.ok(responseDto);

		when(passwordEncoder.encode("1234")).thenReturn("encodedPwd");
		when(customerRegisterAdaptor.customerRegister(any())).thenReturn(response);

		// when
		ResponseCustomerDTO result = customerService.customerRegister(request);

		// then
		assertEquals(result, responseDto);
		assertEquals("encodedPwd", request.getCustomerPassword());
		assertEquals("encodedPwd", request.getCustomerPasswordCheck());
	}

	@Test
	@DisplayName("비회원 가입 테스트 - 실패(비밀번호 불일치)")
	void customerRegister_Fail_PasswordMismatch() {
		// given
		RequestCustomerRegisterDTO request = new RequestCustomerRegisterDTO(
			"email@domain.com", "홍길동", "1234", "4567");

		// when & then
		assertThrows(CustomerPasswordCheckException.class, () -> customerService.customerRegister(request));
	}

	@Test
	@DisplayName("비회원 회원가입 테스트 - 실패(HTTP 응답 실패)")
	void customerRegister_Fail_StatusNot2xx() {
		// given
		RequestCustomerRegisterDTO request = new RequestCustomerRegisterDTO(
			"email@domain.com", "홍길동", "1234", "1234");

		when(passwordEncoder.encode("1234")).thenReturn("encodedPwd");
		ResponseEntity<ResponseCustomerDTO> response = ResponseEntity.ok(null);

		when(customerRegisterAdaptor.customerRegister(any())).thenReturn(response);

		// when & then
		assertThrows(CustomerRegisterProcessingException.class, () -> customerService.customerRegister(request));
	}

	@Test
	@DisplayName("비회원 회원가입 테스트 - 실패(FeignException)")
	void customerRegister_Fail_FeignException() {
		// given
		RequestCustomerRegisterDTO request = new RequestCustomerRegisterDTO(
			"email@domain.com", "홍길동", "1234", "1234");

		when(passwordEncoder.encode("1234")).thenReturn("encodedPwd");
		when(customerRegisterAdaptor.customerRegister(any())).thenThrow(mock(FeignException.class));

		// when & then
		assertThrows(CustomerRegisterProcessingException.class, () -> customerService.customerRegister(request));
	}

}
