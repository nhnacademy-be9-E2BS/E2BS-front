package com.nhnacademy.front.product.category.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryIdsDTO;

@FeignClient(name = "gateway-service", contextId = "user-category-service")
public interface UserCategoryAdaptor {

	@GetMapping("/api/categories")
	ResponseEntity<List<ResponseCategoryDTO>> getCategoriesToDepth3();

	@GetMapping("/api/categories/all")
	ResponseEntity<List<ResponseCategoryDTO>> getAllCategories();

	@GetMapping("/api/categories/productIds")
	ResponseEntity<List<ResponseCategoryIdsDTO>> getCategoriesByProductIds(@RequestParam List<Long> productIds);
}
