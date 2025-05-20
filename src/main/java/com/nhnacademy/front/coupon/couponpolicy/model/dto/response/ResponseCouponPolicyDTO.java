package com.nhnacademy.front.coupon.couponpolicy.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCouponPolicyDTO {
	private long couponPolicyId;
	private long couponPolicyMinimum;
	private Long couponPolicyMaximumAmount;
	private Long couponPolicySalePrice;
	private Integer couponPolicyDiscountRate;
	private LocalDateTime couponPolicyCreatedAt;
	private String couponPolicyName;
}