package com.nhnacademy.front.account.address.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/addresses/save")
public class AddressSaveController {

	private final AddressService addressService;

	@JwtTokenCheck
	@GetMapping
	public String getSaveMemberAddress(HttpServletRequest request,
		Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		model.addAttribute("memberId", memberId);

		return "member/mypage/mypage-addresses-save";
	}

	@JwtTokenCheck
	@PostMapping
	public String saveMemberAddress(@Validated @ModelAttribute RequestMemberAddressSaveDTO requestMemberAddressSaveDTO,
		BindingResult bindingResult,
		HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		addressService.saveMemberAddress(memberId, requestMemberAddressSaveDTO);

		return "redirect:/mypage/addresses";
	}

}
