package com.nhnacademy.front.account.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth/login")
public class OAuthController {

	@GetMapping
	public String oauthLogin() {
		return "member/login/payco-login";
	}

}
