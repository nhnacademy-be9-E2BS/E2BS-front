package com.nhnacademy.front.order.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nhnacademy.front.order.order.model.dto.request.RequestPaymentApproveDTO;
import com.nhnacademy.front.order.order.resolver.PaymentQueryParamResolver;
import com.nhnacademy.front.order.order.resolver.PaymentQueryParamResolverFactory;

import jakarta.servlet.http.HttpServletRequest;

class PaymentQueryParamResolverFactoryTest {

	PaymentQueryParamResolver supportedResolver;
	PaymentQueryParamResolver unsupportedResolver;
	PaymentQueryParamResolverFactory factory;

	HttpServletRequest mockRequest;

	@BeforeEach
	void setUp() {
		supportedResolver = mock(PaymentQueryParamResolver.class);
		unsupportedResolver = mock(PaymentQueryParamResolver.class);
		mockRequest = mock(HttpServletRequest.class);

		factory = new PaymentQueryParamResolverFactory(
			List.of(unsupportedResolver, supportedResolver)
		);
	}

	@Test
	void testResolve_withSupportedResolver_returnsDto() {
		// given
		RequestPaymentApproveDTO expectedDto = new RequestPaymentApproveDTO();
		when(unsupportedResolver.supports(mockRequest)).thenReturn(false);
		when(supportedResolver.supports(mockRequest)).thenReturn(true);
		when(supportedResolver.resolve(mockRequest)).thenReturn(expectedDto);

		// when
		RequestPaymentApproveDTO result = factory.resolve(mockRequest);

		// then
		assertEquals(expectedDto, result);
		verify(supportedResolver).resolve(mockRequest);
		verify(unsupportedResolver, never()).resolve(mockRequest);
	}

	@Test
	void testResolve_withoutSupportedResolver_throwsException() {
		// given
		when(unsupportedResolver.supports(mockRequest)).thenReturn(false);
		when(supportedResolver.supports(mockRequest)).thenReturn(false);

		// when & then
		assertThatThrownBy(() -> factory.resolve(mockRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("지원되지 않는 결제 파라미터입니다.");
	}
}
