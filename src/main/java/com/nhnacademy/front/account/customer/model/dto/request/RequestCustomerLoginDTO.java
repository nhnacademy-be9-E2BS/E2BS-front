package com.nhnacademy.front.account.customer.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCustomerLoginDTO {

	@Email
	private String email;

	@NotBlank
	private String password;

}
