package com.nhnacademy.front.cart.controller;

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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;
import com.nhnacademy.front.cart.service.GuestCartService;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

@WithMockUser(username = "admin", roles = "ADMIN")
@ActiveProfiles("dev")
@WebMvcTest(GuestCartController.class)
class GuestCartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GuestCartService guestCartService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Autowired
	private ObjectMapper objectMapper;

	private MockHttpSession session;

	@BeforeEach
	void setup() {
		session = new MockHttpSession();
		session.setAttribute("JSESSIONID", "test-session-id");
	}

	@Test
	@DisplayName("POST /guests/carts/items - 게스트 장바구니 항목 추가 테스트")
	void guestAddToCart() throws Exception {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		requestDto.setProductId(1L);
		requestDto.setQuantity(2);
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		when(guestCartService.createCartItemForGuest(any(RequestAddCartItemsDTO.class))).thenReturn(1);

		// when & then
		mockMvc.perform(post("/guests/carts/items")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.with(csrf()))
			.andExpect(status().isCreated());

		verify(guestCartService).createCartItemForGuest(any(RequestAddCartItemsDTO.class));
	}

	@Test
	@DisplayName("GET /guests/carts - 게스트 장바구니 목록 조회 테스트")
	void getCartsByGuest() throws Exception {
		// given
		ResponseCartItemsForGuestDTO requestDto = new ResponseCartItemsForGuestDTO();
		requestDto.setProductTotalPrice(5000L);

		when(guestCartService.getCartItemsByGuest(anyString())).thenReturn(List.of(requestDto));

		// when & then
		mockMvc.perform(get("/guests/carts").session(session))
			.andExpect(status().isOk())
			.andExpect(view().name("cart/guest-cart"))
			.andExpect(model().attributeExists("cartItemsByGuest"))
			.andExpect(model().attribute("totalPaymentAmount", 5000L));
	}

	@Test
	@DisplayName("PUT /guests/carts/items - 게스트 장바구니 항목 수량 변경 테스트")
	void updateCartItemForGuest() throws Exception {
		// given
		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO();
		requestDto.setProductId(1L);
		requestDto.setQuantity(3);
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		when(guestCartService.updateCartItemForGuest(any(RequestUpdateCartItemsDTO.class))).thenReturn(1);

		// when & then
		mockMvc.perform(put("/guests/carts/items")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.with(csrf()))
			.andExpect(status().isNoContent());

		verify(guestCartService).updateCartItemForGuest(any(RequestUpdateCartItemsDTO.class));
	}

	@Test
	@DisplayName("DELETE /guests/carts/items - 게스트 장바구니 항목 삭제 테스트")
	void deleteCartItemForGuest() throws Exception {
		// given
		RequestDeleteCartItemsForGuestDTO requestDto = new RequestDeleteCartItemsForGuestDTO();
		requestDto.setProductId(1L);
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		doNothing().when(guestCartService).deleteCartItemForGuest(any(RequestDeleteCartItemsForGuestDTO.class));

		// when & then
		mockMvc.perform(delete("/guests/carts/items")
				.session(session)
				.sessionAttr("cartItemsCounts", 0)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.with(csrf()))
			.andExpect(status().isNoContent());

		verify(guestCartService).deleteCartItemForGuest(any(RequestDeleteCartItemsForGuestDTO.class));
	}

	@Test
	@DisplayName("DELETE /guests/carts - 게스트 장바구니 전체 삭제 테스트")
	void deleteCartForGuest() throws Exception {
		// given
		doNothing().when(guestCartService).deleteCartForGuest(session.getId());

		// when & then
		mockMvc.perform(delete("/guests/carts").session(session)
				.with(csrf()))
			.andExpect(status().isNoContent());

		verify(guestCartService).deleteCartForGuest(anyString());
	}

}
