package com.nhnacademy.front.product.product.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductStockUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductCouponDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

@FeignClient(name = "product-admin-service", url = "${product.book.admin.url}")
public interface ProductAdminAdaptor {
	@PostMapping
	ResponseEntity<Void> postCreateProduct(@RequestBody RequestProductDTO request);

	@GetMapping
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProducts(@SpringQueryMap Pageable pageable);

	@GetMapping("/order")
	ResponseEntity<List<ResponseProductReadDTO>> getProducts(@RequestParam("products") List<Long> products);

	@PutMapping("/{bookId}")
	ResponseEntity<Void> putUpdateProduct(@PathVariable Long bookId, @RequestBody RequestProductDTO request);

	@PutMapping("/{bookId}/stock")
	ResponseEntity<Void> putUpdateProductStock(@PathVariable Long bookId, @RequestBody RequestProductStockUpdateDTO request);

	@PutMapping("/{bookId}/salePrice")
	ResponseEntity<Void> putUpdateProductSalePrice(@PathVariable Long bookId, @RequestBody RequestProductSalePriceUpdateDTO request);

	@GetMapping("/states/sale")
	ResponseEntity<PageResponse<ResponseProductCouponDTO>> getProductsToCoupon(@SpringQueryMap Pageable pageable);

}
