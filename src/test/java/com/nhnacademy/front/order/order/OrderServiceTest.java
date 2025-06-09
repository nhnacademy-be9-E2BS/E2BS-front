package com.nhnacademy.front.order.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.adaptor.OrderAdaptor;
import com.nhnacademy.front.order.order.adaptor.OrderMemberAdaptor;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
import com.nhnacademy.front.order.order.service.OrderService;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderMemberAdaptor orderMemberAdaptor;

	@Mock
	private OrderAdaptor orderAdaptor;

	@Test
	@DisplayName("createOrder - 주문 생성")
	void testCreateOrder() {
		// given
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();
		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();
		ResponseEntity<ResponseOrderResultDTO> expectedResponse = ResponseEntity.ok(responseDTO);

		when(orderAdaptor.postCreateOrder(request)).thenReturn(expectedResponse);

		// when
		ResponseEntity<ResponseOrderResultDTO> actualResponse = orderService.createOrder(request);

		// then
		assertEquals(expectedResponse, actualResponse);
		verify(orderAdaptor).postCreateOrder(request);
	}

	@Test
	@DisplayName("createPointOrder - 포인트 주문")
	void testCreatePointOrder() {
		// given
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();
		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();
		ResponseEntity<ResponseOrderResultDTO> expectedResponse = ResponseEntity.ok(responseDTO);

		when(orderMemberAdaptor.postPointCreateOrder(request)).thenReturn(expectedResponse);

		// when
		ResponseEntity<ResponseOrderResultDTO> actualResponse = orderService.createPointOrder(request);

		// then
		assertEquals(expectedResponse, actualResponse);
		verify(orderMemberAdaptor).postPointCreateOrder(request);
	}

	@Test
	@DisplayName("confirmOrder - 결제 승인")
	void testConfirmOrder() {
		// given
		String orderId = "TEST-ORDER-CODE";
		String paymentKey = "TEST-PAYMENT-KEY";
		long amount = 9999L;
		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

		when(orderAdaptor.confirmOrder(orderId, paymentKey, amount)).thenReturn(expectedResponse);

		// when
		ResponseEntity<Void> actualResponse = orderService.confirmOrder(orderId, paymentKey, amount);

		// then
		assertEquals(expectedResponse, actualResponse);
		verify(orderAdaptor).confirmOrder(orderId, paymentKey, amount);
	}

	@Test
	@DisplayName("deleteOrder - 주문서 삭제")
	void testDeleteOrder() {
		// given
		String orderId = "TEST-ORDER-CODE";
		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

		when(orderAdaptor.deleteOrder(orderId)).thenReturn(expectedResponse);

		// when
		ResponseEntity<Void> actualResponse = orderService.deleteOrder(orderId);

		// then
		assertEquals(expectedResponse, actualResponse);
		verify(orderAdaptor).deleteOrder(orderId);
	}

	@Test
	@DisplayName("getOrderByOrderCode - 주문 상세 조회")
	void testGetOrderByOrderCode() {
		// given
		String orderCode = "TEST-ORDER-CODE";
		ResponseEntity<ResponseOrderWrapperDTO> expectedResponse = ResponseEntity.ok(new ResponseOrderWrapperDTO());

		when(orderAdaptor.getOrderByOrderCode(orderCode)).thenReturn(expectedResponse);

		// when
		ResponseEntity<ResponseOrderWrapperDTO> actualResponse = orderService.getOrderByOrderCode(orderCode);

		// then
		assertEquals(expectedResponse, actualResponse);
		verify(orderAdaptor).getOrderByOrderCode(orderCode);

	}

	@Test
	@DisplayName("getOrdersByMemberId - 회원 주문 내역 조회")
	void testGetOrdersByMemberId() {
		String memberId = "TEST-MEMBER-ID";
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseOrderDTO>> response = ResponseEntity.ok(
			new PageResponse<ResponseOrderDTO>());
		when(orderMemberAdaptor.getOrdersByMemberId(pageable, memberId, null, null, null, null)).thenReturn(response);

		ResponseEntity<PageResponse<ResponseOrderDTO>> actual = orderService.getOrdersByMemberId(pageable, memberId, null, null, null, null);
		assertEquals(response, actual);
		verify(orderMemberAdaptor, times(1)).getOrdersByMemberId(pageable, memberId, null, null, null, null);
	}

	@Test
	@DisplayName("getOrdersByCustomerId - 비회원 주문 내역 조회")
	void testGetOrdersByCustomerId() {
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseOrderDTO>> response = ResponseEntity.ok(
			new PageResponse<ResponseOrderDTO>());
		when(orderAdaptor.getOrdersByCustomerId(pageable, 1L)).thenReturn(response);

		ResponseEntity<PageResponse<ResponseOrderDTO>> actual = orderService.getOrdersByCustomerId(pageable, 1L);
		assertEquals(response, actual);
		verify(orderAdaptor, times(1)).getOrdersByCustomerId(pageable, 1L);
	}

	@Test
	@DisplayName(("returnOrder - 반품 요청"))
	void testReturnOrder() {
		String orderId = "TEST-ORDER-CODE";
		RequestOrderReturnDTO request = new RequestOrderReturnDTO(orderId, "BREAK", "BREAK");
		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
		when(orderMemberAdaptor.returnOrder(request)).thenReturn(expectedResponse);
		ResponseEntity<Void> actualResponse = orderService.returnOrder(request);
		assertEquals(expectedResponse, actualResponse);
		verify(orderMemberAdaptor).returnOrder(request);
	}


	@Test
	@DisplayName(("getReturnOrdersByMemberId - 특정 회원의 반품 목록 조회 요청"))
	void testGetReturnOrdersByMemberId() {
		PageResponse<ResponseOrderReturnDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderReturnDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> expectedResponse = ResponseEntity.ok(pageResponse);
		when(orderMemberAdaptor.getReturnOrdersByMemberId(any(), anyString())).thenReturn(expectedResponse);
		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> actualResponse = orderService.getReturnOrdersByMemberId(Pageable.unpaged(), "Member");
		assertEquals(expectedResponse, actualResponse);
		verify(orderMemberAdaptor).getReturnOrdersByMemberId(any(), anyString());
	}

	@Test
	@DisplayName(("getReturnOrderByOrderCode - 특정 반품 상세 내역 조회 요청"))
	void testGetReturnOrderByOrderCode() {
		ResponseEntity<ResponseOrderReturnDTO> expectedResponse = ResponseEntity.ok(new ResponseOrderReturnDTO());
		when(orderMemberAdaptor.getReturnOrder(anyString())).thenReturn(expectedResponse);
		ResponseEntity<ResponseOrderReturnDTO> actualResponse = orderService.getReturnOrderByOrderCode("TEST-ORDER-CODE");
		assertEquals(expectedResponse, actualResponse);
		verify(orderMemberAdaptor).getReturnOrder(anyString());
	}

	@Test
	@DisplayName(("cancelOrder - 회원 주문 취소"))
	void testCancelOrder() {
		String orderId = "TEST-ORDER-CODE";
		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
		when(orderMemberAdaptor.cancelOrder(orderId)).thenReturn(expectedResponse);
		ResponseEntity<Void> actualResponse = orderService.cancelOrder(orderId);
		assertEquals(expectedResponse, actualResponse);
		verify(orderMemberAdaptor).cancelOrder(orderId);
	}
}
