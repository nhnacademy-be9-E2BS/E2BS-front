package com.nhnacademy.front.order.order.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.order.order.adaptor.OrderCancelAdaptor;
import com.nhnacademy.front.order.order.adaptor.OrderConfirmAdaptor;
import com.nhnacademy.front.order.order.adaptor.OrderCreateAdaptor;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderCreateAdaptor orderCreateAdaptor;
	private final OrderConfirmAdaptor orderConfirmAdaptor;
	private final OrderCancelAdaptor orderCancelAdaptor;

	/**
	 * 주문서 정보를 back에 저장 요청하는 서비스
	 */
	public ResponseEntity<ResponseOrderResultDTO> createOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO) {
		return orderCreateAdaptor.postCreateOrder(requestOrderWrapperDTO);
	}

	/**
	 * 결제 완료 후 결제 승인 요청을 back에 요청하는 서비스
	 */
	public ResponseEntity<Void> confirmOrder(@RequestParam String orderId, @RequestParam String paymentKey,
		@RequestParam long amount) {
		return orderConfirmAdaptor.confirmOrder(orderId, paymentKey, amount);
	}

	public ResponseEntity<Void> cancelOrder(@RequestParam String orderId) {
		return orderCancelAdaptor.cancelOrder(orderId);
	}
}
