package com.nhnacademy.front.product.category.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;

@FeignClient(name = "user-category-service", url = "${product.category.user.url}")
public interface UserCategoryAdaptor {

	@GetMapping
	ResponseEntity<List<ResponseCategoryDTO>> getCategoriesToDepth3();

	@GetMapping("{/categoryId}")
	ResponseEntity<List<ResponseCategoryDTO>> getCategoriesById(@PathVariable("categoryId") Long categoryId);
}
