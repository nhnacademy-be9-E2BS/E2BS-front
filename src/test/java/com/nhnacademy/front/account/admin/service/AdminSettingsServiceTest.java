package com.nhnacademy.front.account.admin.service;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.account.admin.adaptor.AdminSettingsAdaptor;
import com.nhnacademy.front.account.admin.exception.AdminSettingsFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberDeleteFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberUpdateFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMembersFailedException;
import com.nhnacademy.front.account.admin.model.domain.DailySummary;
import com.nhnacademy.front.account.admin.model.domain.MonthlySummary;
import com.nhnacademy.front.account.admin.model.domain.WeeklySummary;
import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDailySummaryDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMonthlySummaryDTO;
import com.nhnacademy.front.account.memberstate.model.domain.MemberState;
import com.nhnacademy.front.common.page.PageResponse;

@ExtendWith(MockitoExtension.class)
class AdminSettingsServiceTest {

	@InjectMocks
	private AdminSettingsService adminSettingsService;

	@Mock
	private AdminSettingsAdaptor adminSettingsAdaptor;

	@Test
	@DisplayName("관리자 페이지 조회 메서드 테스트")
	void getAdminSettingsMethodTest() throws Exception {

		// Given
		ResponseAdminSettingsDTO responseAdminSettingsDTO = new ResponseAdminSettingsDTO(
			25, 300, 450L, 1500000L, 800000L, 100000L
		);
		ResponseEntity<ResponseAdminSettingsDTO> response = new ResponseEntity<>(
			responseAdminSettingsDTO, HttpStatus.CREATED
		);

		// When
		when(adminSettingsAdaptor.getAdminSettings()).thenReturn(response);

		ResponseAdminSettingsDTO result = adminSettingsService.getAdminSettings();

		// Then
		Assertions.assertThat(result).isEqualTo(response.getBody());

	}

	@Test
	@DisplayName("관리자 페이지 조회 메서드 AdminSettingsFailedException 테스트")
	void getAdminSettingsAdminSettingsFailedExceptionTest() throws Exception {

		// Given
		ResponseAdminSettingsDTO responseAdminSettingsDTO = new ResponseAdminSettingsDTO(
			25, 300, 450L, 1500000L, 800000L, 100000L
		);
		ResponseEntity<ResponseAdminSettingsDTO> response = new ResponseEntity<>(
			responseAdminSettingsDTO, HttpStatus.BAD_REQUEST
		);

		// When
		when(adminSettingsAdaptor.getAdminSettings()).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(AdminSettingsFailedException.class, () -> {
			adminSettingsService.getAdminSettings();
		});

	}

	@Test
	@DisplayName("관리자 페이지 일일 요약 데이터 조회 메서드 테스트")
	void getAdminSettingsDailySummariesMethodTest() throws Exception {

		// Given
		ResponseAdminSettingsDailySummaryDTO responseAdminSettingsDailySummaryDTO = new ResponseAdminSettingsDailySummaryDTO(
			List.of()
		);
		ResponseEntity<ResponseAdminSettingsDailySummaryDTO> response = new ResponseEntity<>(
			responseAdminSettingsDailySummaryDTO, HttpStatus.CREATED);

		// When
		when(adminSettingsAdaptor.getAdminSettingsDailySummaries()).thenReturn(response);

		ResponseAdminSettingsDailySummaryDTO result = adminSettingsService.getAdminSettingsDailySummaries();

		// Then
		Assertions.assertThat(result).isEqualTo(response.getBody());

	}

	@Test
	@DisplayName("관리자 페이지 일일 요약 데이터 조회 AdminSettingsFailedException 테스트")
	void getAdminSettingsDailySummariesMethodAdminSettingsFailedExceptionTest() throws Exception {

		// Given
		ResponseAdminSettingsDailySummaryDTO responseAdminSettingsDailySummaryDTO = new ResponseAdminSettingsDailySummaryDTO(
			List.of()
		);
		ResponseEntity<ResponseAdminSettingsDailySummaryDTO> response = new ResponseEntity<>(
			responseAdminSettingsDailySummaryDTO, HttpStatus.BAD_REQUEST);

		// When
		when(adminSettingsAdaptor.getAdminSettingsDailySummaries()).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(AdminSettingsFailedException.class, () -> {
			adminSettingsService.getAdminSettingsDailySummaries();
		});

	}

