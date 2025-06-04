package com.nhnacademy.front.product.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.product.adaptor.ProductAdminAdaptor;
import com.nhnacademy.front.product.product.exception.ProductCreateProcessException;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.exception.ProductUpdateProcessException;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateByQueryDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductMetaDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductStockUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductCouponDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;
import com.nhnacademy.front.product.product.service.ProductAdminService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductAdminServiceImpl implements ProductAdminService {

	private final ProductAdminAdaptor productAdminAdaptor;

	/**
	 * product를 back에서 저장 (관계 테이블들도)
	 */
	@Override
	public void createProduct(RequestProductDTO request) throws FeignException {
		RequestProductMetaDTO requestMeta = new RequestProductMetaDTO(request.getProductStateId(),
			request.getPublisherId(), request.getProductTitle(), request.getProductContent(),
			request.getProductDescription(), request.getProductPublishedAt(), request.getProductIsbn(),
			request.getProductRegularPrice(), request.getProductSalePrice(), request.isProductPackageable(),
			request.getProductStock(), request.getTagIds(), request.getCategoryIds(), request.getContributorIds());
		
		ResponseEntity<Void> response = productAdminAdaptor.postCreateProduct(requestMeta,
			request.getProductImages());

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductCreateProcessException("도서 등록 실패");
		}
	}

	/**
	 * 전체 product 리스트 페이징 조회
	 */
	@Override
	public PageResponse<ResponseProductReadDTO> getProducts(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseProductReadDTO>> response = productAdminAdaptor.getProducts(pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException("전체 도서 조회 실패");
		}
		return response.getBody();
	}

	/**
	 * 도서 수정
	 */
	@Override
	public void updateProduct(long productId, RequestProductDTO request) throws FeignException {
		RequestProductMetaDTO requestMeta = new RequestProductMetaDTO(request.getProductStateId(),
			request.getPublisherId(), request.getProductTitle(), request.getProductContent(),
			request.getProductDescription(), request.getProductPublishedAt(), request.getProductIsbn(),
			request.getProductRegularPrice(), request.getProductSalePrice(), request.isProductPackageable(),
			request.getProductStock(), request.getTagIds(), request.getCategoryIds(), request.getContributorIds());
		ResponseEntity<Void> response = productAdminAdaptor.putUpdateProduct(productId, requestMeta,
			request.getProductImages());

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductUpdateProcessException("도서 정보 수정 실패");
		}
	}

	/**
	 * 도서 재고 수정
	 */
	@Override
	public void updateProductStock(long productId, RequestProductStockUpdateDTO request) throws FeignException {
		ResponseEntity<Void> response = productAdminAdaptor.putUpdateProductStock(productId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductUpdateProcessException("도서 재고 수정 실패");
		}
	}

	/**
	 * 도서 판매가 수정
	 */
	@Override
	public void updateProductSalePrice(long productId, RequestProductSalePriceUpdateDTO request) throws FeignException {
		ResponseEntity<Void> response = productAdminAdaptor.putUpdateProductSalePrice(productId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductUpdateProcessException("도서 판매가 수정 실패");
		}
	}

	/**
	 * Coupon 전용 - Sale중인 전체 도서 페이지로 조회
	 */
	@Override
	public PageResponse<ResponseProductCouponDTO> getProductsToCoupon(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseProductCouponDTO>> response = productAdminAdaptor.getProductsToCoupon(
			pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException("coupon 전용 Sale 상태 도서 리스트 조회 실패");
		}
		return response.getBody();
	}

	/**
	 * 알라딘 api로 검색어, 검색타입에 따른 결과 조회
	 */
	@Override
	public PageResponse<ResponseProductsApiSearchDTO> getProductsApi(RequestProductApiSearchDTO request,
		Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseProductsApiSearchDTO>> response = productAdminAdaptor.searchBooksByQuery(
			request, pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException("도서 조회 실패");
		}
		return response.getBody();
	}

	/**
	 * 알라딘 api로 카테고리로 따른 결과 조회
	 */
	@Override
	public PageResponse<ResponseProductsApiSearchByQueryTypeDTO> getProductsApi(
		RequestProductApiSearchByQueryTypeDTO request, Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseProductsApiSearchByQueryTypeDTO>> response = productAdminAdaptor.listBooksByCategory(
			request, pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException("도서 조회 실패");
		}
		return response.getBody();
	}

	/**
	 * 알라딘 api로 검색어, 검색타입으로 책 등록
	 */
	@Override
	public void createProductApi(RequestProductApiCreateDTO request) throws FeignException {
		if (request.getTagIds() == null) {
			request.setTagIds(new ArrayList<>());
		}
		ResponseEntity<Void> response = productAdminAdaptor.postCreateProductByApi(request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductCreateProcessException("도서 등록 실패");
		}
	}

	/**
	 * 알라딘 api로 카테고리로 따른 책 등록
	 */
	@Override
	public void createProductQueryApi(RequestProductApiCreateByQueryDTO request) throws FeignException {
		ResponseEntity<Void> response = productAdminAdaptor.postCreateProductQueryByApi(request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductCreateProcessException("도서 등록 실패");
		}
	}

}
