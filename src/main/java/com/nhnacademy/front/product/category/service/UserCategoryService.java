package com.nhnacademy.front.product.category.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.product.category.adaptor.UserCategoryAdaptor;
import com.nhnacademy.front.product.category.exception.CategoryGetProcessException;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCategoryService {

	private final UserCategoryAdaptor userCategoryAdaptor;

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
	 * 도서 리스트 조회 시 사이드 바에 표시할 카테고리 리스트를 back에서 조회
	 */
	public List<ResponseCategoryDTO> getCategoriesById(@PathVariable Long categoryId) {
		try {
			ResponseEntity<List<ResponseCategoryDTO>> response = userCategoryAdaptor.getCategoriesById(categoryId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new CategoryGetProcessException("카테고리 사이드 바 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new CategoryGetProcessException("카테고리 사이드 바 조회 실패");
		}
	}
}
