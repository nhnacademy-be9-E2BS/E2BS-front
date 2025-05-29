package com.nhnacademy.front.account.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;
import com.nhnacademy.front.account.oauth.service.OAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {

	private final OAuthService oAuthService;

	@GetMapping("/login/oauth2/code/payco")
	public String paycoLogin(@RequestParam("code") String code) {
		ResponseProviderPaycoAccessTokenDTO responseProviderPaycoAccessTokenDTO = oAuthService.getPaycoAccessToken(
			code
		);

		log.info("AccessToken:{}", responseProviderPaycoAccessTokenDTO.getAccessToken());
		log.info("AccessTokenSecret:{}", responseProviderPaycoAccessTokenDTO.getAccessTokenSecret());
		log.info("RefreshToken:{}", responseProviderPaycoAccessTokenDTO.getRefreshToken());
		log.info("ExpireIn:{}", responseProviderPaycoAccessTokenDTO.getExpireIn());
		log.info("State:{}", responseProviderPaycoAccessTokenDTO.getState());

		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO =
			oAuthService.getPaycoMemberInfo(responseProviderPaycoAccessTokenDTO.getAccessToken());

		log.info("idNo:{}", responsePaycoMemberInfoDTO.getIdNo());
		log.info("email:{}", responsePaycoMemberInfoDTO.getEmail());
		log.info("mobile:{}", responsePaycoMemberInfoDTO.getMobile());
		log.info("name:{}", responsePaycoMemberInfoDTO.getName());
		log.info("birthdayMMDD:{}", responsePaycoMemberInfoDTO.getBirthdayMMdd());

		oAuthService.paycoLogin(responsePaycoMemberInfoDTO);

		return "redirect:/";
	}

}
