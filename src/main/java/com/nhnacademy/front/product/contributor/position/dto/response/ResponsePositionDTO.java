package com.nhnacademy.front.product.contributor.position.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePositionDTO {
	@NotNull
	private long positionId;
	@NotBlank
	private String positionName;
}
