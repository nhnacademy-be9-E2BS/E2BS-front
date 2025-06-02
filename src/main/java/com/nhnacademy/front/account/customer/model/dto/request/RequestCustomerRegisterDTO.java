package com.nhnacademy.front.account.customer.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCustomerRegisterDTO {

	@Email
	private String customerEmail;
	@NotBlank
	private String memberName;
	@NotBlank
	private String customerPassword;
	@NotBlank
	private String customerPasswordCheck;

}
