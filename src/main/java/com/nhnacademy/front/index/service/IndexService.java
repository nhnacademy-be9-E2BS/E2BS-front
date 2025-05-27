package com.nhnacademy.front.index.service;

import java.util.List;

import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;

public interface IndexService {
	List<ResponseMainPageProductDTO> getBestSellerProducts();
	List<ResponseMainPageProductDTO> getBlogBestProducts();
	List<ResponseMainPageProductDTO> getNewItemsProducts();
	List<ResponseMainPageProductDTO> getNewSpecialItemsProducts();
}
