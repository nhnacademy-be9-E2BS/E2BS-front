package com.nhnacademy.front.coupon.coupon.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCouponDTO {
	private long couponId;
	private long couponPolicyId;
	private String couponPolicyName;
	private String couponName;
	private Long categoryId;
	private String categoryName;
	private Long productId;
	private String productTitle;
	private boolean couponIsActive;
}