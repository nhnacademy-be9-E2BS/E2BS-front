package com.nhnacademy.front.account.member.service;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nhnacademy.front.account.customer.model.domain.Customer;
import com.nhnacademy.front.account.member.adaptor.MemberCouponAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberInfoAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberOrderAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberPointHistoryAdaptor;
import com.nhnacademy.front.account.member.exception.NotFoundMemberInfoException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.exception.PasswordNotEqualsException;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberOrderDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberPointDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberRecentOrderDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMypageMemberCouponDTO;
import com.nhnacademy.front.account.memberrank.model.domain.MemberRank;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRole;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;
import com.nhnacademy.front.account.memberstate.model.domain.MemberState;
import com.nhnacademy.front.account.memberstate.model.domain.MemberStateName;
import com.nhnacademy.front.account.socialauth.model.domain.SocialAuth;
import com.nhnacademy.front.account.socialauth.model.domain.SocialAuthName;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class MemberMypageServiceTest {

	@InjectMocks
	private MemberMypageService memberMypageService;

	@Mock
	private MemberCouponAdaptor memberCouponAdaptor;

	@Mock
	private MemberPointHistoryAdaptor memberPointHistoryAdaptor;

	@Mock
	private MemberInfoAdaptor memberInfoAdaptor;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private ValueOperations<String, Object> valueOperations;

	@Mock
	private MemberOrderAdaptor memberOrderAdaptor;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("마이페이지 총 주문 건수 조회 메서드 테스트")
	void getMemberOrderMethodTest() throws Exception {

		// Given
		ResponseMemberOrderDTO responseMemberOrderDTO = new ResponseMemberOrderDTO(
			"user", 1
		);
		ResponseEntity<ResponseMemberOrderDTO> response = new ResponseEntity<>(responseMemberOrderDTO,
			HttpStatus.CREATED);

		// When
		when(memberOrderAdaptor.getMemberOrdersCnt("user")).thenReturn(response);

		int orderCnt = memberMypageService.getMemberOrder("user");

		// Then
		Assertions.assertThat(orderCnt).isEqualTo(response.getBody().getOrderCnt());

	}

	@Test
	@DisplayName("마이페이지 총 주문 건수 조회 메서드 EmptyResponseException 테스트")
	void getMemberOrderMethodEmptyResponseExceptionTest() throws Exception {

		// Given
		ResponseMemberOrderDTO responseMemberOrderDTO = new ResponseMemberOrderDTO(
			"user", 1
		);
		ResponseEntity<ResponseMemberOrderDTO> response = new ResponseEntity<>(responseMemberOrderDTO,
			HttpStatus.BAD_REQUEST);

		// When
		when(memberOrderAdaptor.getMemberOrdersCnt("user")).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			memberMypageService.getMemberOrder("user");
		});

	}

	@Test
	@DisplayName("회원 보유 쿠폰 개수 조회 메서드 테스트")
	void getMemberCouponMethodTest() throws Exception {

		// Given
		ResponseMypageMemberCouponDTO responseMypageMemberCouponDTO = new ResponseMypageMemberCouponDTO(
			"user", 1
		);

		// When
		when(memberCouponAdaptor.getMemberCouponAmount("user")).thenReturn(responseMypageMemberCouponDTO);

		int result = memberMypageService.getMemberCoupon(new RequestMemberIdDTO("user"));

		// Then
		Assertions.assertThat(result).isEqualTo(responseMypageMemberCouponDTO.getCouponCnt());

	}

	@Test
	@DisplayName("회원 보유 쿠폰 개수 조회 메서드 EmptyResponseException 테스트")
	void getMemberCouponMethodEmptyResponseExceptionTest() throws Exception {

		// Given

		// When
		when(memberCouponAdaptor.getMemberCouponAmount("user")).thenReturn(null);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			memberMypageService.getMemberCoupon(new RequestMemberIdDTO("user"));
		});
	}

	@Test
	@DisplayName("회원 보유 포인트 조회 메서드 테스트")
	void getMemberPointTest() throws Exception {

		// Given
		ResponseMemberPointDTO responseMemberPointDTO = new ResponseMemberPointDTO(
			"user", 1L
		);

		// When
		when(memberPointHistoryAdaptor.getMemberPointAmount("user")).thenReturn(responseMemberPointDTO);

		long result = memberMypageService.getMemberPoint(new RequestMemberIdDTO("user"));

		// Then
		Assertions.assertThat(result).isEqualTo(responseMemberPointDTO.getPointAmount());

	}

	@Test
	@DisplayName("회원 보유 포인트 조회 메서드 EmptyResponseException 테스트")
	void getMemberPointEmptyResponseExceptionTest() throws Exception {

		// Given

		// When
		when(memberPointHistoryAdaptor.getMemberPointAmount("user")).thenReturn(null);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			memberMypageService.getMemberPoint(new RequestMemberIdDTO("user"));
		});

	}

	@Test
	@DisplayName("회원 등급 조회 메서드 테스트")
	void getMemberRankNameMethodTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO(
				new Customer(),
				"user",
				LocalDate.now(),
				"01012345678",
				LocalDate.now(),
				LocalDate.now(),
				new MemberRank(1, RankName.NORMAL, 1, 1L),
				new MemberState(1, MemberStateName.ACTIVE),
				new MemberRole(1, MemberRoleName.MEMBER),
				new SocialAuth(1, SocialAuthName.WEB)
			);
			ResponseEntity<ResponseMemberInfoDTO> response = new ResponseEntity<>(responseMemberInfoDTO,
				HttpStatus.CREATED);

			// When
			when(memberInfoAdaptor.getMemberInfo("user")).thenReturn(response);

			// Then
			Assertions.assertThatCode(() -> {
				memberMypageService.getMemberRankName(any(HttpServletRequest.class));
			});

		}
	}

	@Test
	@DisplayName("회원 등급 조회 메서드 NotFoundMemberRankNameException 테스트")
	void getMemberRankNameMethodNotFoundMemberRankNameExceptionTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO(
				new Customer(),
				"user",
				LocalDate.now(),
				"01012345678",
				LocalDate.now(),
				LocalDate.now(),
				new MemberRank(1, RankName.NORMAL, 1, 1L),
				new MemberState(1, MemberStateName.ACTIVE),
				new MemberRole(1, MemberRoleName.MEMBER),
				new SocialAuth(1, SocialAuthName.WEB)
			);
			ResponseEntity<ResponseMemberInfoDTO> response = new ResponseEntity<>(responseMemberInfoDTO,
				HttpStatus.BAD_REQUEST);

			// When
			when(memberInfoAdaptor.getMemberInfo("user")).thenReturn(response);

			// Then
			org.junit.jupiter.api.Assertions.assertThrows(NotFoundMemberRankNameException.class, () -> {
				memberMypageService.getMemberRankName(any(HttpServletRequest.class));
			});

		}
	}

	@Test
	@DisplayName("회원 정보를 조회하는 메서드 테스트")
	void getMemberInfoMethodTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO(
				new Customer(),
				"user",
				LocalDate.now(),
				"01012345678",
				LocalDate.now(),
				LocalDate.now(),
				new MemberRank(1, RankName.NORMAL, 1, 1L),
				new MemberState(1, MemberStateName.ACTIVE),
				new MemberRole(1, MemberRoleName.MEMBER),
				new SocialAuth(1, SocialAuthName.WEB)
			);
			ResponseEntity<ResponseMemberInfoDTO> response = new ResponseEntity<>(responseMemberInfoDTO,
				HttpStatus.CREATED);

			// When
			when(memberInfoAdaptor.getMemberInfo("user")).thenReturn(response);

			// Then
			Assertions.assertThatCode(() -> {
				memberMypageService.getMemberInfo(any(HttpServletRequest.class));
			}).doesNotThrowAnyException();
		}
	}

	@Test
	@DisplayName("회원 정보를 조회하는 메서드 NotFoundMemberInfoException 테스트")
	void getMemberInfoMethodNotFoundMemberInfoExceptionTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO(
				new Customer(),
				"user",
				LocalDate.now(),
				"01012345678",
				LocalDate.now(),
				LocalDate.now(),
				new MemberRank(1, RankName.NORMAL, 1, 1L),
				new MemberState(1, MemberStateName.ACTIVE),
				new MemberRole(1, MemberRoleName.MEMBER),
				new SocialAuth(1, SocialAuthName.WEB)
			);
			ResponseEntity<ResponseMemberInfoDTO> response = new ResponseEntity<>(responseMemberInfoDTO,
				HttpStatus.BAD_REQUEST);

			// When
			when(memberInfoAdaptor.getMemberInfo("user")).thenReturn(response);

			// Then
			org.junit.jupiter.api.Assertions.assertThrows(NotFoundMemberInfoException.class, () -> {
				memberMypageService.getMemberInfo(any(HttpServletRequest.class));
			});

		}
	}

	@Test
	@DisplayName("회원 정보를 수정하는 메서드 테스트")
	void updateMemberInfoMethodTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			RequestMemberInfoDTO requestMemberInfoDTO = new RequestMemberInfoDTO(
				"user", "user", "user@naver.com", LocalDate.now(), "010-1234-1234",
				"1234", "1234"
			);

			ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

			// When
			when(memberInfoAdaptor.updateMemberInfo(eq("user"), eq(requestMemberInfoDTO)))
				.thenReturn(response);

			// Then
			Assertions.assertThatCode(() ->
				memberMypageService.updateMemberInfo(mock(HttpServletRequest.class), requestMemberInfoDTO)
			).doesNotThrowAnyException();
		}
	}

	@Test
	@DisplayName("회원 정보를 수정하는 메서드 PasswordNotEqualsException 테스트")
	void updateMemberInfoMethodPasswordNotEqualsExceptionTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			RequestMemberInfoDTO requestMemberInfoDTO = new RequestMemberInfoDTO(
				"user", "user", "user@naver.com", LocalDate.now(), "010-1234-1234",
				"1234", "12345"
			);

			// When

			// Then
			org.junit.jupiter.api.Assertions.assertThrows(PasswordNotEqualsException.class, () -> {
				memberMypageService.updateMemberInfo(any(HttpServletRequest.class), requestMemberInfoDTO);
			});
		}
	}

	@Test
	@DisplayName("회원 정보를 수정하는 메서드 NotFoundMemberInfoException 테스트")
	void updateMemberInfoMethodNotFoundMemberInfoExceptionTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			RequestMemberInfoDTO requestMemberInfoDTO = new RequestMemberInfoDTO(
				"user", "user", "user@naver.com", LocalDate.now(), "010-1234-1234",
				"1234", "1234"
			);

			ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			// When
			when(memberInfoAdaptor.updateMemberInfo(eq("user"), eq(requestMemberInfoDTO)))
				.thenReturn(response);

			// Then
			org.junit.jupiter.api.Assertions.assertThrows(NotFoundMemberInfoException.class, () -> {
				memberMypageService.updateMemberInfo(any(HttpServletRequest.class), requestMemberInfoDTO);
			});
		}
	}

	@Test
	@DisplayName("회원 탈퇴하는 메서드 테스트")
	void withdrawMemberMethodTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse responseMock = mock(HttpServletResponse.class);

			// When
			when(memberInfoAdaptor.withdrawMember("user")).thenReturn(response);

			// Then
			Assertions.assertThatCode(() ->
				memberMypageService.withdrawMember(request, responseMock)
			).doesNotThrowAnyException();

		}
	}

	@Test
	@DisplayName("회원 탈퇴하는 메서드 NotFoundMemberInfoException 테스트")
	void withdrawMemberMethodNotFoundMemberInfoExceptionTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse responseMock = mock(HttpServletResponse.class);

			// When
			when(memberInfoAdaptor.withdrawMember("user")).thenReturn(response);

			// Then
			org.junit.jupiter.api.Assertions.assertThrows(NotFoundMemberInfoException.class, () -> {
				memberMypageService.withdrawMember(request, responseMock);
			});

		}
	}

	@Test
	@DisplayName("회원 최근 주문 목록 조회 메서드 테스트")
	void getMemberRecentOrdersMethodTest() throws Exception {

		// Given
		List<ResponseMemberRecentOrderDTO> responseMemberRecentOrderDTOS = List.of(
			new ResponseMemberRecentOrderDTO(
				LocalDateTime.now(), "1", new ArrayList<>(), "ACTIVE"
			)
		);
		ResponseEntity<List<ResponseMemberRecentOrderDTO>> response = new ResponseEntity<>(
			responseMemberRecentOrderDTOS, HttpStatus.CREATED
		);

		// When
		when(memberOrderAdaptor.getMemberRecentOrders("user")).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> {
			memberMypageService.getMemberRecentOrders("user");
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("회원 최근 주문 목록 조회 메서드 EmptyResponseException 테스트")
	void getMemberRecentOrdersMethodEmptyResponseExceptionTest() throws Exception {

		// Given
		List<ResponseMemberRecentOrderDTO> responseMemberRecentOrderDTOS = List.of(
			new ResponseMemberRecentOrderDTO(
				LocalDateTime.now(), "1", new ArrayList<>(), "ACTIVE"
			)
		);
		ResponseEntity<List<ResponseMemberRecentOrderDTO>> response = new ResponseEntity<>(
			responseMemberRecentOrderDTOS, HttpStatus.BAD_REQUEST
		);

		// When
		when(memberOrderAdaptor.getMemberRecentOrders("user")).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			memberMypageService.getMemberRecentOrders("user");
		});

	}

}