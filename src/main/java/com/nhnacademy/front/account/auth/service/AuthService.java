package com.nhnacademy.front.account.auth.service;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.auth.adaptor.AuthAdaptor;
import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.model.dto.response.ResponseJwtTokenDTO;
import com.nhnacademy.front.jwt.rule.JwtRule;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthAdaptor authAdaptor;

	/**
	 * authAdaptor 를 이용하여 auth 에 응답을 요청
	 */
	public void postAuthCreateJwtToken(RequestJwtTokenDTO requestJwtTokenDTO, HttpServletResponse response,
		HttpServletRequest request) {

		try {
			ResponseEntity<ResponseJwtTokenDTO> responseJwtTokenDTO = authAdaptor.postAuth(requestJwtTokenDTO);

			if (Objects.isNull(responseJwtTokenDTO)) {
				throw new SaveJwtTokenProcessException("JWT 토큰을 생성하지 못했습니다.");
			}

			/**
			 * 기존 쿠키 있으면 삭제
			 */
			Cookie deleteCookie = new Cookie(JwtRule.JWT_ISSUE_HEADER.getValue(), null);
			deleteCookie.setPath("/");
			deleteCookie.setMaxAge(0);
			deleteCookie.setHttpOnly(true);
			deleteCookie.setSecure(true);
			response.addCookie(deleteCookie);

			/**
			 * 요청 응답의 헤더에 있는 JWT 토큰 값을 쿠키에 저장
			 */
			String token = "";
			token = responseJwtTokenDTO.getHeaders().getFirst(JwtRule.JWT_ISSUE_HEADER.getValue());

			if (Objects.nonNull(token) && token.contains("=") && token.contains(";")) {
				token = token.substring(token.indexOf("=") + 1, token.indexOf(";")).trim();
			}

			Cookie accessCookie = new Cookie(JwtRule.JWT_ISSUE_HEADER.getValue(), token);
			response.addCookie(accessCookie);

			request.setAttribute("access-refresh", token);

		} catch (FeignException ex) {
			throw new SaveJwtTokenProcessException("JWT 토큰을 생성하지 못했습니다.");
		}

	}

}
