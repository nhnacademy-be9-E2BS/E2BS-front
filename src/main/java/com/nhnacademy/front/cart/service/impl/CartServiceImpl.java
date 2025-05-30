package com.nhnacademy.front.cart.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.CartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.MergeCartItemDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestCartCountDTO;
import com.nhnacademy.front.cart.service.CartService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartAdaptor cartAdaptor;

	@Override
	public Integer getCartItemsCounts(RequestCartCountDTO request) {
		try {
			ResponseEntity<Integer>
				result = cartAdaptor.getCartItemsCounts(request.getMemberId(), request.getSessionId());

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("Cart processing failed");
			}
			return result.getBody();
		} catch (FeignException ex) {
			throw new CartProcessException("Cart processing failed" + ex.getMessage());
		}

	}

	@Override
	public Integer mergeCartItemsToMemberFromGuest(List<MergeCartItemDTO> cartItemDTO) {
		try {
			ResponseEntity<Integer> result = cartAdaptor.mergeCartItemsToMemberFromGuest(cartItemDTO);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("Cart processing failed");
			}
			return result.getBody();
		}
	 	catch (FeignException ex) {
				throw new CartProcessException("Cart processing failed" + ex.getMessage());
		}
	}

}
