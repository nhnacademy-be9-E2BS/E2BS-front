package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;

@FeignClient(name = "gateway-service", contextId = "order-admin-service")
public interface OrderAdminAdaptor {
	@GetMapping("/api/auth/admin/orders")
	ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable,
		@RequestParam(required = false) String stateName,
		@RequestParam(required = false) String startDate,
		@RequestParam(required = false) String endDate,
		@RequestParam(required = false) String orderCode,
		@RequestParam(required = false) String memberId);

	@PostMapping("/api/auth/admin/orders/{orderCode}")
	ResponseEntity<Void> startDelivery(@PathVariable String orderCode);

	@GetMapping("/api/auth/admin/orders/return")
	ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrders(Pageable pageable);
}
