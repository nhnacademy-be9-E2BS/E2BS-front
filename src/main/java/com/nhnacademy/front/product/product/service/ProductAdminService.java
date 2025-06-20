package com.nhnacademy.front.product.product.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateByQueryDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;

public interface ProductAdminService {
	//관리자 - 도서 DB에 저장
	void createProduct(RequestProductDTO request);

	//관리자 - 전체 도서 페이지로 조회
	PageResponse<ResponseProductReadDTO> getProducts(Pageable pageable);

	//관리자 - 전체 도서에서 검색하여 페이지로 조회
	PageResponse<ResponseProductReadDTO> getProductsBySearch(Pageable pageable, String keyword);

	//관리자 - 도서 수정
	void updateProduct(long productId, RequestProductDTO request);

	//관리자 - 판매가 수정
	void updateProductSalePrice(long productId, RequestProductSalePriceUpdateDTO request);

	PageResponse<ResponseProductsApiSearchDTO> getProductsApi(RequestProductApiSearchDTO request, Pageable pageable);

	PageResponse<ResponseProductsApiSearchByQueryTypeDTO> getProductsApi(RequestProductApiSearchByQueryTypeDTO request, Pageable pageable);


	void createProductApi(RequestProductApiCreateDTO request);

	void createProductQueryApi(RequestProductApiCreateByQueryDTO request);
}
