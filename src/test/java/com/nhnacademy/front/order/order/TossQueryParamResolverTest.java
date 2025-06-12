package com.nhnacademy.front.order.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nhnacademy.front.order.order.model.dto.request.RequestPaymentApproveDTO;
import com.nhnacademy.front.order.order.resolver.impl.TossQueryParamResolver;

import jakarta.servlet.http.HttpServletRequest;

class TossQueryParamResolverTest {
	TossQueryParamResolver resolver = new TossQueryParamResolver();

	@Test
	@DisplayName("paymentKey 파라미터 존재 시 true 확인")
	void testSupports_returnsTrueWhenPaymentKeyPresent() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("paymentKey")).thenReturn("TEST-PAYMENT-KEY");

		boolean result = resolver.supports(request);

		assertTrue(result);
	}

	@Test
	@DisplayName("paymentKey 파라미터 없을 시 false 확인")
	void testSupports_returnsFalseWhenPaymentKeyAbsent() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("paymentKey")).thenReturn(null);

		boolean result = resolver.supports(request);

		assertFalse(result);
	}

	@Test
	@DisplayName("토스 결제 승인 요청 시 DTO 변환 확인")
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
	@DisplayName("지원하지 않는 형식 요청 시")
	void testResolve_throwsNumberFormatException_whenAmountIsInvalid() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("orderId")).thenReturn("ORDER-002");
		when(request.getParameter("paymentKey")).thenReturn("WRONG-PAYMENT-KEY");
		when(request.getParameter("amount")).thenReturn("invalid_amount");

		assertThatThrownBy(() -> resolver.resolve(request))
			.isInstanceOf(NumberFormatException.class);
	}
}
