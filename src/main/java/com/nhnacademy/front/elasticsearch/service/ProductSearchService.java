package com.nhnacademy.front.elasticsearch.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

public interface ProductSearchService {
	// 검색어로 검색 (정렬 선택적)
	PageResponse<ResponseProductReadDTO> getProductsBySearch(Pageable pageable, String keyword, ProductSortType sort);

	// 카테고리로 검색 (정렬 선택적)
	PageResponse<ResponseProductReadDTO> getProductsByCategory(Pageable pageable, Long categoryId, ProductSortType sort);
}
