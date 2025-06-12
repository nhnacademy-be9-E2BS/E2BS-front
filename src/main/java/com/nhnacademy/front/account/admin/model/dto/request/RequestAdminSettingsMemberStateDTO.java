package com.nhnacademy.front.account.admin.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAdminSettingsMemberStateDTO {

	@NotNull
	private String memberStateName;

}
