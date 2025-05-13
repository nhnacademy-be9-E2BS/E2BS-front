package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-confirm-service", url = "${order.confirm.url}")
public interface OrderConfirmAdaptor {
	@PostMapping
	ResponseEntity<Void> confirmOrder(@RequestParam String orderId, @RequestParam String paymentKey,
		@RequestParam long amount);

}
