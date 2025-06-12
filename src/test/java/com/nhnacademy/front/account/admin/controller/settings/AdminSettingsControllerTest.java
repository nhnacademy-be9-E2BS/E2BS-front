package com.nhnacademy.front.account.admin.controller.settings;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.admin.model.domain.MonthlySummary;
import com.nhnacademy.front.account.admin.model.domain.WeeklySummary;
import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDailySummaryDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMonthlySummaryDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsNonMembersDTO;
import com.nhnacademy.front.account.admin.service.AdminSettingsService;
import com.nhnacademy.front.account.customer.model.domain.Customer;
import com.nhnacademy.front.account.memberstate.model.domain.MemberState;
import com.nhnacademy.front.account.memberstate.model.domain.MemberStateName;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = AdminSettingsController.class)
class AdminSettingsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AdminSettingsService adminSettingsService;

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
	@DisplayName("관리자 메인 화면 테스트")
	void getAdminSettingsTest() throws Exception {

		// Given
		ResponseAdminSettingsDTO responseAdminSettingsDTO = new ResponseAdminSettingsDTO();
		ResponseAdminSettingsDailySummaryDTO responseAdminSettingsDailySummaryDTO = new ResponseAdminSettingsDailySummaryDTO();
		WeeklySummary weeklySummary = new WeeklySummary();
		MonthlySummary monthlySummary = new MonthlySummary(1, 10000L, 1);
		ResponseAdminSettingsMonthlySummaryDTO monthlySummaryDTO = mock(ResponseAdminSettingsMonthlySummaryDTO.class);

		// When
		when(adminSettingsService.getAdminSettings()).thenReturn(responseAdminSettingsDTO);
		when(adminSettingsService.getAdminSettingsDailySummaries()).thenReturn(responseAdminSettingsDailySummaryDTO);
		when(adminSettingsService.getWeeklySummary(responseAdminSettingsDailySummaryDTO)).thenReturn(weeklySummary);
		when(adminSettingsService.getAdminSettingsMonthlySummaries()).thenReturn(monthlySummaryDTO);
		when(monthlySummaryDTO.getMonthlySummary()).thenReturn(monthlySummary);

		// Then
		mockMvc.perform(get("/admin/settings"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/settings/admin-settings"));

	}

	@Test
	@DisplayName("관리자 회원 목록 조회 테스트")
	void getAdminSettingsMembersMethodTest() throws Exception {

		// Given
		List<ResponseAdminSettingsMembersDTO> content = List.of(
			new ResponseAdminSettingsMembersDTO("user", "user", "user@naver.com", "010-1234-5678",
				new MemberState(1L, MemberStateName.ACTIVE),
				"MEMBER")
		);
		PageResponse<ResponseAdminSettingsMembersDTO> mockResponse = new PageResponse<>();
		mockResponse.setContent(content);
		mockResponse.setNumber(0);
		mockResponse.setSize(10);
		mockResponse.setTotalElements(1L);

		Page<ResponseAdminSettingsMembersDTO> page = new PageImpl<>(content);

		// When
		when(adminSettingsService.getAdminSettingsMembers(any(Pageable.class))).thenReturn(mockResponse);

		try (MockedStatic<PageResponseConverter> mockConverter = mockStatic(PageResponseConverter.class)) {
			mockConverter.when(() -> PageResponseConverter.toPage(mockResponse)).thenReturn(page);

			// Then
			mockMvc.perform(get("/admin/settings/members"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/settings/members/admin-settings-members"));
		}

	}

	@Test
	@DisplayName("관리자 회원 상태 수정 테스트")
	void updateAdminSettingsMemberStateMethodTest() throws Exception {

		// Given
		RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO = new RequestAdminSettingsMemberStateDTO(
			"ACTIVE"
		);

		// When
		doNothing().when(adminSettingsService)
			.updateAdminSettingsMemberState("user", requestAdminSettingsMemberStateDTO);

		// Then
		mockMvc.perform(post("/admin/settings/members/user")
				.with(csrf())
				.param("memberStateName", "ACTIVE"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/members"));

	}

	@Test
	@DisplayName("관리자 회원 상태 수정 ValidationFailedException 테스트")
	void updateAdminSettingsMemberStateMethodValidationFailedExceptionTest() throws Exception {

		// Given
		RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO = new RequestAdminSettingsMemberStateDTO(
			"ACTIVE"
		);

		// When
		doNothing().when(adminSettingsService)
			.updateAdminSettingsMemberState("user", requestAdminSettingsMemberStateDTO);

		// Then
		mockMvc.perform(post("/admin/settings/members/user")
				.with(csrf()))
			.andExpect(status().is4xxClientError());

	}

	@Test
	@DisplayName("관리자 회원 역할 수정 메서드 테스트")
	void updateAdminSettingsMemberRoleMethodTest() throws Exception {

		// Given

		// When
		doNothing().when(adminSettingsService).updateAdminSettingsMemberRole("user");

		// Then
		mockMvc.perform(put("/admin/settings/members/user")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/members"));

	}

	@Test
	@DisplayName("관리자 비회원 목록 페이지 테스트")
	void getAdminSettingsNonMembersTest() throws Exception {

		// Given
		List<ResponseAdminSettingsNonMembersDTO> content = List.of(
			new ResponseAdminSettingsNonMembersDTO(new Customer())
		);
		PageResponse<ResponseAdminSettingsNonMembersDTO> mockResponse = new PageResponse<>();
		mockResponse.setContent(content);
		mockResponse.setNumber(0);
		mockResponse.setSize(10);
		mockResponse.setTotalElements(1L);

		Page<ResponseAdminSettingsNonMembersDTO> mockPage = new PageImpl<>(content);

		// When
		when(adminSettingsService.getAdminSettingsNonMembers(any(Pageable.class))).thenReturn(mockResponse);

		try (MockedStatic<PageResponseConverter> mockStatic = mockStatic(PageResponseConverter.class)) {
			mockStatic.when(() -> PageResponseConverter.toPage(mockResponse)).thenReturn(mockPage);

			// Then
			mockMvc.perform(get("/admin/settings/customers"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/settings/members/admin-settings-non-members"))
				.andExpect(model().attributeExists("nonMembers"));
		}

	}

	@Test
	@DisplayName("관리자 회원 탈퇴 메서드 테스트")
	void deleteAdminSettingsMemberMethodTest() throws Exception {

		// Given

		// When
		doNothing().when(adminSettingsService).deleteAdminSettingsMember("user");

		// Then
		mockMvc.perform(delete("/admin/settings/members/user")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/members"));

	}

}