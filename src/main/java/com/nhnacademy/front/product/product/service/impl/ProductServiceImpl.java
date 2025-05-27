package com.nhnacademy.front.product.product.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


	@Override
	public ResponseProductReadDTO getProduct(long productId) {
		return null;
	}

	@Override
	public PageResponse<ResponseProductReadDTO> getProductsByCategoryId(Pageable pageable, long categoryId) {
		return null;
	}
}
