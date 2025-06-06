package com.nhnacademy.front.order.order.adaptor;

import java.time.LocalDate;

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

@FeignClient(name = "order-admin-service", url = "${order.admin}")
public interface OrderAdminAdaptor {
	@GetMapping
	ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable,
		@RequestParam(required = false) String stateName,
		@RequestParam(required = false) LocalDate startDate,
		@RequestParam(required = false) LocalDate endDate,
		@RequestParam(required = false) String orderCode,
		@RequestParam(required = false) String memberId);

	@PostMapping("/{orderCode}")
	ResponseEntity<Void> startDelivery(@PathVariable String orderCode);

	@GetMapping("/return")
	ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrders(Pageable pageable);
}
