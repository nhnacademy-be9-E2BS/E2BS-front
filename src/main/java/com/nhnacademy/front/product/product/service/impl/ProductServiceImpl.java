package com.nhnacademy.front.product.product.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.adaptor.ProductAdaptor;
import com.nhnacademy.front.product.product.adaptor.ProductAdminAdaptor;
import com.nhnacademy.front.product.product.exception.ProductCreateProcessException;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateByQueryDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductAdaptor productAdaptor;
	private final ProductAdminAdaptor productAdminAdaptor;

	/**
	 * product 단일 조회 (상세 조회)
	 */
	@Override
	public ResponseProductReadDTO getProduct(long productId) {
		try {
			ResponseEntity<ResponseProductReadDTO> response = productAdaptor.getProductById(productId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException("도서 단일 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException("도서 단일 조회 실패");
		}
	}

	/**
	 * 하나의 카테고리에 대한 product 리스트 페이징 조회
	 */
	@Override
	public PageResponse<ResponseProductReadDTO> getProductsByCategoryId(Pageable pageable, long categoryId) {
		try {
			ResponseEntity<PageResponse<ResponseProductReadDTO>> response = productAdaptor.getProductsByCategory(
				pageable, categoryId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
		}
	}

}