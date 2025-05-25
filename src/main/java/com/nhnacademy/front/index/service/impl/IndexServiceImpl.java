package com.nhnacademy.front.index.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.index.adapter.IndexAdaptor;
import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;
import com.nhnacademy.front.index.service.IndexService;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

	private final IndexAdaptor indexAdaptor;

	private List<ResponseMainPageProductDTO> handleResponse(ResponseEntity<List<ResponseMainPageProductDTO>> response) {
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException("도서 조회 실패");
		}
		return response.getBody();
	}

	@Override
	public List<ResponseMainPageProductDTO> getBestSellerProducts() {
		return handleResponse(indexAdaptor.getBestSellerProducts());
	}

	@Override
	public List<ResponseMainPageProductDTO> getBlogBestProducts() {
		return handleResponse(indexAdaptor.getBlogBestProducts());
	}

	@Override
	public List<ResponseMainPageProductDTO> getNewItemsProducts() {
		return handleResponse(indexAdaptor.getNewItemsProducts());
	}

	@Override
	public List<ResponseMainPageProductDTO> getNewSpecialItemsProducts() {
		return handleResponse(indexAdaptor.getNewSpecialItemsProducts());
	}
}
