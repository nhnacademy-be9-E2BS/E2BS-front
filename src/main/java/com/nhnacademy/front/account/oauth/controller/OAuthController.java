package com.nhnacademy.front.account.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;
import com.nhnacademy.front.account.oauth.service.OAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {

	private final OAuthService oAuthService;

	@GetMapping("/oauth2/authorization/payco")
	public String getPaycoLogin() {
		return "redirect:" + oAuthService.getPaycoLogin();
	}

	@GetMapping("/login/oauth2/code/payco")
	public String paycoLogin(@RequestParam("code") String code,
		HttpServletRequest request,
		HttpServletResponse response) {
		log.info("paycoLogin code:{}", code);
		ResponseProviderPaycoAccessTokenDTO responseProviderPaycoAccessTokenDTO = oAuthService.getPaycoAccessToken(
			code
		);

		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO =
			oAuthService.getPaycoMemberInfo(responseProviderPaycoAccessTokenDTO.getAccessToken());

		oAuthService.paycoLogin(responsePaycoMemberInfoDTO, request, response);

		return "redirect:/";
	}

}
