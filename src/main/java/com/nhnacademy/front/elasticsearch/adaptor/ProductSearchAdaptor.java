package com.nhnacademy.front.elasticsearch.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

@FeignClient(name = "product-search-service", url = "${search.product.url}")
public interface ProductSearchAdaptor {
	@GetMapping("/search")
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProductsBySearch(Pageable pageable, @RequestParam String keyword, @RequestParam(required = false) ProductSortType sort);

	@GetMapping("/category/{categoryId}")
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProductsByCategory(Pageable pageable, @PathVariable Long categoryId, @RequestParam(required = false) ProductSortType sort);
}
