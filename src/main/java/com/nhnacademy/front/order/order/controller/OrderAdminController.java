package com.nhnacademy.front.order.order.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDetailDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
import com.nhnacademy.front.order.order.service.OrderAdminService;
import com.nhnacademy.front.order.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자 주문 기능", description = "관리자의 주문 관련 기능 제공")
@RequiredArgsConstructor
@Controller
public class OrderAdminController {
	private final OrderAdminService orderAdminService;
	private final OrderService orderService;

	@Operation(summary = "관리자 전체 주문 내역 페이지", description = "관리자가 현재 모든 주문에 대한 정보를 확인 가능한 페이지 제공")
	@JwtTokenCheck
	@GetMapping("/admin/settings/orders")
	public String getOrders(Model model,
		@PageableDefault(page = 0, size = 10) Pageable pageable,
		@Parameter(description = "주문 상태") @RequestParam(required = false) String status,
		@Parameter(description = "주문 일자 검색 시작 일", example = "2025-01-01") @RequestParam(required = false) String startDate,
		@Parameter(description = "주문 일자 검색 끝 일", example = "2025-12-01") @RequestParam(required = false) String endDate,
		@Parameter(description = "주문 코드") @RequestParam(required = false) String orderCode,
		@Parameter(description = "주문 회원 ID") @RequestParam(required = false) String memberId) {

		ResponseEntity<PageResponse<ResponseOrderDTO>> response = orderAdminService.getOrders(pageable, status, startDate, endDate, orderCode, memberId);

		PageResponse<ResponseOrderDTO> pageResponse = response.getBody();
		Page<ResponseOrderDTO> orders = PageResponseConverter.toPage(pageResponse);
		model.addAttribute("orders", orders);
		return "admin/order/ordersManagement";
	}

	@Operation(summary = "관리자 배송 시작 처리", description = "관리자가 특정 주문에 대하여 배송 시작하는 요청을 처리")
	@JwtTokenCheck
	@PostMapping("/admin/settings/orders/{orderCode}")
	public ResponseEntity<Void> startDelivery(@Parameter(description = "주문 코드") @PathVariable String orderCode) {
		return orderAdminService.startDelivery(orderCode);
	}

	@Operation(summary = "관리자 주문 내역 확인 페이지", description = "관리자가 특정 주문에 대하여 상세 정보 확인이 가능한 페이지 제공")
	@JwtTokenCheck
	@GetMapping("/admin/settings/orders/{orderCode}")
	public String getOrderDetails(Model model, @Parameter(description = "주문 코드") @PathVariable String orderCode) {
		ResponseEntity<ResponseOrderWrapperDTO> response = orderService.getOrderByOrderCode(orderCode);
		ResponseOrderWrapperDTO responseOrder = response.getBody();
		ResponseOrderDTO order = responseOrder.getOrder();
		List<ResponseOrderDetailDTO> orderDetails = responseOrder.getOrderDetails();

		long productAmount = 0;
		for (ResponseOrderDetailDTO orderDetail : orderDetails) {
			productAmount += orderDetail.getOrderDetailPerPrice() * orderDetail.getOrderQuantity();
			// 포장지가 있다면 포장지 가격도 포함
			if (orderDetail.getWrapperPrice() != null) {
				productAmount += orderDetail.getWrapperPrice() * orderDetail.getOrderQuantity();
			}
		}

		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("productAmount", productAmount);

		return "admin/order/orderDetailsManagement";
	}


	@Operation(summary = "관리자 반품 내역 확인 페이지", description = "관리자가 전체 반품 주문에 대해 확인 가능한 페이지 제공")
	@GetMapping("/admin/settings/return")
	public String getReturnOrders(Model model, Pageable pageable) {

		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> response =
			orderAdminService.getReturnOrders(pageable);
		Page<ResponseOrderReturnDTO> returns = PageResponseConverter.toPage(response.getBody());
		model.addAttribute("returns", returns);

		return "admin/order/orderReturns";
	}


	@Operation(summary = "관리자 반품 상세 내역 확인 페이지", description = "관리자가 특정 반품에 대하여 상세 정보 확인이 가능한 페이지 제공")
	@GetMapping("/admin/settings/return/{orderCode}")
	public String getReturnOrderDetails(Model model, @Parameter(description = "주문 코드") @PathVariable String orderCode) {

		ResponseOrderReturnDTO returnDTO = orderService.getReturnOrderByOrderCode(orderCode).getBody();
		model.addAttribute("returnDTO", returnDTO);
		return "admin/order/orderReturnDetail";
	}
}
