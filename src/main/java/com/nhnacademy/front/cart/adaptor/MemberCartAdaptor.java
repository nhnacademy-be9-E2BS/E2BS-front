package com.nhnacademy.front.cart.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;

@FeignClient(name = "customer-cart-adaptor", url = "${auth.member.url}")
public interface MemberCartAdaptor {

	/**
	 * 회원 - 장바구니 목록 페이징 조회
	 */
	@GetMapping("/{memberId}/carts")
	ResponseEntity<List<ResponseCartItemsForMemberDTO>> getCartItemsByMember(@PathVariable String memberId);

	/**
	 * 회원 - 장바구니 항목 추가
	 */
	@PostMapping("/carts/items")
	ResponseEntity<Void> createCartItemForMember(@RequestBody RequestAddCartItemsDTO requestDto);

	/**
	 * 회원 - 장바구니 항목 수량 변경
	 */
	@PutMapping("/carts/items/{cartItemsId}")
	ResponseEntity<Void> updateCartItemForMember(@PathVariable long cartItemsId,
		@RequestBody RequestUpdateCartItemsDTO requestDto);

	/**
	 * 회원 - 장바구니 항목 삭제
	 */
	@DeleteMapping("/carts/items/{cartItemId}")
	ResponseEntity<Void> deleteCartItemForMember(@PathVariable long cartItemId);

	/**
	 * 회원 - 장바구니 전체 삭제
	 */
	@DeleteMapping("/{memberId}/carts")
	ResponseEntity<Void> deleteCartForMember(@PathVariable String memberId);

}
