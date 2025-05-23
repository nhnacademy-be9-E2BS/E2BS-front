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
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
import com.nhnacademy.front.order.order.service.OrderAdminService;
import com.nhnacademy.front.order.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderAdminController {
	private final OrderAdminService orderAdminService;
	private final OrderService orderService;

	@JwtTokenCheck
	@GetMapping("/admin/settings/orders")
	public String getOrders(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam(required = false) Long status) {
		ResponseEntity<PageResponse<ResponseOrderDTO>> response = null;
		if (status == null) {
			response = orderAdminService.getOrders(pageable);
		} else {
			response = orderAdminService.getOrders(pageable, status);
		}
		PageResponse<ResponseOrderDTO> pageResponse = response.getBody();
		Page<ResponseOrderDTO> orders = PageResponseConverter.toPage(pageResponse);
		model.addAttribute("orders", orders);
		return "admin/order/ordersManagement";
	}

	@JwtTokenCheck
	@PostMapping("/admin/settings/orders/{orderCode}")
	public ResponseEntity<Void> startDelivery(@PathVariable String orderCode) {
		return orderAdminService.startDelivery(orderCode);
	}

	@JwtTokenCheck
	@GetMapping("/admin/settings/orders/{orderCode}")
	public String getOrderDetails(Model model, @PathVariable String orderCode) {
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
}
