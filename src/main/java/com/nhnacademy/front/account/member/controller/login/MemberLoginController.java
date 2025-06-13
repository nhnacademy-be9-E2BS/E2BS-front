package com.nhnacademy.front.account.member.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "로그인", description = "로그인 페이지 및 기능 제공")
@Controller
@RequestMapping("/members/login")
public class MemberLoginController {
	private static final String DORMANT_CNT = "dormantCnt";

	/**
	 * 로그인 뷰
	 */
	@Operation(summary = "로그인 폼 페이지", description = "로그인 화면 제공")
	@GetMapping
	public String getLogin(HttpServletRequest request) {
		if (request.getSession().getAttribute(DORMANT_CNT) != null) {
			Integer dormantCnt = (Integer)request.getSession().getAttribute(DORMANT_CNT);
			request.getSession().setAttribute(DORMANT_CNT, dormantCnt + 1);
		}

		return "member/login/login";
	}

}
