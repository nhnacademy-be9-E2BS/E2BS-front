package com.nhnacademy.front.cart.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.CustomerCartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.ResponseCartItemsForCustomerDTO;
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



}
