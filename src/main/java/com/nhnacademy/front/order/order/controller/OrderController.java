package com.nhnacademy.front.order.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {
	/**
	 * 결제 주문서 작성 페이지
	 */
	@GetMapping("/order")
	public String getCheckOut() {
		// 사용자가 주문하려는 상품 정보,쿠폰 내역, 포인트 정보 등을 보내줘야 함
		// 지금은 임시 값을 넣어 확인
		return "payment/checkout";
	}

	@PostMapping("/order")
	public void postCheckOut() {

	}
}
