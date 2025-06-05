package com.nhnacademy.front.account.member.controller.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.model.dto.request.RequestMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/info")
public class MemberInfoMypageController {

	private final MemberMypageService memberMypageService;

	/**
	 * 회원 정보를 가져와서 보여주는 뷰
	 */
	@JwtTokenCheck
	@GetMapping
	public String getMemberInfo(HttpServletRequest request, Model model) {
		ResponseMemberInfoDTO response = memberMypageService.getMemberInfo(request);

		model.addAttribute("member", response);

		return "member/mypage/memberinfo/member-info";
	}

	/**
	 * 회원 정보를 수정하는 컨트롤러
	 */
	@JwtTokenCheck
	@PutMapping
	public String updateMemberInfo(@Validated @ModelAttribute RequestMemberInfoDTO requestMemberInfoDTO,
		BindingResult bindingResult,
		HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		memberMypageService.updateMemberInfo(request, requestMemberInfoDTO);

		return "redirect:/mypage/info";
	}

	/**
	 * 회원 탈퇴 메서드
	 */
	@JwtTokenCheck
	@PostMapping
	public String withdrawMember(HttpServletRequest request, HttpServletResponse response) {
		memberMypageService.withdrawMember(request, response);

		return "redirect:/";
	}

}
