package com.nhnacademy.front.product.product.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

public interface ProductService {
	//공통 - 도서 한권 상세 조회
	ResponseProductReadDTO getProduct(long productId);

	//공통 - 카테고리별 도서 여러권 페이지로 조회
	PageResponse<ResponseProductReadDTO> getProductsByCategoryId(Pageable pageable, long categoryId);

}
