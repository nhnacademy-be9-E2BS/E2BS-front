package com.nhnacademy.front.elasticsearch.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.elasticsearch.adaptor.ProductSearchAdaptor;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.elasticsearch.service.ProductSearchService;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

	private final ProductSearchAdaptor productSearchAdaptor;

	/**
	 * 검색어로 도서 리스트 조회
	 * 정렬은 선택적 (선택 안하면 출판일자 최신순)
	 */
	@Override
	public PageResponse<ResponseProductReadDTO> getProductsBySearch(Pageable pageable, String keyword,
		ProductSortType sort, String memberId) {
		try {
			ResponseEntity<PageResponse<ResponseProductReadDTO>> response = productSearchAdaptor.getProductsBySearch(pageable, keyword, sort, memberId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException("검색어로 도서 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException("검색어로 도서 리스트 조회 실패");
		}
	}

	/**
	 * 카테고리 ID로 도서 리스트 조회
	 * 정렬은 선택적 (선택 안하면 출판일자 최신순)
	 */
	@Override
	public PageResponse<ResponseProductReadDTO> getProductsByCategory(Pageable pageable, Long categoryId,
		ProductSortType sort, String memberId) {
		try {
			ResponseEntity<PageResponse<ResponseProductReadDTO>> response = productSearchAdaptor.getProductsByCategory(pageable, categoryId, sort, memberId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
		}
	}

	/**
	 * 헤더 클릭 시 베스트 도서 조회 (top 30)
	 */
	@Override
	public PageResponse<ResponseProductReadDTO> getBestProducts(Pageable pageable, String memberId) {
		try {
			ResponseEntity<PageResponse<ResponseProductReadDTO>> response = productSearchAdaptor.getBestProducts(pageable, memberId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
		}
	}

	/**
	 * 헤더 클릭 시 신상 도서 조회 (출판일자 3개월 이내)
	 */
	@Override
	public PageResponse<ResponseProductReadDTO> getNewestProducts(Pageable pageable, String memberId) {
		try {
			ResponseEntity<PageResponse<ResponseProductReadDTO>> response = productSearchAdaptor.getNewestProducts(pageable, memberId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException("카테고리 도서 리스트 조회 실패");
		}
	}
}
