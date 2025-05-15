package com.nhnacademy.front.cart.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.CustomerCartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForCustomerDTO;
import com.nhnacademy.front.cart.service.CustomerCartService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerCartServiceImpl implements CustomerCartService {

	private final CustomerCartAdaptor customerCartAdaptor;

	@Override
	public List<ResponseCartItemsForCustomerDTO> getCartItemsByCustomer(long customerId) {
		try {
			ResponseEntity<List<ResponseCartItemsForCustomerDTO>> result = customerCartAdaptor.getCartItemsByCustomer(customerId);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("비회원/회원 장바구니 조회 실패: " + result.getStatusCode());
			}
			return result.getBody();
		} catch (FeignException ex) {
			throw new CartProcessException("비회원/회원 장바구니 조회 실패: " + ex.getMessage());
		}
	}

	@Override
	public void createCartItemForCustomer(RequestAddCartItemsDTO requestDto) {
		try {
			ResponseEntity<Void> result = customerCartAdaptor.createCartItemForCustomer(requestDto);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("비회원/회원 장바구니 항목 추가 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("비회원/회원 장바구니 항목 추가 실패: " + ex.getMessage());
		}
	}

	@Override
	public void updateCartItemForCustomer(long cartItemId, RequestUpdateCartItemsDTO requestDto) {
		try {
			ResponseEntity<Void> result = customerCartAdaptor.updateCartItemForCustomer(cartItemId, requestDto);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("비회원/회원 장바구니 항목 수량 변경 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("비회원/회원 장바구니 항목 수량 변경 실패: " + ex.getMessage());
		}
	}

	@Override
	public void deleteCartItemForCustomer(long cartItemId) {
		try {
			ResponseEntity<Void> result = customerCartAdaptor.deleteCartItemForCustomer(cartItemId);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("비회원/회원 장바구니 항목 삭제 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("비회원/회원 장바구니 항목 삭제 실패: " + ex.getMessage());
		}
	}

	@Override
	public void deleteCartForCustomer(long customerId) {
		try {
			ResponseEntity<Void> result = customerCartAdaptor.deleteCartForCustomer(customerId);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("비회원/회원 장바구니 전체 삭제 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("비회원/회원 장바구니 전체 삭제 실패: " + ex.getMessage());
		}
	}

}
