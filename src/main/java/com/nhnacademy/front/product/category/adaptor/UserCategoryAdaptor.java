package com.nhnacademy.front.product.category.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryIdsDTO;

@FeignClient(name = "user-category-service", url = "${product.category.user.url}")
public interface UserCategoryAdaptor {

	@GetMapping
	ResponseEntity<List<ResponseCategoryDTO>> getCategoriesToDepth3();

	@GetMapping("/all")
	ResponseEntity<List<ResponseCategoryDTO>> getAllCategories();

	@GetMapping("/productIds")
	ResponseEntity<List<ResponseCategoryIdsDTO>> getCategoriesByProductIds(@RequestBody List<Long> productIds);
}
