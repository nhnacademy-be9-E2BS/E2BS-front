package com.nhnacademy.front.product.category.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;

@FeignClient(name = "admin-category-service", url = "${product.category.admin.url}")
public interface AdminCategoryAdaptor {

	@PostMapping
	ResponseEntity<Void> postCreateCategoryTree(@RequestBody List<RequestCategoryDTO> request);

	@PostMapping("/{categoryId}")
	ResponseEntity<Void> postCreateChildCategory(@PathVariable("categoryId") Long categoryId,
		@RequestBody RequestCategoryDTO request);

	@PutMapping("/{categoryId}")
	ResponseEntity<Void> putUpdateCategory(@PathVariable("categoryId") Long categoryId,
		@RequestBody RequestCategoryDTO request);

	@DeleteMapping("/{categoryId}")
	ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId);

	@DeleteMapping("/headerCaching")
	ResponseEntity<Void> headerCachingClear();
}
