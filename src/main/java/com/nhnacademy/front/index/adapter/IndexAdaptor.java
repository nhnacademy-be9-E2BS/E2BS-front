package com.nhnacademy.front.index.adapter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;

@FeignClient(name = "index-service", url = "${index.url}")
public interface IndexAdaptor {
	@GetMapping("/bestseller")
	ResponseEntity<List<ResponseMainPageProductDTO>> getBestSellerProducts();

	@GetMapping("/blogbest")
	ResponseEntity<List<ResponseMainPageProductDTO>> getBlogBestProducts();

	@GetMapping("/newitems")
	ResponseEntity<List<ResponseMainPageProductDTO>> getNewItemsProducts();


	@GetMapping("/newspecialitems")
	ResponseEntity<List<ResponseMainPageProductDTO>> getNewSpecialItemsProducts();
}
