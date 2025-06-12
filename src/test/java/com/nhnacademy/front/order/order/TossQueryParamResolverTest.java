package com.nhnacademy.front.order.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.nhnacademy.front.order.order.model.dto.request.RequestPaymentApproveDTO;
import com.nhnacademy.front.order.order.resolver.impl.TossQueryParamResolver;

import jakarta.servlet.http.HttpServletRequest;

class TossQueryParamResolverTest {
	TossQueryParamResolver resolver = new TossQueryParamResolver();

	@Test
	void testSupports_returnsTrueWhenPaymentKeyPresent() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("paymentKey")).thenReturn("TEST-PAYMENT-KEY");

		boolean result = resolver.supports(request);

		assertThat(result).isTrue();
	}

	@Test
	void testSupports_returnsFalseWhenPaymentKeyAbsent() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("paymentKey")).thenReturn(null);

		boolean result = resolver.supports(request);

		assertFalse(result);
	}

	@Test
	void testResolve_returnsDtoWithCorrectValues() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("orderId")).thenReturn("ORDER-001");
		when(request.getParameter("paymentKey")).thenReturn("TEST-PAYMENT-KEY");
		when(request.getParameter("amount")).thenReturn("5000");

		RequestPaymentApproveDTO dto = resolver.resolve(request);

		assertNotNull(dto);
		assertEquals("ORDER-001", dto.getOrderId());
		assertEquals("TEST-PAYMENT-KEY", dto.getPaymentKey());
		assertEquals(5000L, dto.getAmount());
		assertEquals("TOSS", dto.getProvider());
	}

	@Test
	void testResolve_throwsNumberFormatException_whenAmountIsInvalid() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("orderId")).thenReturn("ORDER-002");
		when(request.getParameter("paymentKey")).thenReturn("WRONG-PAYMENT-KEY");
		when(request.getParameter("amount")).thenReturn("invalid_amount");

		assertThatThrownBy(() -> resolver.resolve(request))
			.isInstanceOf(NumberFormatException.class);
	}
}
