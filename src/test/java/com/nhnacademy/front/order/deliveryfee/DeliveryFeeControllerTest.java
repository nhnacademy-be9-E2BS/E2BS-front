package com.nhnacademy.front.order.deliveryfee;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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
import org.springframework.test.web.servlet.ResultActions;

import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.deliveryfee.controller.DeliveryFeeController;
import com.nhnacademy.front.order.deliveryfee.model.dto.request.RequestDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;

@WithMockUser(username = "admin", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = DeliveryFeeController.class)
@ActiveProfiles("dev")
class DeliveryFeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DeliveryFeeSevice deliveryFeeSevice;

	@MockitoBean
	private CartInterceptor cartInterceptor;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@BeforeEach
	void setUp() throws Exception {
		when(cartInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("GET /admin/settings/deliveryFee - 정상 조회")
	void testGetDeliveryFees() throws Exception {
		// given
		ResponseDeliveryFeeDTO dto = new ResponseDeliveryFeeDTO(1L, 2500, 50000, LocalDateTime.now());
		PageResponse<ResponseDeliveryFeeDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(dto));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);

		when(deliveryFeeSevice.getDeliveryFees(any(Pageable.class))).thenReturn(ResponseEntity.ok(pageResponse));

		// when
		ResultActions result = mockMvc.perform(get("/admin/settings/deliveryFee"));

		// then
		result.andExpect(status().isOk())
			.andExpect(view().name("admin/order/delivery-fee-management"))
			.andExpect(model().attributeExists("deliveryFees"));
	}

	@Test
	@DisplayName("POST /admin/settings/deliveryFee - 정상 생성 후 리다이렉트")
	void testCreateDeliveryFees_success() throws Exception {
		// given
		when(deliveryFeeSevice.createDeliveryFee(any(RequestDeliveryFeeDTO.class))).thenReturn(
			ResponseEntity.ok().build());

		// when
		ResultActions result = mockMvc.perform(post("/admin/settings/deliveryFee")
			.param("deliveryFeeAmount", "2500")
			.param("deliveryFeeFreeAmount", "50000")
			.with(csrf()));

		// then
		result.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/deliveryFee"));
	}

	@Test
	@DisplayName("POST /admin/settings/deliveryFee - 검증 실패 시 예외 발생")
	void testCreateDeliveryFees_validationFail() throws Exception {
		// when
		ResultActions result = mockMvc.perform(post("/admin/settings/deliveryFee")
			.param("deliveryFeeAmount", "-100") // 잘못된 값
			.param("deliveryFeeFreeAmount", "50000")
			.with(csrf()));

		// then
		result.andExpect(status().isBadRequest());
	}
}
