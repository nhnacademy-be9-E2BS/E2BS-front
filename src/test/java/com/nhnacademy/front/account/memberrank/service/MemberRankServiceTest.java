package com.nhnacademy.front.account.memberrank.service;

import static org.mockito.Mockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.memberrank.adaptor.MemberRankAdaptor;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.account.memberrank.model.dto.response.ResponseMemberRankDTO;

@ExtendWith(MockitoExtension.class)
class MemberRankServiceTest {

	@InjectMocks
	private MemberRankService memberRankService;

	@Mock
	private MemberRankAdaptor memberRankAdaptor;

	@Test
	@DisplayName("회원 등급 조회 메서드 테스트")
	void getMemberRankMethodTest() throws Exception {

		// Given
		List<ResponseMemberRankDTO> responseMemberRankDTOS = List.of(
			new ResponseMemberRankDTO(RankName.NORMAL, 1, 1L),
			new ResponseMemberRankDTO(RankName.GOLD, 2, 2L)
		);
		ResponseEntity<List<ResponseMemberRankDTO>> response = new ResponseEntity<>(responseMemberRankDTOS,
			HttpStatus.CREATED);

		// When
		when(memberRankAdaptor.getMemberRank("user")).thenReturn(response);

		List<ResponseMemberRankDTO> result = memberRankService.getMemberRank("user");

		// Then
		Assertions.assertThat(result).isEqualTo(response.getBody());

	}

	@Test
	@DisplayName("회원 등급 조회 메서드 NotFoundMemberRankNameException 테스트")
	void getMemberRankMethodNotFoundMemberRankNameExceptionTest() throws Exception {

		// Given
		List<ResponseMemberRankDTO> responseMemberRankDTOS = List.of(
			new ResponseMemberRankDTO(RankName.NORMAL, 1, 1L),
			new ResponseMemberRankDTO(RankName.GOLD, 2, 2L)
		);
		ResponseEntity<List<ResponseMemberRankDTO>> response = new ResponseEntity<>(responseMemberRankDTOS,
			HttpStatus.BAD_REQUEST);

		// When
		when(memberRankAdaptor.getMemberRank("user")).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(NotFoundMemberRankNameException.class, () -> {
			memberRankService.getMemberRank("user");
		});

	}

}