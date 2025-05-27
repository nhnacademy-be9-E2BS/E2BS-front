package com.nhnacademy.front.account.admin.service;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.admin.adaptor.AdminSettingsAdaptor;
import com.nhnacademy.front.account.admin.exception.AdminSettingsFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberDeleteFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberUpdateFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMembersFailedException;
import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.common.page.PageResponse;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSettingsService {

	private final AdminSettingsAdaptor adminSettingsAdaptor;

	public ResponseAdminSettingsDTO getAdminSettings() throws FeignException {
		ResponseEntity<ResponseAdminSettingsDTO> response = adminSettingsAdaptor.getAdminSettings();
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new AdminSettingsFailedException("관리자 페이지를 위한 데이터들을 가져오지 못했습니다.");
		}

		return response.getBody();
	}

	public PageResponse<ResponseAdminSettingsMembersDTO> getAdminSettingsMembers() throws FeignException {
		ResponseEntity<PageResponse<ResponseAdminSettingsMembersDTO>> response = adminSettingsAdaptor.getAdminSettingsMembers();
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new AdminSettingsMembersFailedException("회원 목록을 가져오지 못했습니다.");
		}

		return response.getBody();
	}

	public void updateAdminSettingsMemberState(String memberId,
		RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO) throws FeignException {
		ResponseEntity<Void> response = adminSettingsAdaptor.updateAdminSettingsMemberState(memberId,
			requestAdminSettingsMemberStateDTO);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new AdminSettingsMemberUpdateFailedException("회원 상태를 변경하지 못했습니다.");
		}
	}

	public void updateAdminSettingsMemberRole(String memberId) throws FeignException {
		ResponseEntity<Void> response = adminSettingsAdaptor.updateAdminSettingsMemberRole(memberId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new AdminSettingsMemberUpdateFailedException("회원 권한을 변경하지 못했습니다.");
		}
	}

	public void deleteAdminSettingsMember(String memberId) throws FeignException {
		ResponseEntity<Void> response = adminSettingsAdaptor.deleteAdminSettingsMember(memberId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new AdminSettingsMemberDeleteFailedException("회원 탈퇴에 실패했습니다.");
		}
	}

}
