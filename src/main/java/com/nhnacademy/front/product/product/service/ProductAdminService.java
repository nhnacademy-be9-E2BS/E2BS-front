package com.nhnacademy.front.product.product.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

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

public interface ProductAdminService {
	//관리자 - 도서 DB에 저장
	void createProduct(RequestProductDTO request);

	//관리자 - 전체 도서 페이지로 조회
	PageResponse<ResponseProductReadDTO> getProducts(Pageable pageable);

	//Order전용 - 도서 여러권 리스트로 조회
	List<ResponseProductReadDTO> getProducts(List<Long> productIds);

	//관리자 - 도서 수정
	void updateProduct(long productId, RequestProductDTO request);

	//관리자 - 재고 수동 수정
	void updateProductStock(long productId, RequestProductStockUpdateDTO request);

	//관리자 - 판매가 수정
	void updateProductSalePrice(long productId, RequestProductSalePriceUpdateDTO request);

	//Coupon전용 - Sale중인 전체 도서 페이지로 조회
	PageResponse<ResponseProductCouponDTO> getProductsToCoupon(Pageable pageable);

	PageResponse<ResponseProductsApiSearchDTO> getProductsApi(RequestProductApiSearchDTO request, Pageable pageable);

	PageResponse<ResponseProductsApiSearchByQueryTypeDTO> getProductsApi(RequestProductApiSearchByQueryTypeDTO request, Pageable pageable);


	void createProductApi(RequestProductApiCreateDTO request);

	void createProductQueryApi(RequestProductApiCreateByQueryDTO request);
}
