package com.nhnacademy.front.cart.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.CartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestCartCountDTO;
import com.nhnacademy.front.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartAdaptor cartAdaptor;

	@Override
	public Integer getCartItemsCounts(RequestCartCountDTO request) {
		ResponseEntity<Integer>
			result = cartAdaptor.getCartItemsCounts(request.getMemberId(), request.getSessionId());

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new CartProcessException("Cart processing failed");
		}

		return result.getBody();
	}

}