	@Test
	@DisplayName("관리자 페이지 월 별 요약 데이터 조회 메서드 테스트")
	void getAdminSettingsMonthlySummariesMethodTest() throws Exception {

		// Given
		ResponseAdminSettingsMonthlySummaryDTO responseAdminSettingsMonthlySummaryDTO = new ResponseAdminSettingsMonthlySummaryDTO(
			new MonthlySummary()
		);
		ResponseEntity<ResponseAdminSettingsMonthlySummaryDTO> response = new ResponseEntity<>(
			responseAdminSettingsMonthlySummaryDTO, HttpStatus.CREATED
		);

		// When
		when(adminSettingsAdaptor.getAdminSettingsMonthlySummaries()).thenReturn(response);

		ResponseAdminSettingsMonthlySummaryDTO result = adminSettingsService.getAdminSettingsMonthlySummaries();

		// Then
		Assertions.assertThat(result).isEqualTo(response.getBody());

	}

	@Test
	@DisplayName("관리자 페이지 월 별 요약 데이터 조회 AdminSettingsFailedException 메서드 테스트")
	void getAdminSettingsMonthlySummariesMethodAdminSettingsFailedExceptionTest() throws Exception {

		// Given
		ResponseAdminSettingsMonthlySummaryDTO responseAdminSettingsMonthlySummaryDTO = new ResponseAdminSettingsMonthlySummaryDTO(
			new MonthlySummary()
		);
		ResponseEntity<ResponseAdminSettingsMonthlySummaryDTO> response = new ResponseEntity<>(
			responseAdminSettingsMonthlySummaryDTO, HttpStatus.BAD_REQUEST
		);

		// When
		when(adminSettingsAdaptor.getAdminSettingsMonthlySummaries()).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(AdminSettingsFailedException.class, () -> {
			adminSettingsService.getAdminSettingsMonthlySummaries();
		});

	}

	@Test
	@DisplayName("관리자 페이지 회원 목록 조회 메서드 테스트")
	void getAdminSettingsMembersMethodTest() throws Exception {

		// Given
		List<ResponseAdminSettingsMembersDTO> content = List.of(
			new ResponseAdminSettingsMembersDTO("user", "user", "user@naver.com", "MEMBER", new MemberState(), "MEMBER")
		);
		PageResponse<ResponseAdminSettingsMembersDTO> mockResponse = new PageResponse<>();
		mockResponse.setContent(content);
		mockResponse.setNumber(0);
		mockResponse.setSize(10);
		mockResponse.setTotalElements(1L);

		ResponseEntity<PageResponse<ResponseAdminSettingsMembersDTO>> responseEntity =
			ResponseEntity.ok(mockResponse);

		// When
		Pageable pageable = PageRequest.of(0, 10);

		when(adminSettingsAdaptor.getAdminSettingsMembers(pageable)).thenReturn(responseEntity);

		PageResponse<ResponseAdminSettingsMembersDTO> result = adminSettingsService.getAdminSettingsMembers(pageable);

		// Then
		Assertions.assertThat(result).isEqualTo(mockResponse);

	}

	@Test
	@DisplayName("회원 목록 조회 AdminSettingsMembersFailedException 메서드 테스트")
	void getAdminSettingsMembersAdminSettingsMembersFailedExceptionException() throws Exception {

		// Given
		ResponseEntity<PageResponse<ResponseAdminSettingsMembersDTO>> response =
			ResponseEntity.ok(null);

		// When
		Pageable pageable = PageRequest.of(0, 10);

		when(adminSettingsAdaptor.getAdminSettingsMembers(pageable)).thenReturn(response);

		// Then
		Assertions.assertThatThrownBy(() -> adminSettingsService.getAdminSettingsMembers(pageable))
			.isInstanceOf(AdminSettingsMembersFailedException.class);

	}

