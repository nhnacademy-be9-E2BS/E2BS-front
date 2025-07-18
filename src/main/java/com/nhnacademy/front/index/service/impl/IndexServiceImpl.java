package com.nhnacademy.front.index.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.elasticsearch.adaptor.ProductSearchAdaptor;
import com.nhnacademy.front.index.adapter.IndexAdaptor;
import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;
import com.nhnacademy.front.index.service.IndexService;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

	private final IndexAdaptor indexAdaptor;
	private final ProductSearchAdaptor productSearchAdaptor;

	private List<ResponseMainPageProductDTO> handleResponse(ResponseEntity<List<ResponseMainPageProductDTO>> response) {
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException();
		}
		return response.getBody();
	}

	@Override
	public List<ResponseMainPageProductDTO> getBestSellerProducts() {
		return handleResponse(productSearchAdaptor.getBestProductsByMain());
	}

	@Override
	public List<ResponseMainPageProductDTO> getBlogBestProducts() {
		return handleResponse(indexAdaptor.getBlogBestProducts());
	}

	@Override
	public List<ResponseMainPageProductDTO> getNewItemsProducts() {
		return handleResponse(productSearchAdaptor.getNewestProductsByMain());
	}

	@Override
	public List<ResponseMainPageProductDTO> getNewSpecialItemsProducts() {
		return handleResponse(indexAdaptor.getNewSpecialItemsProducts());
	}
}
