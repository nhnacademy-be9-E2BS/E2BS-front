package com.nhnacademy.front.product.product.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductCouponDTO {
	/**
	 * 상품에 대한 쿠폰을 발급하기 위해 보내주는 정보
	 */
	private long productId;
	private String productTitle;
	private String publisherName;
}
