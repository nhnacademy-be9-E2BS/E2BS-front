package com.nhnacademy.front.elasticsearch.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

@FeignClient(name = "gateway-service", contextId = "product-search-service")
public interface ProductSearchAdaptor {
	@GetMapping("/api/books/elasticsearch/search")
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProductsBySearch(Pageable pageable, @RequestParam String keyword, @RequestParam(required = false) ProductSortType sort, @RequestParam String memberId);

	@GetMapping("/api/books/elasticsearch/category/{categoryId}")
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProductsByCategory(Pageable pageable, @PathVariable Long categoryId, @RequestParam(required = false) ProductSortType sort, @RequestParam String memberId);

	@GetMapping("/api/books/elasticsearch/main/best")
	ResponseEntity<List<ResponseMainPageProductDTO>> getBestProductsByMain();

	@GetMapping("/api/books/elasticsearch/main/newest")
	ResponseEntity<List<ResponseMainPageProductDTO>> getNewestProductsByMain();

	@GetMapping("/api/books/elasticsearch/best")
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getBestProducts(Pageable pageable, @RequestParam String memberId);

	@GetMapping("/api/books/elasticsearch/newest")
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getNewestProducts(Pageable pageable, @RequestParam String memberId);
}