	@Test
	@DisplayName("관리자 회원 상태 변경 메서드 테스트")
	void updateAdminSettingsMemberStateMethodTest() throws Exception {

		// Given
		RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO = new RequestAdminSettingsMemberStateDTO(
			"ACTIVE"
		);
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(
			adminSettingsAdaptor.updateAdminSettingsMemberState("user", requestAdminSettingsMemberStateDTO)).thenReturn(
			response);

		// Then
		Assertions.assertThatCode(() -> {
			adminSettingsService.updateAdminSettingsMemberState("user", requestAdminSettingsMemberStateDTO);
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("관리자 회원 상태 변경 메서드 AdminSettingsMemberUpdateFailedException 테스트")
	void updateAdminSettingsMemberStateMethodAdminSettingsMemberUpdateFailedExceptionTest() throws Exception {

		// Given
		RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO = new RequestAdminSettingsMemberStateDTO(
			"ACTIVE"
		);
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(
			adminSettingsAdaptor.updateAdminSettingsMemberState("user", requestAdminSettingsMemberStateDTO)).thenReturn(
			response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(AdminSettingsMemberUpdateFailedException.class, () -> {
			adminSettingsService.updateAdminSettingsMemberState("user", requestAdminSettingsMemberStateDTO);
		});

	}

	@Test
	@DisplayName("관리자 회원 역할 변경 메서드 테스트")
	void updateAdminSettingsMemberRoleMethodTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(adminSettingsAdaptor.updateAdminSettingsMemberRole("user")).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> {
			adminSettingsService.updateAdminSettingsMemberRole("user");
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("관리자 회원 역할 변경 메서드 AdminSettingsMemberUpdateFailedException 테스트")
	void updateAdminSettingsMemberRoleAdminSettingsMemberUpdateFailedExceptionTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(adminSettingsAdaptor.updateAdminSettingsMemberRole("user")).thenReturn(response);

		// Then{
		org.junit.jupiter.api.Assertions.assertThrows(AdminSettingsMemberUpdateFailedException.class, () -> {
			adminSettingsService.updateAdminSettingsMemberRole("user");
		});

	}

	@Test
	@DisplayName("관리자 회원 탈퇴 메서드 테스트")
	void deleteAdminSettingsMemberMethodTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(adminSettingsAdaptor.deleteAdminSettingsMember("user")).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> {
			adminSettingsService.deleteAdminSettingsMember("user");
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("관리자 회원 탈퇴 메서드 AdminSettingsMemberDeleteFailedException 테스트")
	void deleteAdminSettingsMemberMethodAdminSettingsMemberDeleteFailedExceptionTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(adminSettingsAdaptor.deleteAdminSettingsMember("user")).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(AdminSettingsMemberDeleteFailedException.class, () -> {
			adminSettingsService.deleteAdminSettingsMember("user");
		});

	}

	@Test
	@DisplayName("관리자 페이지 주간 요약 데이터 조회 메서드 테스트")
	void getWeeklySummaryMethodTest() throws Exception {

		// Given
		List<DailySummary> dailySummaries = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			LocalDate date = LocalDate.now().minusDays(6 - i);
			int orderCount = i + 1;
			Long sales = (long)(i * 1000);
			int signupCount = i + 2;

			dailySummaries.add(new DailySummary(date, orderCount, sales, signupCount));
		}

		ResponseAdminSettingsDailySummaryDTO dto = new ResponseAdminSettingsDailySummaryDTO();
		dto.setDailySummaries(dailySummaries);

		// When
		WeeklySummary summary = adminSettingsService.getWeeklySummary(dto);

		// Then
		Assertions.assertThat(summary.getOrderCount()).isEqualTo(28);
		Assertions.assertThat(summary.getSales()).isEqualTo(21000L);
		Assertions.assertThat(summary.getSignupCount()).isEqualTo(35);
	}

}