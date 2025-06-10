package com.nhnacademy.front.order.order;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.controller.OrderAdminController;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDetailDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
import com.nhnacademy.front.order.order.service.OrderAdminService;
import com.nhnacademy.front.order.order.service.OrderService;

@WithMockUser(username = "admin", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = OrderAdminController.class)
@ActiveProfiles("dev")
class OrderAdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderAdminService orderAdminService;

	@MockitoBean
	private OrderService orderService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("getOrders - 관리자 주문 내역 조회(필터링 없음)")
	void testGetOrders_WithoutStatus() throws Exception {
		// given
		PageResponse<ResponseOrderDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderDTO>> responseEntity = ResponseEntity.ok(pageResponse);
		when(orderAdminService.getOrders(any(), any(), any(), any(), any(), any())).thenReturn(responseEntity);

		// when & then
		mockMvc.perform(get("/admin/settings/orders"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/order/orders-management"))
			.andExpect(model().attributeExists("orders"));
	}

	@Test
	@DisplayName("getOrders - 관리자 주문 내역 조회(필터링 있음)")
	void testGetOrders_WithStatus() throws Exception {
		// given
		PageResponse<ResponseOrderDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderDTO>> responseEntity = ResponseEntity.ok(pageResponse);
		when(orderAdminService.getOrders(any(), any(), any(), any(), any(), any())).thenReturn(responseEntity);

		// when & then
		mockMvc.perform(get("/admin/settings/orders")
				.param("status", "WAIT")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/order/orders-management"))
			.andExpect(model().attributeExists("orders"));
	}

	@Test
	@DisplayName("startDelivery - 배송 시작")
	void testStartDelivery() throws Exception {
		// given
		String orderCode = "TEST-ORDER-CODE";
		when(orderAdminService.startDelivery(orderCode)).thenReturn(ResponseEntity.ok().build());

		// when & then
		mockMvc.perform(post("/admin/settings/orders/" + orderCode)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("getOrderDetail - 주문 상세 내역 페이지")
	void testGetOrderDetails() throws Exception {
		// given
		String orderCode = "TEST-ORDER-CODE";

		ResponseOrderDTO order = new ResponseOrderDTO();
		order.setState("WAIT");
		ResponseOrderDetailDTO detail1 = new ResponseOrderDetailDTO();
		detail1.setOrderDetailPerPrice(1000);
		detail1.setOrderQuantity(2);
		detail1.setWrapperPrice(500L);

		ResponseOrderDetailDTO detail2 = new ResponseOrderDetailDTO();
		detail2.setOrderDetailPerPrice(2000);
		detail2.setOrderQuantity(1);

		List<ResponseOrderDetailDTO> orderDetails = List.of(detail1, detail2);

		ResponseOrderWrapperDTO wrapperDTO = new ResponseOrderWrapperDTO();
		wrapperDTO.setOrder(order);
		wrapperDTO.setOrderDetails(orderDetails);

		when(orderService.getOrderByOrderCode(orderCode)).thenReturn(ResponseEntity.ok(wrapperDTO));

		// when & then
		mockMvc.perform(get("/admin/settings/orders/" + orderCode))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/order/order-details-management"))
			.andExpect(model().attributeExists("order"))
			.andExpect(model().attributeExists("orderDetails"))
			.andExpect(model().attributeExists("productAmount"));
	}

	@Test
	@DisplayName("getReturnOrders-관리자 반품 내역 조회")
	void testGetReturnOrders() throws Exception {
		PageResponse<ResponseOrderReturnDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderReturnDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> responseEntity = ResponseEntity.ok(pageResponse);
		when(orderAdminService.getReturnOrders(any(Pageable.class))).thenReturn(responseEntity);

		mockMvc.perform(get("/admin/settings/return")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk());
	}


	@Test
	@DisplayName("getReturnOrderDetails-반품 상세 정보 조회")
	void testGetReturnOrderDetails() throws Exception {
		String orderCode = "TEST-ORDER-CODE";
		ResponseEntity<ResponseOrderReturnDTO> responseEntity = ResponseEntity.ok(new ResponseOrderReturnDTO());
		when(orderService.getReturnOrderByOrderCode(orderCode)).thenReturn(responseEntity);

		mockMvc.perform(get("/admin/settings/return/" + orderCode))
			.andExpect(status().isOk());
	}
}
