package com.nhnacademy.front.account.admin.controller.settings;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.admin.exception.AdminSettingsFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberUpdateFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMembersFailedException;
import com.nhnacademy.front.account.admin.model.domain.MonthlySummary;
import com.nhnacademy.front.account.admin.model.domain.WeeklySummary;
import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDailySummaryDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.account.admin.service.AdminSettingsService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자 메인 페이지", description = "관리자 메인 화면 및 기능 제공")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings")
public class AdminSettingsController {
	private final static String REDIRECT_ADMIN_SETTINGS_MEMBERS_PATH = "redirect:/admin/settings/members";

	private final AdminSettingsService adminSettingsService;

	@Operation(summary = "관리자 메인 페이지", description = "관리자 메인 화면 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "관리자 메인 화면에 필요한 데이터 제공"),
			@ApiResponse(responseCode = "500", description = "관리자 메인 화면에서 필요한 데이터 요청 및 응답 실패",
				content = @Content(schema = @Schema(implementation = AdminSettingsFailedException.class)))
		})
	@JwtTokenCheck
	@GetMapping
	public String getAdminSettings(Model model) {
		ResponseAdminSettingsDTO settings = adminSettingsService.getAdminSettings();
		ResponseAdminSettingsDailySummaryDTO dailySummaries = adminSettingsService.getAdminSettingsDailySummaries();
		WeeklySummary weeklySummary = adminSettingsService.getWeeklySummary(dailySummaries);
		MonthlySummary monthlySummary = adminSettingsService.getAdminSettingsMonthlySummaries().getMonthlySummary();

		model.addAttribute("settings", settings);
		model.addAttribute("updatedTime", LocalDateTime.now());
		model.addAttribute("dailySummaries", dailySummaries.getDailySummaries());
		model.addAttribute("weeklySummary", weeklySummary);
		model.addAttribute("monthlySummary", monthlySummary);

		return "admin/settings/admin-settings";
	}

	@Operation(summary = "관리자 회원 관리 페이지", description = "관리자 회원 관리 페이지 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "관리자 회원 관리 페이지에 필요한 데이터 응답"),
			@ApiResponse(responseCode = "500", description = "관리자 회원 관리 페이지에 필요한 데이터 응답 실패",
				content = @Content(schema = @Schema(implementation = AdminSettingsMembersFailedException.class)))
		})
	@JwtTokenCheck
	@GetMapping("/members")
	public String getAdminSettingsMembers(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseAdminSettingsMembersDTO> response = adminSettingsService.getAdminSettingsMembers(pageable);
		Page<ResponseAdminSettingsMembersDTO> members = PageResponseConverter.toPage(response);

		model.addAttribute("members", members);

		return "admin/settings/members/admin-settings-members";
	}

	@Operation(summary = "관리자 회원 상태 수정", description = "관리자 회원 상태 수정 기능 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "관리자 회원 상태 수정 요청 및 성공 응답"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "500", description = "관리자 회원 상태 수정 실패 응답", content = @Content(schema = @Schema(implementation = AdminSettingsMemberUpdateFailedException.class)))
		})
	@JwtTokenCheck
	@PostMapping("/members/{member-id}")
	public String updateAdminSettingsMemberState(@PathVariable("member-id") String memberId,
		@Validated @Parameter(description = "회원 상태 수정 요청 DTO", required = true, schema = @Schema(implementation = RequestAdminSettingsMemberStateDTO.class))
		@ModelAttribute RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		adminSettingsService.updateAdminSettingsMemberState(memberId, requestAdminSettingsMemberStateDTO);

		return REDIRECT_ADMIN_SETTINGS_MEMBERS_PATH;
	}

	@Operation(summary = "관리자 회원 역할 수정", description = "관리자 회원 역할 수정 기능 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "관리자 회원 역할 수정 요청 및 성공 응답"),
			@ApiResponse(responseCode = "500", description = "관리자 회원 역할 수정 실패 응답", content = @Content(schema = @Schema(implementation = AdminSettingsMemberUpdateFailedException.class)))
		})
	@JwtTokenCheck
	@PutMapping("/members/{member-id}")
	public String updateAdminSettingsMemberRole(@PathVariable("member-id") String memberId) {
		adminSettingsService.updateAdminSettingsMemberRole(memberId);

		return REDIRECT_ADMIN_SETTINGS_MEMBERS_PATH;
	}

	@Operation(summary = "관리자 회원 탈퇴", description = "관리자 회원 탈퇴 기능 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "관리자 회원 탈퇴 요청 및 성공 응답"),
			@ApiResponse(responseCode = "500", description = "관리자 회원 탈퇴 실패 응답", content = @Content(schema = @Schema(implementation = AdminSettingsMemberUpdateFailedException.class)))
		})
	@JwtTokenCheck
	@DeleteMapping("/members/{member-id}")
	public String deleteAdminSettingsMember(@PathVariable("member-id") String memberId) {
		adminSettingsService.deleteAdminSettingsMember(memberId);

		return REDIRECT_ADMIN_SETTINGS_MEMBERS_PATH;
	}

}
