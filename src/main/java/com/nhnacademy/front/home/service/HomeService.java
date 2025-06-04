package com.nhnacademy.front.home.service;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.exception.EmptyResponseException;
import com.nhnacademy.front.home.adaptor.HomeAdaptor;
import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {

	private final HomeAdaptor homeAdaptor;

	public ResponseHomeMemberNameDTO getMemberNameFromHome(HttpServletRequest request) throws FeignException {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		if (Objects.isNull(memberId)) {
			return null;
		}

		ResponseEntity<ResponseHomeMemberNameDTO> response = homeAdaptor.getHomeMemberName(memberId);
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new EmptyResponseException("회원 이름을 가져오지 못했습니다.");
		}

		return response.getBody();
	}

}
