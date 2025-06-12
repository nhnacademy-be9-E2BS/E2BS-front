package com.nhnacademy.front.account.admin.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.admin.adaptor.AdminSettingsAdaptor;
import com.nhnacademy.front.account.admin.exception.AdminSettingsFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberDeleteFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberUpdateFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMembersFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsNonMembersFailedException;
import com.nhnacademy.front.account.admin.model.domain.DailySummary;
import com.nhnacademy.front.account.admin.model.domain.WeeklySummary;
import com.nhnacademy.front.account.admin.model.dto.request.RequestAdminSettingsMemberStateDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsDailySummaryDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMembersDTO;
import com.nhnacademy.front.account.admin.model.dto.response.ResponseAdminSettingsMonthlySummaryDTO;
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
			throw new AdminSettingsFailedException();
		}

		return response.getBody();
	}

	public ResponseAdminSettingsDailySummaryDTO getAdminSettingsDailySummaries() throws FeignException {
		ResponseEntity<ResponseAdminSettingsDailySummaryDTO> response = adminSettingsAdaptor.getAdminSettingsDailySummaries();
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new AdminSettingsFailedException();
		}

		return response.getBody();
	}

	public ResponseAdminSettingsMonthlySummaryDTO getAdminSettingsMonthlySummaries() throws FeignException {
		ResponseEntity<ResponseAdminSettingsMonthlySummaryDTO> response = adminSettingsAdaptor.getAdminSettingsMonthlySummaries();
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new AdminSettingsFailedException();
		}

		return response.getBody();
	}

	public PageResponse<ResponseAdminSettingsMembersDTO> getAdminSettingsMembers(Pageable pageable) throws
		FeignException {
		ResponseEntity<PageResponse<ResponseAdminSettingsMembersDTO>> response = adminSettingsAdaptor.getAdminSettingsMembers(
			pageable);
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new AdminSettingsMembersFailedException();
		}

		return response.getBody();
	}

	public void updateAdminSettingsMemberState(String memberId,
		RequestAdminSettingsMemberStateDTO requestAdminSettingsMemberStateDTO) throws FeignException {
		ResponseEntity<Void> response = adminSettingsAdaptor.updateAdminSettingsMemberState(memberId,
			requestAdminSettingsMemberStateDTO);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new AdminSettingsMemberUpdateFailedException();
		}
	}

	public void updateAdminSettingsMemberRole(String memberId) throws FeignException {
		ResponseEntity<Void> response = adminSettingsAdaptor.updateAdminSettingsMemberRole(memberId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new AdminSettingsMemberUpdateFailedException();
		}
	}

	public void deleteAdminSettingsMember(String memberId) throws FeignException {
		ResponseEntity<Void> response = adminSettingsAdaptor.deleteAdminSettingsMember(memberId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new AdminSettingsMemberDeleteFailedException();
		}
	}

	public WeeklySummary getWeeklySummary(ResponseAdminSettingsDailySummaryDTO responseAdminSettingsDailySummaryDTO) {
		int orderCount = 0;
		long sales = 0;
		int signCount = 0;
		List<DailySummary> dailySummaries = responseAdminSettingsDailySummaryDTO.getDailySummaries();

		for (int i = 0; i < 7; i++) {
			orderCount += dailySummaries.get(i).getOrderCount();
			sales += dailySummaries.get(i).getSales() != null ? dailySummaries.get(i).getSales() : 0;
			signCount += dailySummaries.get(i).getSignupCount();
		}

		return new WeeklySummary(orderCount, sales, signCount);
	}

}
