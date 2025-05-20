package com.nhnacademy.front.product.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalHeaderCategoryAdvice {

	private final UserCategoryService userCategoryService;

	@ModelAttribute("headerCategories")
	public List<ResponseCategoryDTO> getHeaderCategories() {
		return userCategoryService.getCategoriesToDepth3();
	}
}
