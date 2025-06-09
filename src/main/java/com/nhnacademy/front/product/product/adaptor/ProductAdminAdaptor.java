package com.nhnacademy.front.product.product.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateByQueryDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductMetaDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;

@FeignClient(name = "product-admin-service", url = "${auth.product.book.url}")
public interface ProductAdminAdaptor {

	@GetMapping
	ResponseEntity<PageResponse<ResponseProductReadDTO>> getProducts(Pageable pageable);

	@GetMapping("/register")
	ResponseEntity<Void> getRegisterView();

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<Void> postCreateProduct(@RequestPart("requestMeta") RequestProductMetaDTO requestMeta,
		@RequestPart("productImage") List<MultipartFile> productImage);

	@PutMapping(value = "/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<Void> putUpdateProduct(@PathVariable Long bookId,
		@RequestPart("product") RequestProductMetaDTO requestMeta,
		@RequestPart("productImage") List<MultipartFile> productImage);

	@PutMapping("/{bookId}/salePrice")
	ResponseEntity<Void> putUpdateProductSalePrice(@PathVariable Long bookId,
		@RequestBody RequestProductSalePriceUpdateDTO request);

	// 일반 검색 (query + queryType)
	@GetMapping("/aladdin/search")
	ResponseEntity<PageResponse<ResponseProductsApiSearchDTO>> searchBooksByQuery(
		@SpringQueryMap RequestProductApiSearchDTO request, Pageable pageable);

	// 리스트 검색 (query 없음, Bestseller, NewBook 등)
	@GetMapping("/aladdin/search")
	ResponseEntity<PageResponse<ResponseProductsApiSearchByQueryTypeDTO>> listBooksByCategory(
		@SpringQueryMap RequestProductApiSearchByQueryTypeDTO request, Pageable pageable);

	@PostMapping("/aladdin/register")
	ResponseEntity<Void> postCreateProductByApi(@RequestBody RequestProductApiCreateDTO request);

	@PostMapping("/aladdin/register/list")
	ResponseEntity<Void> postCreateProductQueryByApi(@RequestBody RequestProductApiCreateByQueryDTO request);

}
