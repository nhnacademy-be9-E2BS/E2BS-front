package com.nhnacademy.front.order.order.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrderResultDTO {
	private String orderId;
	private long amount;
}
