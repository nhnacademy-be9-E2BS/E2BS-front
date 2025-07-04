package com.nhnacademy.front.cart.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.cart.adaptor.MemberCartAdaptor;
import com.nhnacademy.front.cart.adaptor.MemberRegisterCartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForMemberDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;
import com.nhnacademy.front.cart.service.MemberCartService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCartServiceImpl implements MemberCartService {

	private final MemberRegisterCartAdaptor memberRegisterCartAdaptor;
	private final MemberCartAdaptor memberCartAdaptor;

	@Override
	public void createCartByMember(String memberId) {
		try {
			ResponseEntity<Void> result = memberRegisterCartAdaptor.createCartByMember(memberId);

			if (!result.getStatusCode().is2xxSuccessful()) {
				throw new CartProcessException("회원 장바구니 생성 실패: " + result.getStatusCode());
			}
		} catch (FeignException e) {
			throw new CartProcessException("회원 장바구니 생성 실패: " + e.getMessage());
		}
	}

	@Override
	public List<ResponseCartItemsForMemberDTO> getCartItemsByMember(String memberId) throws FeignException {
		ResponseEntity<List<ResponseCartItemsForMemberDTO>> result = memberCartAdaptor.getCartItemsByMember(memberId);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new CartProcessException("회원 장바구니 조회 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

	@Override
	public Integer createCartItemForMember(RequestAddCartItemsDTO requestDto) throws FeignException {
		ResponseEntity<Integer> result = memberCartAdaptor.createCartItemForMember(requestDto);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new CartProcessException("회원 장바구니 항목 추가 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

	@Override
	public Integer updateCartItemForMember(RequestUpdateCartItemsDTO requestDto) throws FeignException {
		ResponseEntity<Integer> result = memberCartAdaptor.updateCartItemForMember(requestDto);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new CartProcessException("회원 장바구니 항목 수량 변경 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

	@Override
	public void deleteCartItemForMember(RequestDeleteCartItemsForMemberDTO requestDto) throws FeignException {
		ResponseEntity<Void> result = memberCartAdaptor.deleteCartItemForMember(requestDto);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new CartProcessException("회원 장바구니 항목 삭제 실패: " + result.getStatusCode());
		}
	}

	@Override
	public void deleteCartForMember(String memberId) throws FeignException {
		ResponseEntity<Void> result = memberCartAdaptor.deleteCartForMember(memberId);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new CartProcessException("회원 장바구니 전체 삭제 실패: " + result.getStatusCode());
		}
	}

}
