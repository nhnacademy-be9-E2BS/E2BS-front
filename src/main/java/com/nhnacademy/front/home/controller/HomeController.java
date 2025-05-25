package com.nhnacademy.front.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.home.service.HomeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final HomeService homeService;

	@JwtTokenCheck
	@GetMapping("/")
	public String index(HttpServletRequest request, Model model) {
		String memberName = homeService.getMemberNameFromHome(request);

		model.addAttribute("memberName", memberName);

		return "home";
	}
}
