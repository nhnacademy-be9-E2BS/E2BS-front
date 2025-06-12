package com.nhnacademy.front.account.admin.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDailySummaryDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMonthlySummaryDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsNonMembersDTO;
import com.nhnacademy.front.common.page.PageResponse;

@FeignClient(name = "admin-settings-adaptor", url = "${auth.admin.settings.url}")
public interface AdminSettingsAdaptor {

	@GetMapping
	ResponseEntity<ResponseAdminSettingsDTO> getAdminSettings();

	@GetMapping("/members")
	ResponseEntity<PageResponse<ResponseAdminSettingsMembersDTO>> getAdminSettingsMembers(
		@SpringQueryMap Pageable pageable);

	@PostMapping("/members/{member-id}")
	ResponseEntity<Void> updateAdminSettingsMemberState(@PathVariable("member-id") String memberId,
		@RequestBody RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO);

	@PutMapping("/members/{member-id}")
	ResponseEntity<Void> updateAdminSettingsMemberRole(@PathVariable("member-id") String memberId);

	@DeleteMapping("/members/{member-id}")
	ResponseEntity<Void> deleteAdminSettingsMember(@PathVariable("member-id") String memberId);

	@GetMapping("/customers")
	ResponseEntity<PageResponse<ResponseAdminSettingsNonMembersDTO>> getAdminSettingsNonMembers(
		@SpringQueryMap Pageable pageable);

	@GetMapping("/daily")
	ResponseEntity<ResponseAdminSettingsDailySummaryDTO> getAdminSettingsDailySummaries();

	@GetMapping("/monthly")
	ResponseEntity<ResponseAdminSettingsMonthlySummaryDTO> getAdminSettingsMonthlySummaries();

}
