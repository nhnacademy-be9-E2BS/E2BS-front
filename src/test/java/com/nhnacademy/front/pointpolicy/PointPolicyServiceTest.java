package com.nhnacademy.front.pointpolicy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.pointpolicy.adpator.PointPolicyAdaptor;
import com.nhnacademy.front.pointpolicy.exception.*;
import com.nhnacademy.front.pointpolicy.model.domain.PointPolicyType;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyRegisterDTO;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyUpdateDTO;
import com.nhnacademy.front.pointpolicy.model.dto.response.ResponsePointPolicyDTO;
import com.nhnacademy.front.pointpolicy.service.impl.PointPolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
class PointPolicyServiceTest {

	@Mock
	private PointPolicyAdaptor pointPolicyAdaptor;

	@InjectMocks
	private PointPolicyServiceImpl pointPolicyService;

	@Test
	@DisplayName("포인트 정책 등록 - 성공")
	void testCreatePointPolicySuccess() {
		RequestPointPolicyRegisterDTO request =
			new RequestPointPolicyRegisterDTO(PointPolicyType.REGISTER, "회원가입 포인트", 100L);

		when(pointPolicyAdaptor.createPointPolicy(request)).thenReturn(ResponseEntity.ok().build());

		assertThatCode(() -> pointPolicyService.createPointPolicy(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("포인트 정책 등록 - 실패")
	void testCreatePointPolicyFail() {
		RequestPointPolicyRegisterDTO request =
			new RequestPointPolicyRegisterDTO(PointPolicyType.REGISTER, "회원가입 포인트", 100L);

		when(pointPolicyAdaptor.createPointPolicy(request))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		assertThatThrownBy(() -> pointPolicyService.createPointPolicy(request))
			.isInstanceOf(PointPolicyPostException.class);
	}

	@Test
	@DisplayName("회원가입 정책 조회 - 성공")
	void testGetRegisterPointPolicySuccess() {
		List<ResponsePointPolicyDTO> list = List.of(new ResponsePointPolicyDTO());

		when(pointPolicyAdaptor.getRegisterPointPolicies()).thenReturn(ResponseEntity.ok(list));

		List<ResponsePointPolicyDTO> result = pointPolicyService.getRegisterPointPolicy();

		assertThat(result).hasSize(1);
	}

	@Test
	@DisplayName("회원가입 정책 조회 - 실패")
	void testGetRegisterPointPolicyFail() {
		when(pointPolicyAdaptor.getRegisterPointPolicies())
			.thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

		assertThatThrownBy(() -> pointPolicyService.getRegisterPointPolicy())
			.isInstanceOf(PointPolicyGetException.class);
	}

	@Test
	@DisplayName("포인트 정책 활성화 - 성공")
	void testActivatePointPolicySuccess() {
		when(pointPolicyAdaptor.activatePointPolicy(1L)).thenReturn(ResponseEntity.ok().build());

		assertThatCode(() -> pointPolicyService.activatePointPolicy(1L)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("포인트 정책 활성화 - 실패")
	void testActivatePointPolicyFail() {
		when(pointPolicyAdaptor.activatePointPolicy(1L))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		assertThatThrownBy(() -> pointPolicyService.activatePointPolicy(1L))
			.isInstanceOf(PointPolicyUpdateException.class);
	}

	@Test
	@DisplayName("포인트 정책 수정 - 성공")
	void testUpdatePointPolicySuccess() {
		RequestPointPolicyUpdateDTO dto = new RequestPointPolicyUpdateDTO(30L);

		when(pointPolicyAdaptor.updatePointPolicy(eq(1L), any()))
			.thenReturn(ResponseEntity.ok().build());

		assertThatCode(() -> pointPolicyService.updatePointPolicy(1L, dto)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("포인트 정책 수정 - 실패")
	void testUpdatePointPolicyFail() {
		RequestPointPolicyUpdateDTO dto = new RequestPointPolicyUpdateDTO(30L);

		when(pointPolicyAdaptor.updatePointPolicy(eq(1L), any()))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		assertThatThrownBy(() -> pointPolicyService.updatePointPolicy(1L, dto))
			.isInstanceOf(PointPolicyUpdateException.class);
	}

	@Test
	@DisplayName("이미지 리뷰 정책 조회 - 성공")
	void testGetReviewImgPointPolicySuccess() {
		List<ResponsePointPolicyDTO> list = List.of(new ResponsePointPolicyDTO());

		when(pointPolicyAdaptor.getReviewImgPointPolicies()).thenReturn(ResponseEntity.ok(list));

		List<ResponsePointPolicyDTO> result = pointPolicyService.getReviewImgPointPolicy();

		assertThat(result).hasSize(1);
	}

	@Test
	@DisplayName("리뷰 정책 조회 - 성공")
	void testGetReviewPointPolicySuccess() {
		List<ResponsePointPolicyDTO> list = List.of(new ResponsePointPolicyDTO());

		when(pointPolicyAdaptor.getReviewPointPolicies()).thenReturn(ResponseEntity.ok(list));

		List<ResponsePointPolicyDTO> result = pointPolicyService.getReviewPointPolicy();

		assertThat(result).hasSize(1);
	}

	@Test
	@DisplayName("기본 적립률 정책 조회 - 성공")
	void testGetBookPointPolicySuccess() {
		List<ResponsePointPolicyDTO> list = List.of(new ResponsePointPolicyDTO());

		when(pointPolicyAdaptor.getBookPointPolicies()).thenReturn(ResponseEntity.ok(list));

		List<ResponsePointPolicyDTO> result = pointPolicyService.getBookPointPolicy();

		assertThat(result).hasSize(1);
	}
}
