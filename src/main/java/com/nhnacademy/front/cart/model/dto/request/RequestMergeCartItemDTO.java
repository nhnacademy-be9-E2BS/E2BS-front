package com.nhnacademy.front.cart.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMergeCartItemDTO {

	@NotBlank
	private String memberId;

	@NotBlank
	private String sessionId;

}
