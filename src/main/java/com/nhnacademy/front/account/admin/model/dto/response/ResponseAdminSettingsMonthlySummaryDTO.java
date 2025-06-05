package com.nhnacademy.front.account.admin.model.dto.response;

import com.nhnacademy.front.account.admin.model.domain.MonthlySummary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminSettingsMonthlySummaryDTO {

	private MonthlySummary monthlySummary;

}
