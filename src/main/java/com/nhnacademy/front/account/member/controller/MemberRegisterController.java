package com.nhnacademy.front.account.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.common.exception.ValidationFailedException;

@Controller
@RequestMapping("/register")
public class MemberRegisterController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MemberService memberService;

	/**
	 *  회원가입 뷰
	 */
	@GetMapping
	public String getRegister() {
		return "member/register";
	}

	/**
	 * 회원가입 기능 구현
	 */
	@PostMapping
	public String createRegister(@Validated @ModelAttribute RequestRegisterMemberDTO requestRegisterMemberDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String customerPassword = passwordEncoder.encode(requestRegisterMemberDTO.getCustomerPassword());
		requestRegisterMemberDTO.setCustomerPassword(customerPassword);

		memberService.createMember(requestRegisterMemberDTO);

		return "redirect:/login";
	}

}
