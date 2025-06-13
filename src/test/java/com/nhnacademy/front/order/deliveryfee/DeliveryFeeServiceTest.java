package com.nhnacademy.front.order.deliveryfee;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.deliveryfee.adaptor.DeliveryFeeAdaptor;
import com.nhnacademy.front.order.deliveryfee.adaptor.DeliveryFeeAdminAdaptor;
import com.nhnacademy.front.order.deliveryfee.model.dto.request.RequestDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;

@ExtendWith(MockitoExtension.class)
class DeliveryFeeServiceTest {
	@Mock
	private DeliveryFeeAdaptor deliveryFeeAdaptor;

	@Mock
	private DeliveryFeeAdminAdaptor deliveryFeeAdminAdaptor;

	@InjectMocks
	private DeliveryFeeSevice deliveryFeeSevice;

	@Test
	@DisplayName("관리자 배송비 정책 목록 조회")
	void testGetDeliveryFees() {
		// given
		ResponseDeliveryFeeDTO dto = new ResponseDeliveryFeeDTO(1L, 2500, 50000, LocalDateTime.now());
		PageResponse<ResponseDeliveryFeeDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(dto));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);

		when(deliveryFeeAdminAdaptor.getDeliveryFees(any())).thenReturn(ResponseEntity.ok(pageResponse));

		// when
		ResponseEntity<PageResponse<ResponseDeliveryFeeDTO>> result = deliveryFeeSevice.getDeliveryFees(
			PageRequest.of(0, 10));

		// then
		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(result.getBody().getContent()).hasSize(1);
		assertThat(result.getBody().getContent().get(0).getDeliveryFeeAmount()).isEqualTo(2500);
	}

	@Test
	@DisplayName("배송비 정책 추가")
	void testCreateDeliveryFee() {
		// given
		RequestDeliveryFeeDTO request = new RequestDeliveryFeeDTO(2500, 50000);
		when(deliveryFeeAdminAdaptor.postDeliveryFee(any())).thenReturn(ResponseEntity.ok().build());

		// when
		ResponseEntity<Void> result = deliveryFeeSevice.createDeliveryFee(request);

		// then
		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
	}

	@Test
	@DisplayName("현재 적용 중인 배송비 정책 조회")
	void testGetCurrentDeliveryFee() {
		// given
		ResponseDeliveryFeeDTO dto = new ResponseDeliveryFeeDTO(1L, 2500, 50000, LocalDateTime.now());
		when(deliveryFeeAdaptor.getCurrentDeliveryFee()).thenReturn(ResponseEntity.ok(dto));

		// when
		ResponseDeliveryFeeDTO result = deliveryFeeSevice.getCurrentDeliveryFee();

		// then
		assertThat(result.getDeliveryFeeAmount()).isEqualTo(2500);
	}
}
