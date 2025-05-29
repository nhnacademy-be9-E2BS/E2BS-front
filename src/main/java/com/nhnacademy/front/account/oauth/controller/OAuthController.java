package com.nhnacademy.front.account.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OAuthController {

	@GetMapping("/login/oauth2/code/payco")
	public String paycoLogin() {
		return "member/login/payco-login";
	}

}
