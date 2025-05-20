package com.nhnacademy.front.account.member.controller.login;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class MemberRegisterController {

	private final MemberService memberService;

	/**
	 *  회원가입 뷰
	 */
	@GetMapping
	public String getRegister() {
		return "member/login/register";
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

		memberService.createMember(requestRegisterMemberDTO);

		return "redirect:/login";
	}

}
