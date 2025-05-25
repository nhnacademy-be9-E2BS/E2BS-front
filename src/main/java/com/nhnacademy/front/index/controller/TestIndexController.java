// package com.nhnacademy.front.index.controller;
//
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
//
// import com.nhnacademy.front.account.member.service.MemberService;
// import com.nhnacademy.front.common.annotation.JwtTokenCheck;
//
// import jakarta.servlet.http.HttpServletRequest;
// import lombok.RequiredArgsConstructor;
//
// @Controller
// @RequiredArgsConstructor
// public class TestIndexController {
//
// 	private final MemberService memberService;
//
// 	@JwtTokenCheck
// 	@GetMapping("/")
// 	public String index(HttpServletRequest request, Model model) {
//
// 		model.addAttribute("memberName", "회원 이름");
//
// 		return "home";
// 	}
// }
