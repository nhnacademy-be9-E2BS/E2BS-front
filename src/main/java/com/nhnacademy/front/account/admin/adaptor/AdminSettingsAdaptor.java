package com.nhnacademy.front.account.admin.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.common.page.PageResponse;

@FeignClient(name = "admin-settings-adaptor", url = "${admin.settings.url}")
public interface AdminSettingsAdaptor {

	@GetMapping
	ResponseEntity<ResponseAdminSettingsDTO> getAdminSettings();

	@GetMapping("/members")
	ResponseEntity<PageResponse<ResponseAdminSettingsMembersDTO>> getAdminSettingsMembers();

	@PostMapping("/members/{memberId}")
	ResponseEntity<Void> updateAdminSettingsMemberState(@PathVariable("memberId") String memberId,
		@RequestBody RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO);

	@PutMapping("/members/{memberId}")
	ResponseEntity<Void> updateAdminSettingsMemberRole(@PathVariable("memberId") String memberId);

	@DeleteMapping("/members/{memberId}")
	ResponseEntity<Void> deleteAdminSettingsMember(@PathVariable("memberId") String memberId);

}
