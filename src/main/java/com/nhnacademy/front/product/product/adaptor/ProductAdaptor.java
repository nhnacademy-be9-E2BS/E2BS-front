package com.nhnacademy.front.product.product.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

@FeignClient(name = "gateway-service", contextId = "product-service")
public interface ProductAdaptor {

	@GetMapping("/api/books/{book-id}")
	ResponseEntity<ResponseProductReadDTO> getProductById(@PathVariable("book-id") Long bookId,
		@RequestParam("memberId") String memberId);

	@GetMapping("/api/books/order")
	ResponseEntity<List<ResponseProductReadDTO>> getProducts(@RequestParam("products") List<Long> products);

	@GetMapping("/api/books/{book-id}/recommend-books")
	ResponseEntity<List<ResponseProductReadDTO>> getRecommendedProducts(@PathVariable("book-id") Long bookId,
		@RequestParam("memberId") String memberId);
}
