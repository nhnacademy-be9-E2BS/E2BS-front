package com.nhnacademy.front.order.order.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.adaptor.OrderAdaptor;
import com.nhnacademy.front.order.order.adaptor.OrderMemberAdaptor;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderMemberAdaptor orderMemberAdaptor;
	private final OrderAdaptor orderAdaptor;

	/**
	 * 주문서 정보를 back에 저장 요청하는 서비스
	 */
	public ResponseEntity<ResponseOrderResultDTO> createOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO) throws FeignException {
		return orderAdaptor.postCreateOrder(requestOrderWrapperDTO);
	}

	/**
	 * 포인트 주문 요청
	 */
	public ResponseEntity<ResponseOrderResultDTO> createPointOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO) throws FeignException {
		return orderMemberAdaptor.postPointCreateOrder(requestOrderWrapperDTO);
	}

	/**
	 * 결제 완료 후 결제 승인 요청을 back에 요청하는 서비스
	 */
	public ResponseEntity<Void> confirmOrder(@RequestParam String orderId, @RequestParam String paymentKey,
		@RequestParam long amount) throws FeignException {
		return orderAdaptor.confirmOrder(orderId, paymentKey, amount);
	}

	/**
	 * 토스 결제 모달 창을 끌 시 저장된 주문서를 삭제하는 서비스
	 */
	public ResponseEntity<Void> deleteOrder(@RequestParam String orderId) throws FeignException {
		return orderAdaptor.deleteOrder(orderId);
	}

	/**
	 * 해당 회원의 주문 목록을 가져오는 메서드
	 */
	public ResponseEntity<PageResponse<ResponseOrderDTO>> getOrdersByMemberId(Pageable pageable, String memberId, String stateName)
		throws FeignException {
		return orderMemberAdaptor.getOrdersByMemberId(pageable, memberId, stateName);
	}

	public ResponseEntity<PageResponse<ResponseOrderDTO>> getOrdersByCustomerId(Pageable pageable, long customerId)
		throws FeignException {
		return orderAdaptor.getOrdersByCustomerId(pageable, customerId);
	}

	/**
	 * 주문 상세 조회 시 사용하는 메서드
	 * 관리자 + 일반 회원 둘 다 사용 메서드
	 */
	public ResponseEntity<ResponseOrderWrapperDTO> getOrderByOrderCode(String orderCode) throws FeignException {
		return orderAdaptor.getOrderByOrderCode(orderCode);
	}

	/**
	 * 회원이 대기 상태인 특정 주문을 취소하는 메서드
	 */
	public ResponseEntity<Void> cancelOrder(String orderCode) throws FeignException {
		return orderMemberAdaptor.cancelOrder(orderCode);
	}

	public ResponseEntity<Void> returnOrder(RequestOrderReturnDTO returnDTO) throws FeignException {
		return orderMemberAdaptor.returnOrder(returnDTO);
	}

	public ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrdersByMemberId(Pageable pageable, String memberId){
		return orderMemberAdaptor.getReturnOrdersByMemberId(pageable, memberId);
	}

	public ResponseEntity<ResponseOrderReturnDTO> getReturnOrderByOrderCode(String orderCode){
		return orderMemberAdaptor.getReturnOrder(orderCode);
	}
}
