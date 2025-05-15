package com.nhnacademy.front.order.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.order.order.controller.OrderController;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderDetailDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.service.OrderService;

@WithMockUser(username = "admin", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = OrderController.class)
@ActiveProfiles("dev")
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	private RequestOrderDTO getTestOrderDTO() {
		RequestOrderDTO orderDTO = new RequestOrderDTO();
		orderDTO.setDeliveryFeeId(1L);
		orderDTO.setCustomerId(100L);
		orderDTO.setOrderReceiverName("테스트");
		orderDTO.setOrderReceiverPhone("01012345678");
		orderDTO.setOrderAddressCode("12345");
		orderDTO.setOrderAddressInfo("서울특별시 강남구 도산대로");
		orderDTO.setOrderAddressExtra("도곡빌딩 3층");
		orderDTO.setOrderPointAmount(5000L);
		orderDTO.setOrderPaymentStatus(true);
		orderDTO.setOrderPaymentAmount(25000L);
		return orderDTO;
	}

	private RequestOrderDetailDTO getTestOrderDetailDTO() {
		RequestOrderDetailDTO orderDetailDTO = new RequestOrderDetailDTO();
		orderDetailDTO.setProductId(10L);
		orderDetailDTO.setOrderStateId(1L);
		orderDetailDTO.setOrderCode("TEST-ORDER-CODE");
		orderDetailDTO.setWrapperId(123L);
		orderDetailDTO.setOrderQuantity(3);
		orderDetailDTO.setOrderDetailPerPrice(8900L);
		return orderDetailDTO;
	}

	@Test
	@DisplayName("주문서 페이지 접근 확인")
	void testGetCheckOut() throws Exception {
		mockMvc.perform(get("/order"))
			.andExpect(status().isOk())
			.andExpect(view().name("payment/checkout"));
	}

	@Test
	@DisplayName("토스 결제 주문 시 주문서 저장")
	void testPostCheckOut() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO(getTestOrderDTO(),
			new ArrayList<RequestOrderDetailDTO>(Arrays.asList(getTestOrderDetailDTO())));

		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();

		when(orderService.createOrder(any(RequestOrderWrapperDTO.class)))
			.thenReturn(ResponseEntity.ok(responseDTO));

		mockMvc.perform(post("/order/tossPay")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("토스 결제 주문 시 주문서 저장 - 잘못된 요청")
	void testPostCheckOutFail() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();
		
		mockMvc.perform(post("/order/tossPay")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("포인트 결제 주문 생성 - 성공")
	void testPostPointCheckOut() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO(getTestOrderDTO(),
			new ArrayList<RequestOrderDetailDTO>(Arrays.asList(getTestOrderDetailDTO())));

		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();

		when(orderService.createOrder(any(RequestOrderWrapperDTO.class)))
			.thenReturn(ResponseEntity.ok(responseDTO));

		mockMvc.perform(post("/order/point")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("포인트 결제 주문 생성 - 잘못된 요청")
	void testPostPointCheckOutFail() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();

		mockMvc.perform(post("/order/point")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("결제 완료 후 결제 승인 요청 - 성공 응답 시 리다이렉트")
	void testGetSuccessOrder_success() throws Exception {
		when(orderService.confirmOrder(anyString(), anyString(), anyLong()))
			.thenReturn(ResponseEntity.ok().build());

		mockMvc.perform(get("/order/success")
				.param("orderId", "TEST-ORDER-CODE")
				.param("paymentKey", "TEST-PAYMENT-KEY")
				.param("amount", "10000"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/order/confirm"));
	}

	@Test
	@DisplayName("결제 완료 후 결제 승인 요청 - 실패 응답 시 리다이렉트")
	void testGetSuccessOrder_fail() throws Exception {
		when(orderService.confirmOrder(anyString(), anyString(), anyLong()))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		mockMvc.perform(get("/order/success")
				.param("orderId", "TEST-ORDER-CODE")
				.param("paymentKey", "TEST-PAYMENT-KEY")
				.param("amount", "10000"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/order/fail"));
	}

	@Test
	@DisplayName("결제 완료 페이지 접근")
	void testGetConfirmOrder() throws Exception {
		mockMvc.perform(get("/order/confirm"))
			.andExpect(status().isOk())
			.andExpect(view().name("payment/confirmation"));
	}

	@Test
	@DisplayName("결제 실패 페이지 접근")
	void testGetFailOrder() throws Exception {
		mockMvc.perform(get("/order/fail"))
			.andExpect(status().isOk())
			.andExpect(view().name("payment/fail"));
	}

	@Test
	@DisplayName("토스 결제 모달 끌 시 주문서 삭제 요청")
	void testCancelOrder() throws Exception {
		when(orderService.cancelOrder(anyString()))
			.thenReturn(ResponseEntity.ok().build());

		mockMvc.perform(post("/order/cancel")
				.param("orderId", "TEST-ORDER-CODE")
				.with(csrf()))
			.andExpect(status().isOk());
	}
}
