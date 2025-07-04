package com.nhnacademy.front.product.category.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.product.category.adaptor.AdminCategoryAdaptor;
import com.nhnacademy.front.product.category.adaptor.UserCategoryAdaptor;
import com.nhnacademy.front.product.category.exception.CategoryCreateProcessException;
import com.nhnacademy.front.product.category.exception.CategoryDeleteProcessException;
import com.nhnacademy.front.product.category.exception.CategoryGetProcessException;
import com.nhnacademy.front.product.category.exception.CategoryUpdateProcessException;
import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

	private final AdminCategoryAdaptor adminCategoryAdaptor;
	private final UserCategoryAdaptor userCategoryAdaptor;

	/**
	 * 관리자 페이지에서 전체 카테고리 리스트를 back에서 조회
	 */
	public List<ResponseCategoryDTO> getCategories() throws FeignException {
		ResponseEntity<List<ResponseCategoryDTO>> response = userCategoryAdaptor.getAllCategories();

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CategoryGetProcessException();
		}
		return response.getBody();
	}

	/**
	 * Category를 back - category table에 저장
	 * 최상위 + 하위 카테고리를 동시에 저장
	 */
	public void createCategoryTree(List<RequestCategoryDTO> request) throws FeignException {
		ResponseEntity<Void> response = adminCategoryAdaptor.postCreateCategoryTree(request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CategoryCreateProcessException();
		}
	}

	/**
	 * Category를 back - category table에 저장
	 * 이미 존재하는 카테고리 하위에 저장
	 */
	public void createChildCategory(Long categoryId, RequestCategoryDTO request) throws FeignException {
		ResponseEntity<Void> response = adminCategoryAdaptor.postCreateChildCategory(categoryId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CategoryCreateProcessException();
		}
	}

	/**
	 * Category를 back - category table에서 수정
	 */
	public void updateCategory(Long categoryId, RequestCategoryDTO request) throws FeignException {
		ResponseEntity<Void> response = adminCategoryAdaptor.putUpdateCategory(categoryId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CategoryUpdateProcessException();
		}
	}

	/**
	 * Category를 back - category table에서 삭제
	 */
	public void deleteCategory(Long categoryId) throws FeignException {
		ResponseEntity<Void> response = adminCategoryAdaptor.deleteCategory(categoryId);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CategoryDeleteProcessException();
		}
	}

}
