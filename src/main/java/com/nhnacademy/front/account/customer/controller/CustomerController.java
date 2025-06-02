package com.nhnacademy.front.account.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.exception.CustomerLoginCheckException;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.service.CustomerService;
import com.nhnacademy.front.cart.model.dto.order.RequestCartOrderDTO;
import com.nhnacademy.front.common.exception.ValidationFailedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping("/login")
	public String getCustomerLogin() {
		return "member/login/customer-login";
	}

	@PostMapping("/login")
	public String loginCustomer(@Validated @ModelAttribute RequestCustomerLoginDTO requestCustomerLoginDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		Customer customer = customerService.customerLogin(requestCustomerLoginDTO);

		return "redirect:/";
	}

	@PostMapping("/register")
	public String registerCustomer(@Validated @ModelAttribute RequestCustomerRegisterDTO requestCustomerRegisterDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		Customer customer = customerService.customerRegister(requestCustomerRegisterDTO);

		return "redirect:/";
	}


	// 비회원 주문 전 인증 폼
	@GetMapping("/customers/orders/auth")
	public String guestAuthForm() {
		return "cart/guest-cart-auth";
	}

	// 비회원 인증 폼으로 이동 전에 세션에 주문할 장바구니 항목을 저장하기 위한 요청
	@PostMapping("/customers/orders/carts/auth")
	public ResponseEntity<Void> redirectGuestAuth(@RequestBody RequestCartOrderDTO requestCartOrderDTO, BindingResult bindingResult, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		// 세션에 주문할 장바구니 항목들을 저장한 후 리다이렉트
		HttpSession session = request.getSession();
		session.setAttribute("orderCart", requestCartOrderDTO);

		return ResponseEntity.ok().build();
	}

	// 비회원 가입 요청
	@PostMapping("/customers/register")
	public ResponseEntity<Void> register(@Validated @RequestBody RequestCustomerRegisterDTO requestCustomerRegisterDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		customerService.createCustomer(requestCustomerRegisterDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// 비회원 로그인 요청
	@PostMapping("/customers/login")
	public ResponseEntity<Boolean> login(@Validated @RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		boolean isLogin = customerService.loginCustomer(requestCustomerLoginDTO);
		if (isLogin) {
			return ResponseEntity.ok(true);
		}

		throw new CustomerLoginCheckException();
	}
}
