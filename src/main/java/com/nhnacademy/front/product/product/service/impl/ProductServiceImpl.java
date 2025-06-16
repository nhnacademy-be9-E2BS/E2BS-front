package com.nhnacademy.front.product.product.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.product.product.adaptor.ProductAdaptor;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductAdaptor productAdaptor;

	/**
	 * product 단일 조회 (상세 조회)
	 */
	@Override
	public ResponseProductReadDTO getProduct(long productId, String memberId) {
		try {
			ResponseEntity<ResponseProductReadDTO> response = productAdaptor.getProductById(productId, memberId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException();
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException();
		}
	}

	/**
	 * order 전용 - 도서 여러권 리스트 조회
	 */
	@Override
	public List<ResponseProductReadDTO> getProducts(List<Long> productIds) {
		try {
			ResponseEntity<List<ResponseProductReadDTO>> response = productAdaptor.getProducts(productIds);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException();
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException();
		}
	}

	/**
	 * product 단일 조회 (상세 조회)
	 */
	@Override
	public List<ResponseProductReadDTO> getRecommendedProducts(long productId, String memberId) {
		try {
			ResponseEntity<List<ResponseProductReadDTO>> response = productAdaptor.getRecommendedProducts(productId,
				memberId);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new ProductGetProcessException();
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new ProductGetProcessException();
		}
	}
}