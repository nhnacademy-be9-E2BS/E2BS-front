package com.nhnacademy.front.product.category.controller;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalHeaderCategoryAdvice {

	private final UserCategoryService userCategoryService;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

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
				List<ResponseCategoryDTO> categoryDTOs = objectMapper.convertValue(cached,
					new TypeReference<List<ResponseCategoryDTO>>() {});
				log.info("캐시");
				log.info(categoryDTOs + "");
				return categoryDTOs;
			}
		}

		return userCategoryService.getCategoriesToDepth3();
	}
}
