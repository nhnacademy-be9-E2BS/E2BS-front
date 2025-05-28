package com.nhnacademy.front.pointpolicy.model.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.front.pointpolicy.model.domain.PointPolicyType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePointPolicyDTO {
	private Long pointPolicyId;
	private PointPolicyType pointPolicyType;
	private String pointPolicyName;
	private Long pointPolicyFigure;
	private LocalDateTime pointPolicyCreatedAt;
	private Boolean pointPolicyIsActive;
}
