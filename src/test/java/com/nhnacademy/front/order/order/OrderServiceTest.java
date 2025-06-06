// package com.nhnacademy.front.order.order;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.ResponseEntity;
//
// import com.nhnacademy.front.common.page.PageResponse;
// import com.nhnacademy.front.order.order.adaptor.OrderAdaptor;
// import com.nhnacademy.front.order.order.adaptor.OrderMemberAdaptor;
// import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
// import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
// import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
// import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
// import com.nhnacademy.front.order.order.service.OrderService;
//
// @ExtendWith(MockitoExtension.class)
// class OrderServiceTest {
//
// 	@InjectMocks
// 	private OrderService orderService;
//
// 	@Mock
// 	private OrderMemberAdaptor orderMemberAdaptor;
//
// 	@Mock
// 	private OrderAdaptor orderAdaptor;
//
// 	@Test
// 	@DisplayName("createOrder - 주문 생성")
// 	void testCreateOrder() {
// 		// given
// 		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();
// 		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();
// 		ResponseEntity<ResponseOrderResultDTO> expectedResponse = ResponseEntity.ok(responseDTO);
//
// 		when(orderAdaptor.postCreateOrder(request)).thenReturn(expectedResponse);
//
// 		// when
// 		ResponseEntity<ResponseOrderResultDTO> actualResponse = orderService.createOrder(request);
//
// 		// then
// 		assertEquals(expectedResponse, actualResponse);
// 		verify(orderAdaptor).postCreateOrder(request);
// 	}
//
// 	@Test
// 	@DisplayName("createPointOrder - 포인트 주문")
// 	void testCreatePointOrder() {
// 		// given
// 		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();
// 		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();
// 		ResponseEntity<ResponseOrderResultDTO> expectedResponse = ResponseEntity.ok(responseDTO);
//
// 		when(orderMemberAdaptor.postPointCreateOrder(request)).thenReturn(expectedResponse);
//
// 		// when
// 		ResponseEntity<ResponseOrderResultDTO> actualResponse = orderService.createPointOrder(request);
//
// 		// then
// 		assertEquals(expectedResponse, actualResponse);
// 		verify(orderMemberAdaptor).postPointCreateOrder(request);
// 	}
//
// 	@Test
// 	@DisplayName("confirmOrder - 결제 승인")
// 	void testConfirmOrder() {
// 		// given
// 		String orderId = "TEST-ORDER-CODE";
// 		String paymentKey = "TEST-PAYMENT-KEY";
// 		long amount = 9999L;
// 		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
//
// 		when(orderAdaptor.confirmOrder(orderId, paymentKey, amount)).thenReturn(expectedResponse);
//
// 		// when
// 		ResponseEntity<Void> actualResponse = orderService.confirmOrder(orderId, paymentKey, amount);
//
// 		// then
// 		assertEquals(expectedResponse, actualResponse);
// 		verify(orderAdaptor).confirmOrder(orderId, paymentKey, amount);
// 	}
//
// 	@Test
// 	@DisplayName("deleteOrder - 주문서 삭제")
// 	void testDeleteOrder() {
// 		// given
// 		String orderId = "TEST-ORDER-CODE";
// 		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
//
// 		when(orderAdaptor.deleteOrder(orderId)).thenReturn(expectedResponse);
//
// 		// when
// 		ResponseEntity<Void> actualResponse = orderService.deleteOrder(orderId);
//
// 		// then
// 		assertEquals(expectedResponse, actualResponse);
// 		verify(orderAdaptor).deleteOrder(orderId);
// 	}
//
// 	@Test
// 	@DisplayName("getOrderByOrderCode - 주문 상세 조회")
// 	void testGetOrderByOrderCode() {
// 		// given
// 		String orderCode = "TEST-ORDER-CODE";
// 		ResponseEntity<ResponseOrderWrapperDTO> expectedResponse = ResponseEntity.ok(new ResponseOrderWrapperDTO());
//
// 		when(orderAdaptor.getOrderByOrderCode(orderCode)).thenReturn(expectedResponse);
//
// 		// when
// 		ResponseEntity<ResponseOrderWrapperDTO> actualResponse = orderService.getOrderByOrderCode(orderCode);
//
// 		// then
// 		assertEquals(expectedResponse, actualResponse);
// 		verify(orderAdaptor).getOrderByOrderCode(orderCode);
//
// 	}
//
// 	@Test
// 	@DisplayName("getOrdersByMemberId - 회원 주문 내역 조회")
// 	void testGetOrdersByMemberId() {
// 		String memberId = "TEST-MEMBER-ID";
// 		Pageable pageable = PageRequest.of(0, 10);
// 		ResponseEntity<PageResponse<ResponseOrderDTO>> response = ResponseEntity.ok(
// 			new PageResponse<ResponseOrderDTO>());
// 		when(orderMemberAdaptor.getOrdersByMemberId(pageable, memberId)).thenReturn(response);
//
// 		ResponseEntity<PageResponse<ResponseOrderDTO>> actual = orderService.getOrdersByMemberId(pageable, memberId);
// 		assertEquals(response, actual);
// 		verify(orderMemberAdaptor, times(1)).getOrdersByMemberId(pageable, memberId);
// 	}
//
// 	@Test
// 	@DisplayName(("cancelOrder - 회원 주문 취소"))
// 	void testCancelOrder() {
// 		String orderId = "TEST-ORDER-CODE";
// 		ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
// 		when(orderMemberAdaptor.cancelOrder(orderId)).thenReturn(expectedResponse);
// 		ResponseEntity<Void> actualResponse = orderService.cancelOrder(orderId);
// 		assertEquals(expectedResponse, actualResponse);
// 		verify(orderMemberAdaptor).cancelOrder(orderId);
// 	}
// }
