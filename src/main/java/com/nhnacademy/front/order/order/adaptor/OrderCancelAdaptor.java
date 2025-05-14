package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-cancel-service", url = "${order.cancel.url}")
public interface OrderCancelAdaptor {
	@PostMapping
	ResponseEntity<Void> cancelOrder(@RequestParam String orderId);
}
