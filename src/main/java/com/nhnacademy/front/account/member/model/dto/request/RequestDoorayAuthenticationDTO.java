package com.nhnacademy.front.account.member.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDoorayAuthenticationDTO {

	@NotBlank
	private String botName;
	@NotBlank
	private String text;

}
