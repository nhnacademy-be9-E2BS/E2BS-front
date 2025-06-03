package com.nhnacademy.front.account.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.customer.model.domain.Customer;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.service.CustomerService;
import com.nhnacademy.front.common.exception.ValidationFailedException;

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

}
