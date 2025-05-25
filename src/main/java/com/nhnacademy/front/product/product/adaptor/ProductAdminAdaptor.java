package com.nhnacademy.front.product.product.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
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
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductStockUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductCouponDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchByQueryTypeDTO;

@FeignClient(name = "product-admin-service", url = "${product.book.admin.url}")
public interface ProductAdminAdaptor {
	@PostMapping("/register")
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
	//
	// @GetMapping("/aladdin/search")
	// ResponseEntity<PageResponse<ResponseProductsApiSearchDTO>> searchProducts(@SpringQueryMap RequestProductApiSearchDTO request, Pageable pageable);

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
