package com.nhnacademy.front.home.controller;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.index.service.IndexService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final HomeService homeService;
	private final IndexService indexService;


	@JwtTokenCheck
	@GetMapping("/")
	public String index(HttpServletRequest request, Model model) {
		ResponseHomeMemberNameDTO responseHomeMemberNameDTO = homeService.getMemberNameFromHome(request);

		model.addAttribute("BestSellerList", indexService.getBestSellerProducts());
		model.addAttribute("BlogBestList", indexService.getBlogBestProducts());
		model.addAttribute("ItemNewAllList", indexService.getNewItemsProducts());
		model.addAttribute("NewSpecialItemsList", indexService.getNewSpecialItemsProducts());
		model.addAttribute("memberName", "회원 이름");

		if (Objects.nonNull(responseHomeMemberNameDTO)) {
			model.addAttribute("memberName", responseHomeMemberNameDTO.getMemberName());
			model.addAttribute("memberRole", responseHomeMemberNameDTO.getMemberRole());
		}

		return "home";
	}
}
