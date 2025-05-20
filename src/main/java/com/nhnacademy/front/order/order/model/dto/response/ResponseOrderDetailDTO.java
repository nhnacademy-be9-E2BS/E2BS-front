package com.nhnacademy.front.order.order.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrderDetailDTO {
	private long orderDetailId;
	private int orderQuantity;
	private long orderDetailPerPrice;

	private Long productId;
	private String productName;

	private Long wrapperId;
	private String wrapperName;
	private Long wrapperPrice;

	private Long reviewId;
}
