package com.nhnacademy.front.pointpolicy.model.dto.request;

import com.nhnacademy.front.pointpolicy.model.domain.PointPolicyType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPointPolicyRegisterDTO {

	@NotNull
	private PointPolicyType pointPolicyType;

	@NotNull
	private String pointPolicyName;

	@NotNull
	private Long pointPolicyFigure;

}
