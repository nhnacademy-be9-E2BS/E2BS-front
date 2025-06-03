package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;

@FeignClient(name = "order-service", url = "${order.order.auth}")
public interface OrderAdaptor {

	@PostMapping("/create/tossPay")
	ResponseEntity<ResponseOrderResultDTO> postCreateOrder(@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@PostMapping("/create/point")
	ResponseEntity<ResponseOrderResultDTO> postPointCreateOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@PostMapping("/cancel")
	ResponseEntity<Void> deleteOrder(@RequestParam String orderId);

	@PostMapping("/confirm")
	ResponseEntity<Void> confirmOrder(@RequestParam String orderId, @RequestParam String paymentKey,
		@RequestParam long amount);

	@GetMapping("/{orderCode}")
	ResponseEntity<ResponseOrderWrapperDTO> getOrderByOrderCode(@PathVariable String orderCode);

	@GetMapping
	ResponseEntity<PageResponse<ResponseOrderDTO>> getOrdersByMemberId(Pageable pageable, @RequestParam String memberId);

	@DeleteMapping("/{orderCode}")
	ResponseEntity<Void> cancelOrder(@PathVariable String orderCode);

	@PostMapping("/return")
	ResponseEntity<Void> returnOrder(@RequestBody RequestOrderReturnDTO returnDTO);

	@GetMapping("/return")
	ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrdersByMemberId(Pageable pageable, @RequestParam String memberId);

	@GetMapping("/return/{orderCode}")
	ResponseEntity<ResponseOrderReturnDTO> getReturnOrder(@PathVariable String orderCode);
}
