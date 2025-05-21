package com.nhnacademy.front.account.member.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class MemberLoginController {

	/**
	 * 로그인 뷰
	 */
	@GetMapping
	public String getLogin() {
		return "member/login/login";
	}

}
