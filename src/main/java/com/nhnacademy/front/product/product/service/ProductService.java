package com.nhnacademy.front.product.product.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateByQueryDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;

public interface ProductService {
	//공통 - 도서 한권 상세 조회
	ResponseProductReadDTO getProduct(long productId);

	//공통 - 카테고리별 도서 여러권 페이지로 조회
	PageResponse<ResponseProductReadDTO> getProductsByCategoryId(Pageable pageable, long categoryId);
	PageResponse<ResponseProductsApiSearchDTO> getProductsApi(RequestProductApiSearchDTO request, Pageable pageable);

	PageResponse<ResponseProductsApiSearchByQueryTypeDTO> getProductsApi(RequestProductApiSearchByQueryTypeDTO request, Pageable pageable);


	void createProductApi(RequestProductApiCreateDTO request);

	void createProductQueryApi(RequestProductApiCreateByQueryDTO request);
}
