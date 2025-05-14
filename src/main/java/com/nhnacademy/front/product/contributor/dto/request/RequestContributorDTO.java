package com.nhnacademy.front.product.contributor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestContributorDTO {
	@NotBlank
	String contributorName;
	@NotNull
	long positionId;

	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}

	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}
}
