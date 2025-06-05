package com.nhnacademy.front.account.customer.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.service.CustomerService;
import com.nhnacademy.front.cart.model.dto.order.RequestCartOrderDTO;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;
	private final ObjectMapper objectMapper;

	@GetMapping("/customers/login")
	public String getCustomerLogin() {
		return "member/login/customer-login";
	}

	/// 비회원 주문 전 인증 폼
	@GetMapping("/order/auth")
	public String guestAuthForm() {
		return "customer/auth";
	}

	/// 비회원 인증 폼으로 이동 전에 세션에 주문할 장바구니 항목을 저장하기 위한 요청
	@PostMapping("/order/auth")
	public ResponseEntity<Void> redirectGuestAuth(@RequestBody RequestCartOrderDTO requestCartOrderDTO,
		BindingResult bindingResult,
		HttpServletResponse response) throws JsonProcessingException {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		// 세션에 주문할 장바구니 항목들을 저장
		// 객체 → JSON 문자열 직렬화
		String cartJson = objectMapper.writeValueAsString(requestCartOrderDTO);

		// JSON → Base64 (한글 깨짐 방지 및 안전한 전송)
		String encodedCart = Base64.getEncoder().encodeToString(cartJson.getBytes(StandardCharsets.UTF_8));

		// 쿠키 생성
		Cookie cartCookie = new Cookie("orderCart", encodedCart);
		cartCookie.setPath("/");                  // 모든 경로에서 접근 가능하게
		cartCookie.setHttpOnly(true);             // JS 접근 방지 (보안)
		cartCookie.setMaxAge(60 * 30);            // 30분
		response.addCookie(cartCookie);

		return ResponseEntity.ok().build();
	}

	/// 비회원 가입 요청
	@PostMapping("/customers/register")
	public ResponseEntity<ResponseCustomerRegisterDTO> register(@CookieValue(name = "orderCart") String encodedCart,
		@Validated @RequestBody RequestCustomerRegisterDTO requestCustomerRegisterDTO,
		BindingResult bindingResult) throws JsonProcessingException {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		ResponseCustomerDTO customer = customerService.customerRegister(requestCustomerRegisterDTO);

		String orderCartJson = new String(Base64.getDecoder().decode(encodedCart), StandardCharsets.UTF_8);
		RequestCartOrderDTO requestCartOrderDTO = objectMapper.readValue(orderCartJson, RequestCartOrderDTO.class);

		ResponseCustomerRegisterDTO response = new ResponseCustomerRegisterDTO(customer.getCustomerName(),
			customer.getCustomerId(), requestCartOrderDTO);
		return ResponseEntity.ok(response);
	}

	// 주문 시 비회원 로그인 요청
	@PostMapping("/customers/login")
	public ResponseEntity<ResponseCustomerRegisterDTO> login(@CookieValue(name = "orderCart") String encodedCart,
		@Validated @RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO, BindingResult bindingResult) throws
		JsonProcessingException {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		ResponseCustomerDTO customer = customerService.customerLogin(requestCustomerLoginDTO);

		String orderCartJson = new String(Base64.getDecoder().decode(encodedCart), StandardCharsets.UTF_8);
		RequestCartOrderDTO requestCartOrderDTO = objectMapper.readValue(orderCartJson, RequestCartOrderDTO.class);

		ResponseCustomerRegisterDTO response = new ResponseCustomerRegisterDTO(customer.getCustomerName(),
			customer.getCustomerId(), requestCartOrderDTO);
		return ResponseEntity.ok(response);
	}


	// 주문 시 비회원 로그인 요청
	@PostMapping("/customers/order/login")
	public ResponseEntity<Long> login(@Validated @RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO
		, BindingResult bindingResult) throws JsonProcessingException {

		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		ResponseCustomerDTO customer = customerService.customerLogin(requestCustomerLoginDTO);

		return ResponseEntity.ok(customer.getCustomerId());
	}

}
