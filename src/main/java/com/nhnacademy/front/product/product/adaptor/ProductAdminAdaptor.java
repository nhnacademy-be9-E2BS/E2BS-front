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
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateByQueryDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductStockUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductCouponDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;

@FeignClient(name = "product-admin-service", url = "${product.book.admin.url}")
public interface ProductAdminAdaptor {
	@PostMapping

	@GetMapping
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProducts(@SpringQueryMap Pageable pageable);

	@GetMapping("/order")
	ResponseEntity<List<ResponseProductReadDTO>> getProducts(@RequestParam("products") List<Long> products);

	@GetMapping("/register")
	ResponseEntity<Void> getRegisterView();

	@PostMapping("/register")
	ResponseEntity<Void> postCreateProduct(@RequestBody RequestProductDTO request);

	@PutMapping("/{bookId}")
	ResponseEntity<Void> putUpdateProduct(@PathVariable Long bookId, @RequestBody RequestProductDTO request);

	@PutMapping("/{bookId}/stock")
	ResponseEntity<Void> putUpdateProductStock(@PathVariable Long bookId, @RequestBody RequestProductStockUpdateDTO request);

	@PutMapping("/{bookId}/salePrice")
	ResponseEntity<Void> putUpdateProductSalePrice(@PathVariable Long bookId, @RequestBody RequestProductSalePriceUpdateDTO request);

	@GetMapping("/states/sale")
	ResponseEntity<PageResponse<ResponseProductCouponDTO>> getProductsToCoupon(@SpringQueryMap Pageable pageable);

	// 일반 검색 (query + queryType)
	@GetMapping("/aladdin/search")
	ResponseEntity<PageResponse<ResponseProductsApiSearchDTO>> searchBooksByQuery(@SpringQueryMap RequestProductApiSearchDTO request, Pageable pageable);

	// 리스트 검색 (query 없음, Bestseller, NewBook 등)
	@GetMapping("/aladdin/search")
	ResponseEntity<PageResponse<ResponseProductsApiSearchByQueryTypeDTO>> listBooksByCategory(@SpringQueryMap RequestProductApiSearchByQueryTypeDTO request, Pageable pageable);


	@PostMapping("/aladdin/register")
	ResponseEntity<Void> postCreateProductByApi(@RequestBody RequestProductApiCreateDTO request);


	@PostMapping("/aladdin/register/list")
	ResponseEntity<Void> postCreateProductQueryByApi(@RequestBody RequestProductApiCreateByQueryDTO request);
}
