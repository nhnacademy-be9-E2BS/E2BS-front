package com.nhnacademy.front.account.customer.model.dto.response;

import com.nhnacademy.front.cart.model.dto.order.RequestCartOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCustomerRegisterDTO {

	private long customerId;

	private RequestCartOrderDTO requestCartOrder;

}
