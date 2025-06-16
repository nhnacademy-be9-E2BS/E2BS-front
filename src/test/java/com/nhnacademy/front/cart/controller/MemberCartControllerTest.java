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
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;
import com.nhnacademy.front.cart.service.MemberCartService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;

import jakarta.servlet.http.HttpServletRequest;

@WithMockUser(username = "member", roles = "MEMBER")
@ActiveProfiles("dev")
@WebMvcTest(MemberCartController.class)
class MemberCartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private MemberCartService memberCartService;

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

	private static final String MEMBER_ID = "id123";

	@Test
	@DisplayName("POST /members/carts/items - 회원 장바구니 항목 추가 테스트")
	void memberAddToCart() throws Exception {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO(MEMBER_ID, null, 1L, 3);
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		// 정적(static) 메서드 호출은 기본 Mockito 모킹할 수 없어 MockedStatic 사용
		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(MEMBER_ID);

			// when & then
			mockMvc.perform(post("/members/carts/items")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest)
					.with(csrf()))
				.andExpect(status().isCreated());
		}

		verify(memberCartService).createCartItemForMember(requestDto);
	}

	@Test
	@DisplayName("POST /members/carts/items - 회원 장바구니 항목 추가 테스트 실패(유효성 검사 실패)")
	void memberAddToCart_Fail_ValidationException() throws Exception {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(MEMBER_ID);

			// when & then
			mockMvc.perform(post("/members/carts/items")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest)
					.with(csrf()))
				.andExpect(status().is3xxRedirection());
		}
	}

	@Test
	@DisplayName("GET /members/carts - 회원 장바구니 목록 조회 테스트")
	void getCartsByMember() throws Exception {
		// given
		List<ResponseCartItemsForMemberDTO> cartItemsByMember = List.of(
			new ResponseCartItemsForMemberDTO(1L, 1L, "상품A", 15000, 10000, new BigDecimal(50), "thumbnailImage", 2,
				20000),
			new ResponseCartItemsForMemberDTO(1L, 2L, "상품B", 15000, 10000, new BigDecimal(50), "thumbnailImage", 3,
				30000)
		);

		ResponseDeliveryFeeDTO deliveryFeeDTO = new ResponseDeliveryFeeDTO(1L, 3000, 50000, LocalDateTime.now());

		when(memberCartService.getCartItemsByMember(MEMBER_ID)).thenReturn(cartItemsByMember);
		when(deliveryFeeSevice.getCurrentDeliveryFee()).thenReturn(deliveryFeeDTO);

		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(MEMBER_ID);

			// when & then
			mockMvc.perform(get("/members/carts")
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attribute("cartItemsByMember", cartItemsByMember))
				.andExpect(model().attribute("totalProductPrice", 50000L))
				.andExpect(view().name("cart/member-cart"));
		}

		verify(memberCartService).getCartItemsByMember(MEMBER_ID);
	}

	@Test
	@DisplayName("PUT /members/carts/items/{cartItemsId} - 회원 장바구니 항목 수량 변경 테스트")
	void updateCartItemForMember() throws Exception {
		// given
		long cartItemId = 10L;
		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO(MEMBER_ID, "", 1L, 5);
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(MEMBER_ID);

			// when & then
			when(memberCartService.updateCartItemForMember(eq(cartItemId), any(RequestUpdateCartItemsDTO.class)))
				.thenReturn(5);

			mockMvc.perform(put("/members/carts/items/{cartItemsId}", cartItemId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest)
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().string("5"));
		}

		verify(memberCartService).updateCartItemForMember(eq(cartItemId), any(RequestUpdateCartItemsDTO.class));
	}

	@Test
	@DisplayName("PUT /members/carts/items/{cartItemsId} - 회원 장바구니 항목 수량 변경 테스트 실패(유효성 검사)")
	void updateCartItemForMember_ValidationFail() throws Exception {
		// given
		long cartItemId = 10L;
		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO();
		String jsonRequest = objectMapper.writeValueAsString(requestDto);

		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(MEMBER_ID);

			// when & then
			mockMvc.perform(put("/members/carts/items/{cartItemsId}", cartItemId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest)
					.with(csrf()))
				.andExpect(status().is3xxRedirection());
		}
	}

	@Test
	@DisplayName("DELETE /members/carts/items/{cartItemsId} - 회원 장바구니 항목 삭제 테스트")
	void deleteCartItemForMember() throws Exception {
		// given
		long cartItemId = 11L;

		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(MEMBER_ID);

			// when & then
			mockMvc.perform(delete("/members/carts/items/{cartItemsId}", cartItemId)
					.sessionAttr("cartItemsCounts", 3)
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().string("2"));
		}

		verify(memberCartService).deleteCartItemForMember(cartItemId);
	}

	@Test
	@DisplayName("DELETE /members/carts - 회원 장바구니 전체 삭제 테스트")
	void deleteCartForMember() throws Exception {
		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(MEMBER_ID);

			// when & then
			mockMvc.perform(delete("/members/carts")
					.with(csrf()))
				.andExpect(status().isNoContent());
		}

		verify(memberCartService).deleteCartForMember(MEMBER_ID);
	}

}
