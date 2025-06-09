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
import com.nhnacademy.front.order.order.adaptor.OrderAdminAdaptor;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;
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
		when(orderAdminAdaptor.getOrders(any(), any(), any(), any(), any(), any())).thenReturn(response);

		ResponseEntity<PageResponse<ResponseOrderDTO>> actual = orderAdminService.getOrders(pageable, "", "", "", "", "");
		assertEquals(response, actual);
		verify(orderAdminAdaptor, times(1)).getOrders(any(), any(), any(), any(), any(), any());

	}

	@Test
	@DisplayName("getOrders - 관리자 주문 내역 특정 상태 페이지 조회")
	void testGetOrdersWithStateId() {
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseOrderDTO>> response = ResponseEntity.ok(
			new PageResponse<ResponseOrderDTO>());
		when(orderAdminAdaptor.getOrders(any(), any(), any(), any(), any(), any())).thenReturn(response);

		ResponseEntity<PageResponse<ResponseOrderDTO>> actual = orderAdminService.getOrders(pageable, "WAIT", "","","","");
		assertEquals(response, actual);
		verify(orderAdminAdaptor, times(1)).getOrders(any(), any(), any(), any(), any(), any());
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

	@Test
	@DisplayName("getReturnOrders - 반품 전체 내역 조회")
	void testGetReturnOrders() {
		// given
		PageResponse<ResponseOrderReturnDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderReturnDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> expectedResponse = ResponseEntity.ok(pageResponse);
		when(orderAdminAdaptor.getReturnOrders(any())).thenReturn(expectedResponse);

		// when
		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> actualResponse = orderAdminService.getReturnOrders(Pageable.unpaged());

		// then
		assertEquals(expectedResponse, actualResponse);
		verify(orderAdminAdaptor).getReturnOrders(any());

	}
}
