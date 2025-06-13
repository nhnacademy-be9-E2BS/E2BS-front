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

@FeignClient(name = "gateway-service", contextId = "order-member-service")
public interface OrderMemberAdaptor {

	@PostMapping("/api/auth/orders/create/point")
	ResponseEntity<ResponseOrderResultDTO> postPointCreateOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@GetMapping("/api/auth/orders")
	ResponseEntity<PageResponse<ResponseOrderDTO>> getOrdersByMemberId(Pageable pageable, @RequestParam String memberId,
		@RequestParam(required = false) String stateName,
		@RequestParam(required = false) String startDate,
		@RequestParam(required = false) String endDate,
		@RequestParam(required = false) String orderCode);

	@DeleteMapping("/api/auth/orders/{orderCode}")
	ResponseEntity<Void> cancelOrder(@PathVariable String orderCode);

	@PostMapping("/api/auth/orders/return")
	ResponseEntity<Void> returnOrder(@RequestBody RequestOrderReturnDTO returnDTO);

	@GetMapping("/api/auth/orders/return")
	ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrdersByMemberId(Pageable pageable, @RequestParam String memberId);

	@GetMapping("/api/auth/orders/return/{orderCode}")
	ResponseEntity<ResponseOrderReturnDTO> getReturnOrder(@PathVariable String orderCode);

}
