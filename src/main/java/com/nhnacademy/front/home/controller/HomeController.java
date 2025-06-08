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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "메인 페이지", description = "메인 페이지 화면 제공 및 기능 구현")
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final HomeService homeService;
	private final IndexService indexService;

	@Operation(summary = "메인 페이지 화면", description = "메인 페이지 데이터 요청 및 화면 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "메인 페이지 화면 제공을 위한 요청 및 성공 응답")
		})
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
		model.addAttribute("BlogBestList", indexService.getBlogBestProducts());
		model.addAttribute("ItemNewAllList", indexService.getNewItemsProducts());
		model.addAttribute("NewSpecialItemsList", indexService.getNewSpecialItemsProducts());

		return "home";
	}

}
