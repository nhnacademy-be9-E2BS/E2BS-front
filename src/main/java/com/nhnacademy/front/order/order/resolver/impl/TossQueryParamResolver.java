package com.nhnacademy.front.order.order.resolver.impl;

import org.springframework.stereotype.Component;

import com.nhnacademy.front.order.order.model.dto.request.RequestPaymentApproveDTO;
import com.nhnacademy.front.order.order.resolver.PaymentQueryParamResolver;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class TossQueryParamResolver implements PaymentQueryParamResolver {
	@Override
	public boolean supports(HttpServletRequest request) {
		return request.getParameter("paymentKey") != null;
	}

	@Override
	public RequestPaymentApproveDTO resolve(HttpServletRequest request) {
		return new RequestPaymentApproveDTO(
			request.getParameter("orderId"),
			request.getParameter("paymentKey"),
			Long.parseLong(request.getParameter("amount")),
			"TOSS"
		);
	}
}
