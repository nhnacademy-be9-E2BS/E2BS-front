package com.nhnacademy.front.account.auth.service;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.auth.adaptor.AuthAdaptor;
import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.model.dto.response.ResponseJwtTokenDTO;
import com.nhnacademy.front.common.exception.EmptyRequestException;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthAdaptor authAdaptor;

	/**
	 * authAdaptor 를 이용하여 auth 에 응답을 요청
	 */
	public void postAuthCreateJwtToken(RequestJwtTokenDTO requestJwtTokenDTO, HttpServletResponse response) {
		if (Objects.isNull(requestJwtTokenDTO)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponseEntity<ResponseJwtTokenDTO> responseJwtTokenDTO = authAdaptor.postAuth(requestJwtTokenDTO);

			if (Objects.isNull(responseJwtTokenDTO)
				|| Objects.isNull(responseJwtTokenDTO.getBody().getAccessToken())
				|| Objects.isNull(responseJwtTokenDTO.getBody().getRefreshToken())) {

				throw new SaveJwtTokenProcessException("JWT 토큰을 생성하지 못했습니다.");

			}

			Cookie accessCookie = new Cookie("Set-Cookie", responseJwtTokenDTO.getBody().getAccessToken());
			accessCookie.setHttpOnly(true);
			accessCookie.setSecure(true);
			accessCookie.setPath("/");
			accessCookie.setMaxAge(600);

			response.addCookie(accessCookie);

		} catch (FeignException ex) {
			throw new SaveJwtTokenProcessException("JWT 토큰을 생성하지 못했습니다.");
		}

	}

}
