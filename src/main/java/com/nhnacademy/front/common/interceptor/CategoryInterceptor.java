package com.nhnacademy.front.common.interceptor;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {

	private final UserCategoryService userCategoryService;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Object cached = Boolean.TRUE.equals(redisTemplate.hasKey("Categories::header"))
			? redisTemplate.opsForValue().get("Categories::header")
			: null;

		if (cached instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<ResponseCategoryDTO> categoryDTOs = objectMapper.convertValue(
				cached, new TypeReference<List<ResponseCategoryDTO>>() {});
			request.setAttribute("headerCategories", categoryDTOs);
		} else {
			request.setAttribute("headerCategories", userCategoryService.getCategoriesToDepth3());
		}

		return true;
	}

}
