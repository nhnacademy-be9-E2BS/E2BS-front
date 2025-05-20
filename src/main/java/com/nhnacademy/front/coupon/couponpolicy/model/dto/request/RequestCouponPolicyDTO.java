package com.nhnacademy.front.coupon.couponpolicy.model.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestCouponPolicyDTO {
	@NotNull
	private long couponPolicyMinimum;
	private Long couponPolicyMaximumAmount;
	private Long couponPolicySalePrice;
	private Integer couponPolicyDiscountRate;
	@NotNull
	private LocalDateTime couponPolicyCreatedAt;
	@NotNull
	private String couponPolicyName;
}