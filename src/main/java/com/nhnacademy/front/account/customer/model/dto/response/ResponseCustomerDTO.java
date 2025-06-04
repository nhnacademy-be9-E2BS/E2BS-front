package com.nhnacademy.front.account.customer.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCustomerDTO {
	private String customerName;
	private long customerId;
}
