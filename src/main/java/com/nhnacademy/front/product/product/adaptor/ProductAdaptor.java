package com.nhnacademy.front.product.product.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

@FeignClient(name = "product-service", url = "${product.book.member.url}")
public interface ProductAdaptor {
	@GetMapping("/category/{categoryId}")
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProductsByCategory(Pageable pageable, @PathVariable("categoryId") Long categoryId);

	@GetMapping("{bookId}")
	ResponseEntity<ResponseProductReadDTO> getProductById(@PathVariable("bookId") Long bookId);
}
