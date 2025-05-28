package com.nhnacademy.front.product.category.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.product.category.adaptor.UserCategoryAdaptor;
import com.nhnacademy.front.product.category.exception.CategoryGetProcessException;
import com.nhnacademy.front.product.category.exception.CategoryNotFoundException;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCategoryService {

	private final UserCategoryAdaptor userCategoryAdaptor;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	/**
	 * html 헤더에 표시할 카테고리 리스트를 back에서 조회
	 */
	public List<ResponseCategoryDTO> getCategoriesToDepth3() {
		try {
			ResponseEntity<List<ResponseCategoryDTO>> response = userCategoryAdaptor.getCategoriesToDepth3();

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new CategoryGetProcessException("카테고리 헤더 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new CategoryGetProcessException("카테고리 헤더 조회 실패");
		}
	}

	/**
	 * 도서 리스트 조회 시 사이드 바에 표시할 카테고리 조회
	 */
	public ResponseCategoryDTO getCategoriesById(Long categoryId) {
		try {
			List<ResponseCategoryDTO> allCategories = getCachedCategories();

			if (allCategories == null) {
				ResponseEntity<List<ResponseCategoryDTO>> response = userCategoryAdaptor.getAllCategories();

				if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
					throw new CategoryGetProcessException("카테고리 사이드 바 조회 실패");
				}
				allCategories = response.getBody();
			}

			ResponseCategoryDTO result = findCategoryById(allCategories, categoryId);
			if (result == null) {
				throw new CategoryNotFoundException();
			}
			return result;
			
		} catch (FeignException e) {
			throw new CategoryGetProcessException("카테고리 사이드 바 조회 실패");
		}
	}

	/**
	 * Redis 서버에 캐싱된 카테고리 데이터가 있는지 확인하고 있으면 return
	 */
	private List<ResponseCategoryDTO> getCachedCategories() {
		if (!Boolean.TRUE.equals(redisTemplate.hasKey("Categories::all"))) {
			return null;
		}

		Object cached = redisTemplate.opsForValue().get("Categories::all");
		return objectMapper.convertValue(cached, new TypeReference<List<ResponseCategoryDTO>>() {});
	}

	/**
	 * getCategoriesById(long categoryId) 메소드에서 해당 categoryId 노드를 찾기 위한 메소드
	 */
	private ResponseCategoryDTO findCategoryById(List<ResponseCategoryDTO> categories, long categoryId) {
		for (ResponseCategoryDTO category : categories) {
			if (category.getCategoryId() == categoryId) {
				return category;
			}

			ResponseCategoryDTO found = findCategoryById(category.getChildren(), categoryId);
			if (found != null) {
				return found;
			}
		}
		return null;
	}
}
