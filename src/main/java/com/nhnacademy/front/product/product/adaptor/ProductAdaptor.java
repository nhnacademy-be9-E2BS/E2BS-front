package com.nhnacademy.front.product.product.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

@FeignClient(name = "product-service", url = "${product.book.url}")
public interface ProductAdaptor {

	@GetMapping("{book-id}")
	ResponseEntity<ResponseProductReadDTO> getProductById(@PathVariable("book-id") Long bookId, @RequestParam("memberId") String memberId);

	@GetMapping("/order")
	ResponseEntity<List<ResponseProductReadDTO>> getProducts(@RequestParam("products") List<Long> products);
}
