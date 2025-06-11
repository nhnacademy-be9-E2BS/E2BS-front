package com.nhnacademy.front.coupon.membercoupon;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponAdaptor;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponBoxAdaptor;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponOrderAdaptor;
import com.nhnacademy.front.coupon.membercoupon.exception.CouponBoxGetProcessException;
import com.nhnacademy.front.coupon.membercoupon.exception.IssueCouponToAllMemberProcessException;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseOrderCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.impl.MemberCouponServiceImpl;

@ExtendWith(MockitoExtension.class)
class MemberCouponServiceTest {

	@Mock
	MemberCouponAdaptor memberCouponAdaptor;

	@Mock
	MemberCouponBoxAdaptor memberCouponBoxAdaptor;

	@Mock
	MemberCouponOrderAdaptor memberCouponOrderAdaptor;

	@InjectMocks
	MemberCouponServiceImpl memberCouponService;

	@Test
	@DisplayName("전체 회원 쿠폰 발급 - 성공")
	void testIssueCouponToAllMemberSuccess() {
		RequestAllMemberCouponDTO dto = new RequestAllMemberCouponDTO();
		when(memberCouponAdaptor.postMemberCoupons(dto)).thenReturn(ResponseEntity.ok().build());

		memberCouponService.issueCouponToAllMember(dto);

		verify(memberCouponAdaptor).postMemberCoupons(dto);
	}

	@Test
	@DisplayName("전체 회원 쿠폰 발급 - 실패")
	void testIssueCouponToAllMemberFail() {
		RequestAllMemberCouponDTO dto = new RequestAllMemberCouponDTO();
		when(memberCouponAdaptor.postMemberCoupons(dto)).thenReturn(ResponseEntity.badRequest().build());

		assertThatThrownBy(() -> memberCouponService.issueCouponToAllMember(dto))
			.isInstanceOf(IssueCouponToAllMemberProcessException.class)
			.hasMessageContaining("전체 회원 쿠폰 발급 실패");

		verify(memberCouponAdaptor).postMemberCoupons(dto);
	}

	@Test
	@DisplayName("회원 전체 쿠폰 조회 - 성공")
	void testGetMemberCouponsByMemberIdSuccess() {
		Pageable pageable = PageRequest.of(0, 10);
		PageResponse<ResponseMemberCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(new ResponseMemberCouponDTO()));

		when(memberCouponBoxAdaptor.getMemberCouponsByMemberId("testUser", pageable))
			.thenReturn(ResponseEntity.ok(page));

		PageResponse<ResponseMemberCouponDTO> result = memberCouponService.getMemberCouponsByMemberId("testUser", pageable);

		assertThat(result.getContent()).hasSize(1);
		verify(memberCouponBoxAdaptor).getMemberCouponsByMemberId("testUser", pageable);
	}

	@Test
	@DisplayName("회원 전체 쿠폰 조회 - 실패")
	void testGetMemberCouponsByMemberIdFail() {
		Pageable pageable = PageRequest.of(0, 10);

		when(memberCouponBoxAdaptor.getMemberCouponsByMemberId("testUser", pageable))
			.thenReturn(ResponseEntity.badRequest().build());

		assertThatThrownBy(() -> memberCouponService.getMemberCouponsByMemberId("testUser", pageable))
			.isInstanceOf(CouponBoxGetProcessException.class)
			.hasMessageContaining("회원 아이디로 전체 쿠폰 조회 실패");

		verify(memberCouponBoxAdaptor).getMemberCouponsByMemberId("testUser", pageable);
	}

	@Test
	@DisplayName("회원 사용 가능 쿠폰 조회 - 성공")
	void testGetUsableMemberCouponsByMemberIdSuccess() {
		Pageable pageable = PageRequest.of(0, 10);
		PageResponse<ResponseMemberCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(new ResponseMemberCouponDTO()));

		when(memberCouponBoxAdaptor.getUsableMemberCouponsByMemberId("user1", pageable))
			.thenReturn(ResponseEntity.ok(page));

		PageResponse<ResponseMemberCouponDTO> result = memberCouponService.getUsableMemberCouponsByMemberId("user1", pageable);

		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	@DisplayName("회원 사용 불가 쿠폰 조회 - 실패")
	void testGetUnusableMemberCouponsByMemberIdFail() {
		Pageable pageable = PageRequest.of(0, 10);

		when(memberCouponBoxAdaptor.getUnusableMemberCouponsByMemberId("user1", pageable))
			.thenReturn(ResponseEntity.badRequest().build());

		assertThatThrownBy(() -> memberCouponService.getUnusableMemberCouponsByMemberId("user1", pageable))
			.isInstanceOf(CouponBoxGetProcessException.class)
			.hasMessageContaining("회원 아이디로 사용불가 쿠폰 조회 실패");
	}

	@Test
	@DisplayName("주문서 내 적용 가능한 쿠폰 조회 - 성공")
	void testGetCouponsInOrderSuccess() {
		List<Long> productIds = List.of(1L, 2L);
		List<ResponseOrderCouponDTO> coupons = List.of(new ResponseOrderCouponDTO());

		when(memberCouponOrderAdaptor.getCouponsInOrder("user1", productIds))
			.thenReturn(ResponseEntity.ok(coupons));

		List<ResponseOrderCouponDTO> result = memberCouponService.getCouponsInOrder("user1", productIds);

		assertThat(result).hasSize(1);
		verify(memberCouponOrderAdaptor).getCouponsInOrder("user1", productIds);
	}

	@Test
	@DisplayName("주문서 내 쿠폰 조회 실패")
	void testGetCouponsInOrderFail() {
		List<Long> productIds = List.of(1L, 2L);

		when(memberCouponOrderAdaptor.getCouponsInOrder("user1", productIds))
			.thenReturn(ResponseEntity.status(500).build());

		assertThatThrownBy(() -> memberCouponService.getCouponsInOrder("user1", productIds))
			.isInstanceOf(CouponBoxGetProcessException.class)
			.hasMessageContaining("회원 아이디로 주문서에서 적용 가능한 쿠폰 조회 실패");
	}
}
