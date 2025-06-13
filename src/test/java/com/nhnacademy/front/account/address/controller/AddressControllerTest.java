package com.nhnacademy.front.account.address.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

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
import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = AddressController.class)
class AddressControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AddressService addressService;

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

	@Test
	@DisplayName("회원 배송지 목록 조회 테스트")
	void getMemberAddressesTest() throws Exception {

		// Given
		List<ResponseMemberAddressDTO> addresses = List.of(
			new ResponseMemberAddressDTO(
				1L,
				"12345",
				"서울특별시 송파구 올림픽로",
				"1000호",
				"아파트 10동",
				"집",
				true,
				LocalDateTime.now(),
				"김도윤",
				"010-1234-5678"
			),
			new ResponseMemberAddressDTO(
				2L,
				"10101",
				"부산광역시 해운대구 해운대로",
				"1000호",
				"아파트 10동",
				"집",
				true,
				LocalDateTime.now(),
				"김도윤",
				"010-1234-5678"
			)
		);

		// When
		when(addressService.getMemberAddresses("user")).thenReturn(addresses);

		// Then
		mockMvc.perform(get("/mypage/addresses"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/mypage/mypage-addresses"));

	}

	@Test
	@DisplayName("회원 주소 수정 화면 테스트")
	void getUpdateMemberAddressTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseMemberAddressDTO responseMemberAddressDTO = new ResponseMemberAddressDTO(
				1L,
				"12345",
				"서울특별시 송파구 올림픽로",
				"1000호",
				"아파트 10동",
				"집",
				true,
				LocalDateTime.now(),
				"김도윤",
				"010-1234-5678"
			);

			// When
			when(addressService.getAddressByAddressId("user", 1L)).thenReturn(responseMemberAddressDTO);

			// Then
			mockMvc.perform(get("/mypage/addresses/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("member/mypage/mypage-addresses-update"));
		}
	}

	@Test
	@DisplayName("회원 주소 변경 테스트")
	void updateMemberAddressTest() throws Exception {

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
		doNothing().when(addressService)
			.updateAddress(requestMemberAddressSaveDTO, "user", 1L);

		// Then
		mockMvc.perform(put("/mypage/addresses/{address-id}", 1L)
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
	@DisplayName("회원 배송지 Validation Test")
	void updateMemberAddressValidationTest() throws Exception {

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
		doNothing().when(addressService)
			.updateAddress(requestMemberAddressSaveDTO, "user", 1L);

		// Then
		mockMvc.perform(put("/mypage/addresses/{address-id}", 1L)
				.with(csrf())
				.param("addressAlias", requestMemberAddressSaveDTO.getAddressAlias())
				.param("addressCode", requestMemberAddressSaveDTO.getAddressCode())
				.param("addressInfo", requestMemberAddressSaveDTO.getAddressInfo())
				.param("addressDetail", requestMemberAddressSaveDTO.getAddressDetail())
				.param("addressExtra", requestMemberAddressSaveDTO.getAddressExtra())
				.param("addressDefault", String.valueOf(requestMemberAddressSaveDTO.isAddressDefault()))
				.param("addressReceiverPhone", ""))
			.andDo(print())
			.andExpect(status().is4xxClientError());

	}

	@Test
	@DisplayName("회원 배송지 삭제 테스트")
	void deleteAddressTest() throws Exception {

		// Given

		// When
		doNothing().when(addressService).deleteAddress(
			"user", 1L
		);

		// Then
		mockMvc.perform(delete("/mypage/addresses/1")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/mypage/addresses"));

	}

	@Test
	@DisplayName("회원 기본 배송지 설정 테스트")
	void setDefaultAddressTest() throws Exception {

		// Given

		// When
		doNothing().when(addressService).setDefaultAddress("user", 1);

		// Then
		mockMvc.perform(post("/mypage/addresses/default?addressId=1")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/mypage/addresses"));
	}

}