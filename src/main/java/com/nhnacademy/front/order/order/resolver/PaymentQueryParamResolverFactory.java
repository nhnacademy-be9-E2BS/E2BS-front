package com.nhnacademy.front.order.order.resolver;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nhnacademy.front.order.order.model.dto.request.RequestPaymentApproveDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentQueryParamResolverFactory {
	private final List<PaymentQueryParamResolver> resolvers;

	public RequestPaymentApproveDTO resolve(HttpServletRequest request) {
		return resolvers.stream()
			.filter(resolver -> resolver.supports(request))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("지원되지 않는 결제 파라미터입니다."))
			.resolve(request);
	}
}
