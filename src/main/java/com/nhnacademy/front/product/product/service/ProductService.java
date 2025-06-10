package com.nhnacademy.front.product.product.service;

import java.util.List;

import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

public interface ProductService {
	//공통 - 도서 한권 상세 조회
	ResponseProductReadDTO getProduct(long productId, String memberId);

	//Order전용 - 도서 여러권 리스트로 조회
	List<ResponseProductReadDTO> getProducts(List<Long> productIds);
}
