package com.nhnacademy.front.account.admin.model.dto.response;

import java.util.List;

import com.nhnacademy.front.account.admin.model.domain.DailySummary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminSettingsDailySummaryDTO {

	private List<DailySummary> dailySummaries;

}
