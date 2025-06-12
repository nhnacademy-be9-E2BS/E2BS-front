package com.nhnacademy.front.cart.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "member-cart-adaptor", url = "${member.url}")
public interface MemberRegisterCartAdaptor {
	/**
	 * 회원 - 장바구니 생성
	 */
	@PostMapping("/{memberId}/carts")
	ResponseEntity<Void> createCartByMember(@PathVariable String memberId);
}
