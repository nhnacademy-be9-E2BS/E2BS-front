package com.nhnacademy.front.order.order.adaptor;

import java.time.LocalDate;

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

@FeignClient(name = "order-member-service", url = "${order.order.auth}")
public interface OrderMemberAdaptor {

	@PostMapping("/create/point")
	ResponseEntity<ResponseOrderResultDTO> postPointCreateOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@GetMapping
	ResponseEntity<PageResponse<ResponseOrderDTO>> getOrdersByMemberId(Pageable pageable, @RequestParam String memberId,
		@RequestParam(required = false) String stateName,
		@RequestParam(required = false) LocalDate startDate,
		@RequestParam(required = false) LocalDate endDate,
		@RequestParam(required = false) String orderCode);

	@DeleteMapping("/{orderCode}")
	ResponseEntity<Void> cancelOrder(@PathVariable String orderCode);

	@PostMapping("/return")
	ResponseEntity<Void> returnOrder(@RequestBody RequestOrderReturnDTO returnDTO);

	@GetMapping("/return")
	ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrdersByMemberId(Pageable pageable, @RequestParam String memberId);

	@GetMapping("/return/{orderCode}")
	ResponseEntity<ResponseOrderReturnDTO> getReturnOrder(@PathVariable String orderCode);

}
