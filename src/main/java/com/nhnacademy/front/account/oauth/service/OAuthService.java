package com.nhnacademy.front.account.oauth.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.oauth.adaptor.OAuthLoginAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthPaycoMemberInfoAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthProviderPaycoAccessTokenAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthRegisterAdaptor;
import com.nhnacademy.front.account.oauth.exception.PaycoProcessingException;
import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthPaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthRegisterDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseCheckOAuthIdDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;
import com.nhnacademy.front.common.exception.ServerErrorException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {
	private final String PAYCO_PROCESSING_MESSAGE = "Payco 응답을 받지 못했습니다.";
	private final String SYSTEM_ERROR_MESSAGE = "시스템에서 에러가 발생했습니다.";

	private final OAuthProviderPaycoAccessTokenAdaptor oAuthProviderPaycoAccessTokenAdaptor;
	private final OAuthPaycoMemberInfoAdaptor oAuthPaycoMemberInfoAdaptor;
	private final OAuthLoginAdaptor oAuthLoginAdaptor;
	private final OAuthRegisterAdaptor oAuthRegisterAdaptor;

	@Value("${spring.security.oauth2.client.registration.payco.authorization-grant-type}")
	private String authorization_grant_type;

	@Value("${spring.security.oauth2.client.registration.payco.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.payco.client-secret}")
	private String clientSecret;

	public ResponseProviderPaycoAccessTokenDTO getPaycoAccessToken(String code) {
		ResponseEntity<ResponseProviderPaycoAccessTokenDTO> response = oAuthProviderPaycoAccessTokenAdaptor.getPaycoAccessToken(
			authorization_grant_type, clientId, clientSecret, code
		);
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new PaycoProcessingException(PAYCO_PROCESSING_MESSAGE);
		}

		return response.getBody();
	}

	public ResponsePaycoMemberInfoDTO getPaycoMemberInfo(String accessToken) {
		RequestOAuthPaycoMemberInfoDTO requestOAuthPaycoMemberInfoDTO = new RequestOAuthPaycoMemberInfoDTO(
			clientId, accessToken
		);

		ResponseEntity<ResponsePaycoMemberInfoDTO> response =
			oAuthPaycoMemberInfoAdaptor.getPaycoMemberInfo(requestOAuthPaycoMemberInfoDTO);
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new PaycoProcessingException(PAYCO_PROCESSING_MESSAGE);
		}

		return response.getBody();
	}

	public void paycoLogin(ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO) {
		try {
			String idNo = responsePaycoMemberInfoDTO.getIdNo();

			ResponseEntity<ResponseCheckOAuthIdDTO> responseCheckOAuthIdDTO = oAuthLoginAdaptor.checkOAuthLoginId(idNo);
			if (!responseCheckOAuthIdDTO.getStatusCode().is2xxSuccessful()
				|| Objects.isNull(responseCheckOAuthIdDTO.getBody())) {
				throw new PaycoProcessingException(PAYCO_PROCESSING_MESSAGE);
			}

			if (!responseCheckOAuthIdDTO.getBody().isExistsOAuthId()) {
				RequestOAuthRegisterDTO requestOAuthRegisterDTO = RequestOAuthRegisterDTO.builder()
					.memberId(responsePaycoMemberInfoDTO.getIdNo())
					.email(responsePaycoMemberInfoDTO.getEmail())
					.mobile(responsePaycoMemberInfoDTO.getMobile())
					.name(responsePaycoMemberInfoDTO.getName())
					.birthdayMMdd(responsePaycoMemberInfoDTO.getBirthdayMMdd())
					.build();

				ResponseEntity<Void> registerResponse = oAuthRegisterAdaptor.registerOAuth(requestOAuthRegisterDTO);
				if (!registerResponse.getStatusCode().is2xxSuccessful()) {
					throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
				}
			}

		} catch (FeignException ex) {
			throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
		}
	}

}
