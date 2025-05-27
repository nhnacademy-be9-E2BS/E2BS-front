package com.nhnacademy.front.account.admin.controller.settings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsNonMembersDTO;
import com.nhnacademy.front.account.admin.service.AdminSettingsService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings")
public class AdminSettingsController {

	private final AdminSettingsService adminSettingsService;

	@JwtTokenCheck
	@GetMapping
	public String getAdminSettings(Model model) {
		ResponseAdminSettingsDTO settings = adminSettingsService.getAdminSettings();

		model.addAttribute("settings", settings);

		return "admin/settings/admin-settings";
	}

	@JwtTokenCheck
	@GetMapping("/members")
	public String getAdminSettingsMembers(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseAdminSettingsMembersDTO> response = adminSettingsService.getAdminSettingsMembers(pageable);
		Page<ResponseAdminSettingsMembersDTO> members = PageResponseConverter.toPage(response);

		model.addAttribute("members", members);

		return "admin/settings/members/admin-settings-members";
	}

	@JwtTokenCheck
	@PostMapping("/members/{memberId}")
	public String updateAdminSettingsMemberState(@PathVariable("memberId") String memberId,
		@ModelAttribute RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		adminSettingsService.updateAdminSettingsMemberState(memberId, requestAdminSettingsMemberStateDTO);

		return "redirect:/admin/settings/members";
	}

	@JwtTokenCheck
	@PutMapping("/members/{memberId}")
	public String updateAdminSettingsMemberRole(@PathVariable("memberId") String memberId) {
		adminSettingsService.updateAdminSettingsMemberRole(memberId);

		return "redirect:/admin/settings/members";
	}

	@JwtTokenCheck
	@DeleteMapping("/members/{memberId}")
	public String deleteAdminSettingsMember(@PathVariable("memberId") String memberId) {
		adminSettingsService.deleteAdminSettingsMember(memberId);

		return "redirect:/admin/settings/members";
	}

	/**
	 * 관리자 페이지 비회원 관리 뷰
	 */
	@JwtTokenCheck
	@GetMapping("/non-members")
	public String getAdminSettingsNonMembers(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseAdminSettingsNonMembersDTO> response = adminSettingsService.getAdminSettingsNonMembers(
			pageable);
		Page<ResponseAdminSettingsNonMembersDTO> nonMembers = PageResponseConverter.toPage(response);

		model.addAttribute("nonMembers", nonMembers);

		return "admin/settings/members/admin-settings-non-members";
	}

}
