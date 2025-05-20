package com.nhnacademy.front.coupon.membercoupon.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자가 쿠폰함에서 쿠폰을 조회할때 사용하는 DTO
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMemberCouponDTO {
	private Long couponId;
	private String couponName;
	private String couponPolicyName;
	private Long categoryId;
	private String categoryName;
	private Long productId;
	private String productTitle;
	private LocalDateTime memberCouponCreatedAt;
	private LocalDateTime memberCouponPeriod;
	private boolean memberCouponUsed;
}
