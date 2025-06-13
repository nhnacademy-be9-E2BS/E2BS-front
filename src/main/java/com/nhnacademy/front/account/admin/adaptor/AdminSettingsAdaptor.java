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
import com.nhnacademy.front.common.page.PageResponse;

@FeignClient(name = "gateway-service", contextId = "admin-settings-adaptor")
public interface AdminSettingsAdaptor {

	@GetMapping("/api/auth/admin/settings")
	ResponseEntity<ResponseAdminSettingsDTO> getAdminSettings();

	@GetMapping("/api/auth/admin/settings/members")
	ResponseEntity<PageResponse<ResponseAdminSettingsMembersDTO>> getAdminSettingsMembers(
		@SpringQueryMap Pageable pageable);

	@PostMapping("/api/auth/admin/settings/members/{member-id}")
	ResponseEntity<Void> updateAdminSettingsMemberState(@PathVariable("member-id") String memberId,
		@RequestBody RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO);

	@PutMapping("/api/auth/admin/settings/members/{member-id}")
	ResponseEntity<Void> updateAdminSettingsMemberRole(@PathVariable("member-id") String memberId);

	@DeleteMapping("/api/auth/admin/settings/members/{member-id}")
	ResponseEntity<Void> deleteAdminSettingsMember(@PathVariable("member-id") String memberId);

	@GetMapping("/api/auth/admin/settings/daily")
	ResponseEntity<ResponseAdminSettingsDailySummaryDTO> getAdminSettingsDailySummaries();

	@GetMapping("/api/auth/admin/settings/monthly")
	ResponseEntity<ResponseAdminSettingsMonthlySummaryDTO> getAdminSettingsMonthlySummaries();

}
