package com.nhnacademy.front.product.category.controller;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalHeaderCategoryAdvice {

	private final UserCategoryService userCategoryService;
	private final RedisTemplate<String, Object> redisTemplate;

	@ModelAttribute("headerCategories")
	public List<ResponseCategoryDTO> getHeaderCategories(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.startsWith("/actuator")) {
			return List.of(); // or null
		}

		if (Boolean.TRUE.equals(redisTemplate.hasKey("Categories::header"))) {
			Object cached = redisTemplate.opsForValue().get("Categories::header");
			if (cached instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<ResponseCategoryDTO> categoryDTOs = (List<ResponseCategoryDTO>) cached;
				return categoryDTOs;
			}
		}

		return userCategoryService.getCategoriesToDepth3();
	}
}
