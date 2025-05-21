package com.nhnacademy.front.product.product.service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.product.product.adaptor.ProductAdminAdaptor;
import com.nhnacademy.front.product.product.exception.ProductCreateProcessException;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.exception.ProductUpdateProcessException;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductGetDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductStockUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductCouponDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductAdminAdaptor productAdminAdaptor;

	/**
	 * Product 단건 조회(도서 상세페이지)
	 * Member
	 */
	@Override
	public ResponseProductReadDTO getProduct(long productId, RequestProductGetDTO request) throws FeignException {
		return null;
	}

	/**
	 * Product 페이지단위 조회
	 * Member
	 */
	@Override
	public Page<ResponseProductReadDTO> getProducts(Pageable pageable) throws FeignException {
		return null;
	}



	/**
	 * Product를 back - product 테이블에 저장
	 * Admin
	 */
	@Override
	public void createProduct(RequestProductCreateDTO request) throws FeignException {
		ResponseEntity<Void> response =productAdminAdaptor.postCreateProduct(request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductCreateProcessException("상품 등록 실패");
		}

	}

	/**
	 * Product 전체 정보 업데이트
	 * Admin
	 */
	@Override
	public void updateProduct(long productId, RequestProductUpdateDTO request) throws FeignException {
		ResponseEntity<Void> response = productAdminAdaptor.putUpdateProduct(productId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductUpdateProcessException("도서 수정 실패");
		}
	}

	/**
	 * Product 재고 수정 (필요 없으려나?)
	 * Admin
	 */
	@Override
	public void updateProductStock(long productId, RequestProductStockUpdateDTO request) throws FeignException {
		ResponseEntity<Void> response = productAdminAdaptor.putUpdateProductStock(productId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductUpdateProcessException("도서 재고 수정 실패");
		}
	}

	/**
	 * Product 판매가 수정 (필요 없으려나?)
	 * Admin
	 */
	@Override
	public void updateProductSalePrice(long productId, RequestProductSalePriceUpdateDTO request) throws FeignException {
		ResponseEntity<Void> response = productAdminAdaptor.putUpdateProductSalePrice(productId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductUpdateProcessException("도서 판매가 수정 실패");
		}
	}

	/**
	 * ProductState가 SALE인 것 페이지로 조회 (coupon전용)
	 * Admin
	 */
	@Override
	public Page<ResponseProductCouponDTO> getProductsToCoupon(Pageable pageable) throws FeignException {
		ResponseEntity<Page<ResponseProductCouponDTO>> response = productAdminAdaptor.getProductsToCoupon(pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException("SALE 중인 도서 조회 실패");
		}
		return response.getBody();
	}

	/**
	 * ProductId로 리스트 조회 (Order전용)
	 * Admin
	 */
	@Override
	public List<ResponseProductReadDTO> getProducts(List<Long> products) throws FeignException {
		ResponseEntity<List<ResponseProductReadDTO>> response = productAdminAdaptor.getProducts(products);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductGetProcessException("도서 리스트 조회 실패");
		}

		return response.getBody();
	}

}
