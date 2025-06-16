package com.nhnacademy.front.account.address.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = AddressSaveController.class)
class AddressSaveControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AddressService addressService;

	MockedStatic<JwtGetMemberId> jwtStatic;

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
	@DisplayName("회원 배송지 정보 입력 화면 테스트")
	void getSaveMemberAddressTest() throws Exception {

		// Given

		// When

		// Then
		mockMvc.perform(get("/mypage/addresses/form"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/mypage/mypage-addresses-save"));

	}

	@Test
	@DisplayName("회원 배송지 저장 테스트")
	void saveMemberAddressTest() throws Exception {

		// Given
		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO = new RequestMemberAddressSaveDTO(
			"우리집",
			"12345",
			"서울시 강남구",
			"101동 202호",
			"엘리베이터 있음",
			true,
			"홍길동",
			"01012345678"
		);

		// When
		doNothing().when(addressService).saveMemberAddress("user", requestMemberAddressSaveDTO);

		// Then
		mockMvc.perform(post("/mypage/addresses/form")
				.with(csrf())
				.param("addressAlias", requestMemberAddressSaveDTO.getAddressAlias())
				.param("addressCode", requestMemberAddressSaveDTO.getAddressCode())
				.param("addressInfo", requestMemberAddressSaveDTO.getAddressInfo())
				.param("addressDetail", requestMemberAddressSaveDTO.getAddressDetail())
				.param("addressExtra", requestMemberAddressSaveDTO.getAddressExtra())
				.param("addressDefault", String.valueOf(requestMemberAddressSaveDTO.isAddressDefault()))
				.param("addressReceiver", requestMemberAddressSaveDTO.getAddressReceiver())
				.param("addressReceiverPhone", requestMemberAddressSaveDTO.getAddressReceiverPhone()))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/mypage/addresses"));

	}

	@Test
	@DisplayName("회원 배송지 저장 Validation 테스트")
	void saveMemberAddressValidationTest() throws Exception {

		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO = new RequestMemberAddressSaveDTO(
			"우리집",
			"12345",
			"서울시 강남구",
			"101동 202호",
			"엘리베이터 있음",
			true,
			"홍길동",
			"01012345678"
		);

		// When
		doNothing().when(addressService).saveMemberAddress("user", requestMemberAddressSaveDTO);

		// Then
		mockMvc.perform(post("/mypage/addresses/form")
				.with(csrf())
				.param("addressAlias", requestMemberAddressSaveDTO.getAddressAlias())
				.param("addressCode", requestMemberAddressSaveDTO.getAddressCode())
				.param("addressInfo", requestMemberAddressSaveDTO.getAddressInfo())
				.param("addressDetail", requestMemberAddressSaveDTO.getAddressDetail())
				.param("addressExtra", requestMemberAddressSaveDTO.getAddressExtra())
				.param("addressDefault", String.valueOf(requestMemberAddressSaveDTO.isAddressDefault())))
			.andDo(print())
			.andExpect(status().is3xxRedirection());

	}

}