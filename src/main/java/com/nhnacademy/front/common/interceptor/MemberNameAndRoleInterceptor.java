package com.nhnacademy.front.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.jwt.parser.JwtHasToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberNameAndRoleInterceptor implements HandlerInterceptor {

	private final HomeService homeService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {

		if (JwtHasToken.hasToken(request)) {
			ResponseHomeMemberNameDTO responseHomeMemberNameDTO = homeService.getMemberNameFromHome(request);

			request.setAttribute("memberName", responseHomeMemberNameDTO.getMemberName());
			request.setAttribute("memberRole", responseHomeMemberNameDTO.getMemberRole());
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
