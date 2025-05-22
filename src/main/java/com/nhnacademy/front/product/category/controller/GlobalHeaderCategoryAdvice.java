package com.nhnacademy.front.product.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlobalHeaderCategoryAdvice {

	private final UserCategoryService userCategoryService;

	@ModelAttribute("headerCategories")
	public List<ResponseCategoryDTO> getHeaderCategories(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.startsWith("/actuator")) {
			return List.of(); // or null
		}
		return userCategoryService.getCategoriesToDepth3();
	}
}
