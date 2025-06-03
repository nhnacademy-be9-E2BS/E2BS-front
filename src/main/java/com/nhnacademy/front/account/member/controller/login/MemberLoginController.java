package com.nhnacademy.front.account.member.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class MemberLoginController {

	/**
	 * 로그인 뷰
	 */
	@GetMapping
	public String getLogin(HttpServletRequest request) {
		if (request.getSession().getAttribute("dormantCnt") != null) {
			Integer dormantCnt = (Integer)request.getSession().getAttribute("dormantCnt");
			request.getSession().setAttribute("dormantCnt", dormantCnt + 1);
		}

		return "member/login/login";
	}

}
