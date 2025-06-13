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
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;

@FeignClient(name = "gateway-service", contextId = "guest-cart-adaptor")
public interface GuestCartAdaptor {

	/**
	 * 게스트 - 장바구니 목록 조회
	 */
	@GetMapping("/api/guests/{sessionId}/carts")
	ResponseEntity<List<ResponseCartItemsForGuestDTO>> getCartItemsByGuest(@PathVariable String sessionId);

	/**
	 * 게스트 - 장바구니 항목 추가
	 */
	@PostMapping("/api/guests/carts/items")
	ResponseEntity<Integer> createCartItemForGuest(@RequestBody RequestAddCartItemsDTO requestDto);

	/**
	 * 게스트 - 장바구니 항목 수량 변경
	 */
	@PutMapping("/api/guests/carts/items")
	ResponseEntity<Integer> updateCartItemForGuest(@RequestBody RequestUpdateCartItemsDTO requestDto);

	/**
	 * 게스트 - 장바구니 항목 삭제
	 */
	@DeleteMapping("/api/guests/carts/items")
	ResponseEntity<Void> deleteCartItemForGuest(@RequestBody RequestDeleteCartItemsForGuestDTO requestDto);

	/**
	 * 게스트 - 장바구니 전체 삭제
	 */
	@DeleteMapping("/api/guests/{sessionId}/carts")
	ResponseEntity<Void> deleteCartForGuest(@PathVariable String sessionId);

}
