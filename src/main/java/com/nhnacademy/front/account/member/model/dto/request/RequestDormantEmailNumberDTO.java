package com.nhnacademy.front.account.member.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDormantEmailNumberDTO {

	@NotNull
	private String dormantEmailNumber;

}
