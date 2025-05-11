package com.nhnacademy.front.order.order.model.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderDTO {

	private Long memberCouponId;

	private long deliveryFeeId;

	private long customerId;

	private String orderReceiverName;
	private String orderReceiverPhone;
	private String orderReceiverTel;

	private String orderAddressCode;
	private String orderAddressInfo;
	private String orderAddressDetail;
	private String orderAddressExtra;

	private long orderPointAmount;
	private String orderMemo;

	private boolean orderPaymentStatus;

	private LocalDate orderReceivedDate;
	private LocalDate orderShipmentDate;

	private LocalDateTime orderCreatedAt;

}
