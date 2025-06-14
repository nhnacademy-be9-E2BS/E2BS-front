package com.nhnacademy.front.account.customer.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerDTO;
import com.nhnacademy.front.account.customer.service.CustomerService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

@ActiveProfiles("dev")
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CustomerService customerService;

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
	@DisplayName("회원 로그인 페이지 반환 테스트")
	void getCustomerLogin() throws Exception {
		// when & then
		mockMvc.perform(get("/customers/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/login/customer-login"));
	}

	@Test
	@DisplayName("비회원 인증 폼 페이지 반환 테스트")
	void guestAuthForm() throws Exception {
		// when & then
		mockMvc.perform(get("/order/auth"))
			.andExpect(status().isOk())
			.andExpect(view().name("customer/auth"));
	}

	@Test
	@DisplayName("비회원 장바구니 항목 쿠키 저장 테스트 - 실패(유효성 검증)")
	void redirectGuestAuth_Fail_Validation() throws Exception {
		// when & then
		mockMvc.perform(post("/order/auth")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("비회원 가입 테스트")
	void register() throws Exception {
		// given
		RequestCustomerRegisterDTO request = new RequestCustomerRegisterDTO("email@naver.com", "홍길동", "pwd123", "pwd123");
		String jsonRequest = objectMapper.writeValueAsString(request);

		ResponseCustomerDTO customer = new ResponseCustomerDTO("홍길동", 1L);
		when(customerService.customerRegister(any())).thenReturn(customer);

		// when & then
		mockMvc.perform(post("/customers/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.customerName").value("홍길동"))
			.andExpect(jsonPath("$.customerId").value(1L));
	}

	@Test
	@DisplayName("비회원 가입 테스트 - 실패(유효성 검증)")
	void register_Fail_Validation() throws Exception {
		// when & then
		mockMvc.perform(post("/customers/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("비회원 로그인 요청 테스트")
	void loginByCart() throws Exception {
		// given
		RequestCustomerLoginDTO request = new RequestCustomerLoginDTO("email@naver.com", "pwd123");
		String jsonRequest = objectMapper.writeValueAsString(request);

		ResponseCustomerDTO customer = new ResponseCustomerDTO("홍길동", 1L);
		when(customerService.customerLogin(any())).thenReturn(customer);

		// when & then
		mockMvc.perform(post("/customers/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.customerName").value("홍길동"))
			.andExpect(jsonPath("$.customerId").value(1L));
	}

	@Test
	@DisplayName("비회원 로그인 요청 테스트 - 실패(유효성 검증)")
	void loginByCart_Fail_Validation() throws Exception {
		// when & then
		mockMvc.perform(post("/customers/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("비회원 로그인 요청 테스트)")
	void login() throws Exception {
		// given
		RequestCustomerLoginDTO request = new RequestCustomerLoginDTO("email@naver.com", "pwd123");
		String jsonRequest = objectMapper.writeValueAsString(request);

		ResponseCustomerDTO customer = new ResponseCustomerDTO("홍길동", 1L);
		when(customerService.customerLogin(any())).thenReturn(customer);

		// when & then
		mockMvc.perform(post("/customers/order/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));
	}

	@Test
	@DisplayName("비회원 로그인 요청 테스트- 실패(유효성 검증)")
	void login_Fail_Validation() throws Exception {
		// when & then
		mockMvc.perform(post("/customers/order/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isBadRequest());
	}

}