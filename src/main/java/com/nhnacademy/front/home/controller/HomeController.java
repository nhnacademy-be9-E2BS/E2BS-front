package com.nhnacademy.front.home.controller;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
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
		ResponseHomeMemberNameDTO responseHomeMemberNameDTO = homeService.getMemberNameFromHome(request);

		if (Objects.nonNull(responseHomeMemberNameDTO)) {
			model.addAttribute("memberName", responseHomeMemberNameDTO.getMemberName());
			model.addAttribute("memberRole", responseHomeMemberNameDTO.getMemberRole());
		}

		return "home";
	}
}
