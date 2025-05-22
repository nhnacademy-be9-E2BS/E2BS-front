package com.nhnacademy.front.index.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.account.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestIndexController {

	private final MemberService memberService;

	@GetMapping( "/")
	public String index(HttpServletRequest request, Model model) {

		String memberName = memberService.getMemberName(request);
		model.addAttribute("memberName", memberName);

		return "home";
	}
}
