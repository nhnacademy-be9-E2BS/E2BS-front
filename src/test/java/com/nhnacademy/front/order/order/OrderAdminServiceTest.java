package com.nhnacademy.front.order.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import com.nhnacademy.front.order.order.adaptor.OrderAdminAdaptor;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.service.OrderAdminService;

@ExtendWith(MockitoExtension.class)
class OrderAdminServiceTest {

	@InjectMocks
	private OrderAdminService orderAdminService;

	@Mock
	private OrderAdminAdaptor orderAdminAdaptor;

	@Test
	@DisplayName("getOrders - 관리자 주문 내역 전체 조회")
	void testGetOrders() {
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseOrderDTO>> response = ResponseEntity.ok(
			new PageResponse<ResponseOrderDTO>());
		when(orderAdminAdaptor.getOrders(pageable)).thenReturn(response);

		ResponseEntity<PageResponse<ResponseOrderDTO>> actual = orderAdminService.getOrders(pageable);
		assertEquals(response, actual);
		verify(orderAdminAdaptor, times(1)).getOrders(pageable);

	}

	@Test
	@DisplayName("getOrders - 관리자 주문 내역 특정 상태 페이지 조회")
	void testGetOrdersWithStateId() {
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseOrderDTO>> response = ResponseEntity.ok(
			new PageResponse<ResponseOrderDTO>());
		when(orderAdminAdaptor.getOrders(pageable, 5L)).thenReturn(response);

		ResponseEntity<PageResponse<ResponseOrderDTO>> actual = orderAdminService.getOrders(pageable, 5L);
		assertEquals(response, actual);
		verify(orderAdminAdaptor, times(1)).getOrders(pageable, 5L);
	}

	@Test
	@DisplayName("startDelivery - 주문 배송 시작")
	void testStartDelivery() {
		// given
		String orderCode = "TEST-ORDER-CODE";
		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

		when(orderAdminAdaptor.startDelivery(orderCode)).thenReturn(expectedResponse);

		// when
		ResponseEntity<Void> actualResponse = orderAdminService.startDelivery(orderCode);

		// then
		assertEquals(expectedResponse, actualResponse);
		verify(orderAdminAdaptor).startDelivery(orderCode);

	}
}
