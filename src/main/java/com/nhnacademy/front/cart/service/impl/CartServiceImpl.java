package com.nhnacademy.front.cart.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.CartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartOrderDTO;
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
				throw new CartProcessException("회원 장바구니 개수 조회 실패");
			}
			return result.getBody();
		} catch (FeignException ex) {
			throw new CartProcessException("회원 장바구니 개수 조회 실패" + ex.getMessage());
		}

	}

	@Override
	public Integer mergeCartItemsToMemberFromGuest(RequestMergeCartItemDTO request) {
		try {
			ResponseEntity<Integer> result = cartAdaptor.mergeCartItemsToMemberFromGuest(request);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("게스트 장바구니 -> 회원 장바구니에 병합 실패");
			}
			return result.getBody();
		}
	 	catch (FeignException ex) {
				throw new CartProcessException("게스트 장바구니 -> 회원 장바구니에 병합 실패" + ex.getMessage());
		}
	}

	@Override
	public Integer deleteOrderCompleteCartItems(RequestDeleteCartOrderDTO request) {
		try {
			ResponseEntity<Integer> result = cartAdaptor.deleteOrderCompleteCartItems(request);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("주문한 상품을 장바구니에서 삭제 실패");
			}

			return result.getBody();
		}
		catch (FeignException ex) {
			throw new CartProcessException("주문한 상품을 장바구니에서 삭제 실패" + ex.getMessage());
		}
	}

}
