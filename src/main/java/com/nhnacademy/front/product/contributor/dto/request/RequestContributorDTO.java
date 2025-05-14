package com.nhnacademy.front.product.contributor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestContributorDTO {
	@NotBlank
	String contributorName;
	@NotBlank
	String positionId;

	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
}
