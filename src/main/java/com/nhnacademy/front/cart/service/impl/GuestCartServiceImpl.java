package com.nhnacademy.front.cart.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.GuestCartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;
import com.nhnacademy.front.cart.service.GuestCartService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestCartServiceImpl implements GuestCartService {

	private final GuestCartAdaptor guestCartAdaptor;

	@Override
	public List<ResponseCartItemsForGuestDTO> getCartItemsByGuest(String sessionId) {
		try {
			ResponseEntity<List<ResponseCartItemsForGuestDTO>> result = guestCartAdaptor.getCartItemsByGuest(sessionId);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("게스트 장바구니 목록 조회 실패: " + result.getStatusCode());
			}
			return result.getBody();
		} catch (FeignException ex) {
			throw new CartProcessException("게스트 장바구니 목록 조회 실패: " + ex.getMessage());
		}
	}

	@Override
	public void createCartItemForGuest(RequestAddCartItemsDTO request) {
		try {
			ResponseEntity<Void> result = guestCartAdaptor.createCartItemForGuest(request);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("게스트 장바구니 항목 추가 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("게스트 장바구니 항목 추가 실패: " + ex.getMessage());
		}
	}

	@Override
	public void updateCartItemForGuest(RequestUpdateCartItemsDTO requestDto) {
		try {
			ResponseEntity<Void> result = guestCartAdaptor.updateCartItemForGuest(requestDto);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("게스트 장바구니 항목 수량 변경 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("게스트 장바구니 항목 수량 변경 실패: " + ex.getMessage());
		}
	}

	@Override
	public void deleteCartItemForGuest(RequestDeleteCartItemsForGuestDTO request) {
		try {
			ResponseEntity<Void> result = guestCartAdaptor.deleteCartItemForGuest(request);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("게스트 장바구니 항목 삭제 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("게스트 장바구니 항목 삭제 실패: " + ex.getMessage());
		}
	}

	@Override
	public void deleteCartForGuest(String sessionId) {
		try {
			ResponseEntity<Void> result = guestCartAdaptor.deleteCartForGuest(sessionId);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("게스트 장바구니 전체 삭제 실패: " + result.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CartProcessException("게스트 장바구니 전체 삭제 실패: " + ex.getMessage());
		}
	}

}
