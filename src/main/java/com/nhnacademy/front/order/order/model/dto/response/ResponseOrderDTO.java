package com.nhnacademy.front.order.order.model.dto.response;

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
public class ResponseOrderDTO {
	private long customerId;
	private String memberId;
	private boolean isMember;
	private long deliveryFeeId;
	private long deliveryFeeAmount;
	private long deliveryFeeFreeAmount;
	private Long memberCouponId;
	private String orderCode;
	private String receiverName;
	private String receiverPhone;
	private String receiverTel;
	private String addressCode;
	private String addressInfo;
	private String addressDetail;
	private String addressExtra;
	private long pointAmount;
	private long paymentAmount;
	private String memo;
	private boolean isPaid;
	private LocalDate receiveDate;
	private LocalDate shipmentDate;
	private LocalDateTime createdAt;
	private String state;
	private String paymentMethod;
}
