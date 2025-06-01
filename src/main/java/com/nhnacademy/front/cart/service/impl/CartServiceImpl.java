package com.nhnacademy.front.cart.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.CartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestMergeCartItemDTO;
import com.nhnacademy.front.cart.service.CartService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartAdaptor cartAdaptor;

	@Override
	public Integer getCartItemsCountsForMember(String memberId) {
		try {
			ResponseEntity<Integer> result = cartAdaptor.getCartItemsCounts(memberId);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("Cart processing failed");
			}
			return result.getBody();
		} catch (FeignException ex) {
			throw new CartProcessException("Cart processing failed" + ex.getMessage());
		}

	}

	@Override
	public Integer mergeCartItemsToMemberFromGuest(RequestMergeCartItemDTO request) {
		try {
			ResponseEntity<Integer> result = cartAdaptor.mergeCartItemsToMemberFromGuest(request.getMemberId(), request.getSessionId());

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
