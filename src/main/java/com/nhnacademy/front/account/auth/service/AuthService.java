package com.nhnacademy.front.account.auth.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.auth.adaptor.AuthAdaptor;
import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.model.dto.response.ResponseJwtTokenDTO;
import com.nhnacademy.front.common.exception.EmptyRequestException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthAdaptor authAdaptor;

	/**
	 * authAdaptor 를 이용하여 auth 에 응답을 요청
	 */
	public void postAuthCreateJwtToken(RequestJwtTokenDTO requestJwtTokenDTO) {
		if (Objects.isNull(requestJwtTokenDTO)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponseJwtTokenDTO responseJwtTokenDTO = authAdaptor.postAuth(requestJwtTokenDTO);

			if (Objects.isNull(responseJwtTokenDTO)
				|| Objects.isNull(responseJwtTokenDTO.getAccessToken())
				|| Objects.isNull(responseJwtTokenDTO.getRefreshToken())) {

				throw new SaveJwtTokenProcessException("JWT 토큰을 생성하지 못했습니다.");

			}

		} catch (FeignException ex) {
			throw new SaveJwtTokenProcessException("JWT 토큰을 생성하지 못했습니다.");
		}

	}

}
