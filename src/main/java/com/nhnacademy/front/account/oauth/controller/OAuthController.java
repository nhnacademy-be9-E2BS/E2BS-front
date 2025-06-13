package com.nhnacademy.front.account.oauth.controller;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.account.oauth.exception.PaycoProcessingException;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;
import com.nhnacademy.front.account.oauth.service.OAuthService;
import com.nhnacademy.front.common.error.exception.ServerErrorException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "PAYCO 로그인", description = "PAYCO 로그인 화면 및 기능 제공")
@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {

	private final OAuthService oAuthService;

	@Operation(summary = "PAYCO 로그인 폼 페이지", description = "PAYCO 로그인 화면 제공")
	@GetMapping("/oauth2/authorization/payco")
	public String getPaycoLogin() {
		return "redirect:" + oAuthService.getPaycoLogin();
	}

	@Operation(summary = "PAYCO 로그인 요청", description = "PAYCO 로그인 요청 처리 기능 제공",
		responses = {
			@ApiResponse(responseCode = "302", description = "PAYCO 로그인 요청 및 성공 응답"),
			@ApiResponse(responseCode = "500", description = "PAYCO 로그인 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = PaycoProcessingException.class))),
			@ApiResponse(responseCode = "500", description = "서버 오류로 인한 에러 처리",
				content = @Content(schema = @Schema(implementation = ServerErrorException.class)))
		})
	@GetMapping("/login/oauth2/code/payco")
	public String paycoLogin(@RequestParam("code") String code,
		HttpServletRequest request,
		HttpServletResponse response) {
		ResponseProviderPaycoAccessTokenDTO responseProviderPaycoAccessTokenDTO = oAuthService.getPaycoAccessToken(
			code
		);

		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO =
			oAuthService.getPaycoMemberInfo(responseProviderPaycoAccessTokenDTO.getAccessToken());

		oAuthService.paycoLogin(responsePaycoMemberInfoDTO, request, response);

		// 주문할 장바구니 항목이 존재할 경우
		Cookie[] cookies = request.getCookies();
		if (Objects.nonNull(cookies)) {
			for (Cookie cookie : cookies) {
				if ("orderCart".equals(cookie.getName())) {
					return "redirect:/members/order";
				}
			}
		}

		return "redirect:/";
	}

}
