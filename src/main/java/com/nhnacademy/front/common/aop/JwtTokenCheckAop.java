package com.nhnacademy.front.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.common.error.exception.LoginRedirectException;
import com.nhnacademy.front.jwt.parser.JwtExpParser;
import com.nhnacademy.front.jwt.parser.JwtMemberIdParser;
import com.nhnacademy.front.jwt.rule.JwtRule;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class JwtTokenCheckAop {

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final RedisTemplate<String, String> redisTemplate;
	private final AuthService authService;

	@Around("@annotation(com.nhnacademy.front.common.annotation.JwtTokenCheck)")
	public Object jwtTokenAndRoleAop(ProceedingJoinPoint joinPoint) throws Throwable {

		try {
			return joinPoint.proceed();
		} catch (FeignException.Unauthorized ex) {
			String refreshToken = "";
			String accessToken = extractAccessTokenFromCookie();
			if (accessToken == null) {
				throw new LoginRedirectException();
			}

			Long exp = JwtExpParser.getExp(accessToken);
			if (exp == null) {
				throw new LoginRedirectException();
			}

			String memberId = JwtMemberIdParser.getMemberId(accessToken);
			if (memberId == null) {
				throw new LoginRedirectException();
			}

			String refreshKey = JwtRule.REFRESH_PREFIX.getValue() + ":" + memberId;
			refreshToken = redisTemplate.opsForValue().get(refreshKey);
			if (refreshToken == null) {
				throw new LoginRedirectException();
			}

			Long refreshExp = JwtExpParser.getExp(refreshToken);
			if (refreshExp == null || !JwtExpParser.isTokenValid(refreshToken)) {
				throw new LoginRedirectException();
			}

			RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO(memberId);
			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);

			return joinPoint.proceed();
		}

	}

	private String extractAccessTokenFromCookie() {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
				return cookie.getValue();
			}
		}

		return null;
	}

}
