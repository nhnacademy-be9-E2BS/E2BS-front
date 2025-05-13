package com.nhnacademy.front.order.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.order.order.model.dto.request.RequestOrderDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderDetailDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderController {

	private final OrderService orderService;

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
	public ResponseEntity<ResponseOrderResultDTO> postCheckOut(@RequestBody RequestOrderWrapperDTO request) {
		RequestOrderDTO orderDTO = request.getOrder();
		List<RequestOrderDetailDTO> orderDetailDTOs = request.getOrderDetails();

		return orderService.createOrder(new RequestOrderWrapperDTO(orderDTO, orderDetailDTOs));
	}
}
