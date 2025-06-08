package com.nhnacademy.front.cart.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.cart.model.dto.request.RequestMergeCartItemDTO;

@FeignClient(name = "cart-adaptor", url = "${cart.url}")
public interface CartAdaptor {

	/**
	 * 장바구니 항목 개수 조회
	 */
	@GetMapping("/counts")
	ResponseEntity<Integer> getCartItemsCounts(@RequestParam String memberId);

	/**
	 * 게스트 장바구니 -> 회원 장바구니 병합
	 */
	@PostMapping("/merge")
	ResponseEntity<Integer> mergeCartItemsToMemberFromGuest(@RequestBody RequestMergeCartItemDTO requestDto);

}