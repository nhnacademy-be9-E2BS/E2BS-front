package com.nhnacademy.front.product.contributor.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPositionDTO {
	@NotBlank
	private String positionName;

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
}
