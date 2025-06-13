package com.nhnacademy.front.cart.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;

import jakarta.servlet.http.Cookie;

@WithMockUser(username = "admin", roles = "ADMIN")
@ActiveProfiles("dev")
@WebMvcTest(GuestCartController.class)
class GuestCartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GuestCartService guestCartService;

	@MockitoBean
	private DeliveryFeeSevice deliveryFeeSevice;

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
	@DisplayName("POST /guests/carts/items - 게스트 장바구니 항목 추가 테스트")
	void guestAddToCart() throws Exception {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		requestDto.setProductId(1L);
		requestDto.setQuantity(2);
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		String guestKey = "session-id-123";
		Cookie cookie = new Cookie("guestKey", guestKey);

		when(guestCartService.createCartItemForGuest(any(RequestAddCartItemsDTO.class))).thenReturn(1);

		// when & then
		mockMvc.perform(post("/guests/carts/items")
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.with(csrf()))
			.andExpect(status().isCreated());

		verify(guestCartService).createCartItemForGuest(any(RequestAddCartItemsDTO.class));
	}

	@Test
	@DisplayName("POST /guests/carts/items - 게스트 장바구니 항목 추가 테스트 실패(유효성 검사)")
	void guestAddToCart_Fail_Validation() throws Exception {
		// given
		RequestAddCartItemsDTO invalidDto = new RequestAddCartItemsDTO();
		String json = objectMapper.writeValueAsString(invalidDto);

		// when & then
		mockMvc.perform(post("/guests/carts/items")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("PUT /guests/carts/items - 장바구니 항목 수량 변경 테스트")
	void updateCartItemForGuest() throws Exception {
		// given
		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO();
		requestDto.setProductId(1L);
		requestDto.setQuantity(3);
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		String guestKey = "session-id-123";
		Cookie cookie = new Cookie("guestKey", guestKey);

		when(guestCartService.updateCartItemForGuest(any())).thenReturn(2);

		// when & then
		mockMvc.perform(put("/guests/carts/items")
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.with(csrf()))
			.andExpect(status().isOk());

		verify(guestCartService).updateCartItemForGuest(any());
	}

	@Test
	@DisplayName("PUT /guests/carts/items - 장바구니 항목 수량 변경 테스트 실패(유효성 검사 실패)")
	void updateCartItemForGuest_Fail_Validation() throws Exception {
		// given
		RequestUpdateCartItemsDTO invalidDto = new RequestUpdateCartItemsDTO();
		String json = objectMapper.writeValueAsString(invalidDto);

		// when & then
		mockMvc.perform(put("/guests/carts/items")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("GET /guests/carts - 게스트 장바구니 목록 조회 테스트")
	void getCartsByGuest() throws Exception {
		// given
		String guestKey = "session-id-123";
		Cookie cookie = new Cookie("guestKey", guestKey);

		List<ResponseCartItemsForGuestDTO> cartItems = List.of(new ResponseCartItemsForGuestDTO(1L, "title", 10000, 5000, new BigDecimal(0), "image", 3, 15000));

		ResponseDeliveryFeeDTO deliveryFeeDTO = new ResponseDeliveryFeeDTO(1L, 3000, 50000, LocalDateTime.now());

		when(guestCartService.getCartItemsByGuest(guestKey)).thenReturn(cartItems);
		when(deliveryFeeSevice.getCurrentDeliveryFee()).thenReturn(deliveryFeeDTO);

		// when & then
		mockMvc.perform(get("/guests/carts")
				.cookie(cookie)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("cart/guest-cart"))
			.andExpect(model().attributeExists("cartItemsByGuest"))
			.andExpect(model().attributeExists("totalProductPrice"))
			.andExpect(model().attributeExists("currentDeliveryPrice"))
			.andExpect(model().attributeExists("currentDeliveryFee"));

		verify(deliveryFeeSevice).getCurrentDeliveryFee();
	}

	@Test
	@DisplayName("DELETE /guests/carts/items - 장바구니 항목 삭제 테스트")
	void deleteCartItemForGuest() throws Exception {
		// given
		RequestDeleteCartItemsForGuestDTO requestDto = new RequestDeleteCartItemsForGuestDTO();
		requestDto.setProductId(1L);
		String json = objectMapper.writeValueAsString(requestDto);

		String guestKey = "session-id-123";
		Cookie cookie = new Cookie("guestKey", guestKey);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cartItemsCounts", 3);

		doNothing().when(guestCartService).deleteCartItemForGuest(any(RequestDeleteCartItemsForGuestDTO.class));

		// when & then
		mockMvc.perform(delete("/guests/carts/items")
				.cookie(cookie)
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(content().string("2"));

		verify(guestCartService).deleteCartItemForGuest(any(RequestDeleteCartItemsForGuestDTO.class));
	}
	
	@Test
	@DisplayName("DELETE /guests/carts/items - 장바구니 항목 삭제 테스트 실패(유효성 검사)")
	void deleteCartItemForGuest_Fail_Validation() throws Exception {
		// given
		RequestDeleteCartItemsForGuestDTO invalidDto = new RequestDeleteCartItemsForGuestDTO();
		String json = objectMapper.writeValueAsString(invalidDto);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cartItemsCounts", 3);

		doNothing().when(guestCartService).deleteCartItemForGuest(any(RequestDeleteCartItemsForGuestDTO.class));

		// when & then
		mockMvc.perform(delete("/guests/carts/items")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("DELETE /guests/carts - 게스트 장바구니 전체 삭제 테스트")
	void deleteCartForGuest() throws Exception {
		// given
		String guestKey = "session-id-123";
		Cookie cookie = new Cookie("guestKey", guestKey);

		doNothing().when(guestCartService).deleteCartForGuest(anyString());

		// when & then
		mockMvc.perform(delete("/guests/carts")
				.cookie(cookie)
				.with(csrf()))
			.andExpect(status().isNoContent());

		verify(guestCartService).deleteCartForGuest(anyString());
	}

}
