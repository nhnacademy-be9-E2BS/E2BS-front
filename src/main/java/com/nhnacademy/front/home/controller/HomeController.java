package com.nhnacademy.front.home.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;
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
		List<ResponseMainPageProductDTO> bestSellerList = indexService.getBestSellerProducts();
		List<List<ResponseMainPageProductDTO>> chunkedList = new ArrayList<>();

		for (int i = 0; i < bestSellerList.size(); i += 4) {
			chunkedList.add(bestSellerList.subList(i, Math.min(i + 4, bestSellerList.size())));
		}
		model.addAttribute("today", LocalDate.now());
		model.addAttribute("BestSellerList", bestSellerList);
		model.addAttribute("ItemNewAllList", indexService.getNewItemsProducts());

		return "home";
	}

}
