package com.nhnacademy.front.order.order.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrderReturnDTO {
	private long id;
	private String orderCode;
	private String orderReturnReason;
	private String returnCategory;
	private LocalDateTime orderReturnCreatedAt;
	private long orderReturnAmount;
}
