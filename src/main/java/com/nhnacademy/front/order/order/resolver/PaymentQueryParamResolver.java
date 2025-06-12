package com.nhnacademy.front.order.order.resolver;

import com.nhnacademy.front.order.order.model.dto.request.RequestPaymentApproveDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentQueryParamResolver {
	boolean supports(HttpServletRequest request);
	RequestPaymentApproveDTO resolve(HttpServletRequest request);
}
