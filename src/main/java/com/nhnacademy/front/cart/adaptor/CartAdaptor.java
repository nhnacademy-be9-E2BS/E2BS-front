package com.nhnacademy.front.cart.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cart-adaptor", url = "${cart.url}")
public interface CartAdaptor {

	@GetMapping("/counts")
	ResponseEntity<Integer> getCartItemsCounts(@RequestParam("memberId") String memberId, @RequestParam("sessionId") String sessionId);

}