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
	// https:id.payco.com/oauth2.0/authorize
	// ?response_type=code
	// &client_id=3RD4qpUi5ZoDO2GNFEnioKX
	// &scope=profile
	// &state=ywqIsOnUMFgvaSGa0WULb4LU49sy4RCZ9x4y0SiSwRM%3D
	// &redirect_uri=https://e2bs.shop/login/oauth2/code/payco
}
