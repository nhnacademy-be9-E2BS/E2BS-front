package com.nhnacademy.front.product.contributor.dto.response;

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
public class ResponseContributorDTO {
	@NotBlank
	String contributorName;
	@NotNull
	long contributorId;
	@NotBlank
	String positionName;
	@NotNull
	long positionId;
}
