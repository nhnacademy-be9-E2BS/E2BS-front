package com.nhnacademy.front.order.deliveryfee.model.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDeliveryFeeDTO {
	private long deliveryFeeId;
	private long deliveryFeeAmount;
	private long deliveryFeeFreeAmount;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime deliveryFeeDate;
}
