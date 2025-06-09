package com.nhnacademy.front.account.address.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

	MockedStatic<JwtGetMemberId> jwtStatic;

	@BeforeEach
	void setUp() throws Exception {
		jwtStatic = mockStatic(JwtGetMemberId.class);
		jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");
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

}