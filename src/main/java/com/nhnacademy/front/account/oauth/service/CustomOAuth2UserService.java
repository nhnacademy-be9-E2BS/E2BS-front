package com.nhnacademy.front.account.oauth.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.oauth.adaptor.OAuthLoginAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthRegisterAdaptor;
import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthLoginDTO;
import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthRegisterDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseCheckOAuthIdDTO;
import com.nhnacademy.front.common.exception.ServerErrorException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private static final String SYSTEM_ERROR_MESSAGE = "시스템에서 에러가 발생했습니다.";

	private final OAuthLoginAdaptor oAuthLoginAdaptor;
	private final OAuthRegisterAdaptor oAuthRegisterAdaptor;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

		String memberId = oAuth2User.getAttribute("idNo");
		log.info("idNo:{}", memberId);
		String email = oAuth2User.getAttribute("email");
		log.info("email:{}", email);
		String mobile = oAuth2User.getAttribute("mobile");
		log.info("mobile:{}", mobile);
		String name = oAuth2User.getAttribute("name");
		log.info("name:{}", name);
		String birthdayMMdd = oAuth2User.getAttribute("birthdayMMdd");
		log.info("birthdayMMdd:{}", birthdayMMdd);

		try {
			ResponseEntity<ResponseCheckOAuthIdDTO> response = oAuthLoginAdaptor.checkOAuthLoginId(memberId);
			if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
				throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
			}

			if (!response.getBody().isExistsOAuthId()) {
				RequestOAuthRegisterDTO requestOAuthRegisterDTO = RequestOAuthRegisterDTO.builder()
					.memberId(memberId)
					.email(email)
					.mobile(mobile)
					.name(name)
					.birthdayMMdd(birthdayMMdd)
					.build();

				ResponseEntity<Void> registerResponse = oAuthRegisterAdaptor.registerOAuth(requestOAuthRegisterDTO);
				if (!registerResponse.getStatusCode().is2xxSuccessful()) {
					throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
				}
			}
			ResponseEntity<Void> loginResponse = oAuthLoginAdaptor.loginOAuth(
				new RequestOAuthLoginDTO(memberId)
			);
			if (!loginResponse.getStatusCode().is2xxSuccessful()) {
				throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
			}

			return new DefaultOAuth2User(
				List.of(new SimpleGrantedAuthority("ROLE_MEMBER")),
				oAuth2User.getAttributes(),
				"idNo"
			);

		} catch (FeignException ex) {
			throw new ServerErrorException(SYSTEM_ERROR_MESSAGE);
		}
	}

}
