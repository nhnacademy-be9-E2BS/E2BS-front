package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;

@FeignClient(name = "order-service", url = "${order.order.url}")
public interface OrderAdaptor {

	@PostMapping("/create/tossPay")
	ResponseEntity<ResponseOrderResultDTO> postCreateOrder(@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@PostMapping("/create/point")
	ResponseEntity<ResponseOrderResultDTO> postPointCreateOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@PostMapping("/cancel")
	ResponseEntity<Void> cancelOrder(@RequestParam String orderId);

	@PostMapping("/confirm")
	ResponseEntity<Void> confirmOrder(@RequestParam String orderId, @RequestParam String paymentKey,
		@RequestParam long amount);

}
