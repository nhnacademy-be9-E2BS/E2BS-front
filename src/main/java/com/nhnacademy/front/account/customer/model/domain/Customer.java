package com.nhnacademy.front.account.customer.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	private long customerId;
	private String customerEmail;
	private String customerPassword;
	private String customerName;

}
