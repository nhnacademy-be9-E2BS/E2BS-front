package com.nhnacademy.front.elasticsearch.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

public interface ProductSearchService {
	// 검색어로 검색 (정렬 선택적)
	PageResponse<ResponseProductReadDTO> getProductsBySearch(Pageable pageable, String keyword, ProductSortType sort, String memberId);

	// 카테고리로 검색 (정렬 선택적)
	PageResponse<ResponseProductReadDTO> getProductsByCategory(Pageable pageable, Long categoryId, ProductSortType sort, String memberId);

	// 베스트 도서 - 헤더 클릭
	PageResponse<ResponseProductReadDTO> getBestProducts(Pageable pageable, String memberId);

	// 신상 도서 - 헤더 클릭
	PageResponse<ResponseProductReadDTO> getNewestProducts(Pageable pageable, String memberId);
}
