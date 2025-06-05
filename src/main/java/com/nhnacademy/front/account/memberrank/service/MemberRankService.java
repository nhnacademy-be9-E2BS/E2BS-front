package com.nhnacademy.front.account.memberrank.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.memberrank.adaptor.MemberRankAdaptor;
import com.nhnacademy.front.account.memberrank.model.dto.response.ResponseMemberRankDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberRankService {

	private final MemberRankAdaptor memberRankAdaptor;

	public List<ResponseMemberRankDTO> getMemberRank(String memberId) throws FeignException {
		ResponseEntity<List<ResponseMemberRankDTO>> response = memberRankAdaptor.getMemberRank(memberId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new NotFoundMemberRankNameException();
		}

		return response.getBody();
	}

}
