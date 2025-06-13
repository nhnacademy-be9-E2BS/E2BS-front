package com.nhnacademy.front.index.adapter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;

@FeignClient(name = "gateway-service", contextId = "index-service")
public interface IndexAdaptor {
	@GetMapping("/api/category/bestseller")
	ResponseEntity<List<ResponseMainPageProductDTO>> getBestSellerProducts();

	@GetMapping("/api/category/blogbest")
	ResponseEntity<List<ResponseMainPageProductDTO>> getBlogBestProducts();

	@GetMapping("/api/category/newitems")
	ResponseEntity<List<ResponseMainPageProductDTO>> getNewItemsProducts();


	@GetMapping("/api/category/newspecialitems")
	ResponseEntity<List<ResponseMainPageProductDTO>> getNewSpecialItemsProducts();
}
