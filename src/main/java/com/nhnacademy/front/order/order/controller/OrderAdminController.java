package com.nhnacademy.front.order.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderAdminController {

	@GetMapping("/admin/settings/orders")
	public String orderSettings(Model model) {

		return "admin/order/ordersManagement";
	}
}
