package com.nhnacademy.front.product.product.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.product.product.model.dto.response.ResponseProductCouponDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

@FeignClient(name = "product-service", url = "${product.book.admin.url}")
public interface ProductAdminAdaptor {
	@PostMapping("/self")
	ResponseEntity<Void> postCreateProduct(@RequestBody RequestProductCreateDTO request);

	@PutMapping("/{bookId}")
	ResponseEntity<Void> putUpdateProduct(@PathVariable Long bookId, @RequestBody RequestProductUpdateDTO request);

	@PutMapping("/{bookId}/stock")
	ResponseEntity<Void> putUpdateProductStock(@PathVariable Long bookId, @RequestBody RequestProductStockUpdateDTO request);

	@PutMapping("/{bookId}/sale-price")
	ResponseEntity<Void> putUpdateProductSalePrice(@PathVariable Long bookId, @RequestBody RequestProductSalePriceUpdateDTO request);


	@GetMapping("/order")
	ResponseEntity<List<ResponseProductReadDTO>> getProducts(@RequestParam("products") List<Long> products);

	@GetMapping("/state-sale")
	ResponseEntity<Page<ResponseProductCouponDTO>> getProductsToCoupon(Pageable pageable);

}
