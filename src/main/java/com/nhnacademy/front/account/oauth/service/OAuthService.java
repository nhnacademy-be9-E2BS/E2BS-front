package com.nhnacademy.front.account.oauth.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.account.oauth.adaptor.OAuthLoginAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthPaycoMemberInfoAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthProviderPaycoAccessTokenAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthRegisterAdaptor;
import com.nhnacademy.front.account.oauth.exception.PaycoProcessingException;
import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthRegisterDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseCheckOAuthIdDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;
import com.nhnacademy.front.common.exception.ServerErrorException;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
	private final String PAYCO_PROCESSING_MESSAGE = "Payco 응답을 받지 못했습니다.";
	private final String SYSTEM_ERROR_MESSAGE = "시스템에서 에러가 발생했습니다.";

	private final OAuthProviderPaycoAccessTokenAdaptor oAuthProviderPaycoAccessTokenAdaptor;
	private final OAuthPaycoMemberInfoAdaptor oAuthPaycoMemberInfoAdaptor;
	private final OAuthLoginAdaptor oAuthLoginAdaptor;
	private final OAuthRegisterAdaptor oAuthRegisterAdaptor;

	private final AuthService authService;

	@Value("${spring.security.oauth2.client.provider.payco.authorization-uri}")
	private String authorizationUri;

	@Value("${spring.security.oauth2.client.registration.payco.redirect-uri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.registration.payco.scope}")
	private String scope;

	@Value("${spring.security.oauth2.client.registration.payco.authorization-grant-type}")
	private String authorizationGrantType;

	@Value("${spring.security.oauth2.client.registration.payco.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.payco.client-secret}")
	private String clientSecret;

	public String getPaycoLogin() {
		String requestPath = authorizationUri;
		requestPath += "&response_type=code";
		requestPath += "&client_id=" + clientId;
		requestPath += "&scope=" + scope;
		requestPath += "&redirect_uri=" + redirectUri;

		return requestPath;
	}

	public ResponseProviderPaycoAccessTokenDTO getPaycoAccessToken(String code) {
		ResponseEntity<ResponseProviderPaycoAccessTokenDTO> response = oAuthProviderPaycoAccessTokenAdaptor.getPaycoAccessToken(
			authorizationGrantType, clientId, clientSecret, code
		);
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new PaycoProcessingException(PAYCO_PROCESSING_MESSAGE);
		}

		return response.getBody();
	}

	public ResponsePaycoMemberInfoDTO getPaycoMemberInfo(String accessToken) {
		ResponseEntity<ResponsePaycoMemberInfoDTO> response =
			oAuthPaycoMemberInfoAdaptor.getPaycoMemberInfo(clientId, accessToken);

		if (!response.getStatusCode().is2xxSuccessful()
			|| response.getBody() == null
			|| response.getBody().getData() == null
			|| response.getBody().getData().getMember() == null) {

			throw new PaycoProcessingException(PAYCO_PROCESSING_MESSAGE);
		}

		return response.getBody();
	}

	public void paycoLogin(ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO,
		HttpServletRequest request,
		HttpServletResponse response) {
		try {
			String idNo = responsePaycoMemberInfoDTO.getData().getMember().getIdNo();

			ResponseEntity<ResponseCheckOAuthIdDTO> responseCheckOAuthIdDTO = oAuthLoginAdaptor.checkOAuthLoginId(idNo);
			if (!responseCheckOAuthIdDTO.getStatusCode().is2xxSuccessful()
				|| Objects.isNull(responseCheckOAuthIdDTO.getBody())) {
				throw new PaycoProcessingException(PAYCO_PROCESSING_MESSAGE);
			}

			if (!responseCheckOAuthIdDTO.getBody().isExistsOAuthId()) {
				RequestOAuthRegisterDTO requestOAuthRegisterDTO = RequestOAuthRegisterDTO.builder()
					.memberId(responsePaycoMemberInfoDTO.getData().getMember().getIdNo())
					.email(responsePaycoMemberInfoDTO.getData().getMember().getEmail())
					.mobile(responsePaycoMemberInfoDTO.getData().getMember().getMobile())
					.name(responsePaycoMemberInfoDTO.getData().getMember().getName())
					.birthdayMMdd(responsePaycoMemberInfoDTO.getData().getMember().getBirthdayMMdd())
					.build();

				ResponseEntity<Void> registerResponse = oAuthRegisterAdaptor.registerOAuth(requestOAuthRegisterDTO);
				if (!registerResponse.getStatusCode().is2xxSuccessful()) {
					throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
				}
			}

			RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO(
				responsePaycoMemberInfoDTO.getData().getMember().getIdNo());
			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);

		} catch (FeignException ex) {
			throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
		}
	}

}
