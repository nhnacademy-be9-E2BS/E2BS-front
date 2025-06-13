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
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.cart.service.MemberCartService;
import com.nhnacademy.front.common.error.exception.ServerErrorException;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

	private final OAuthProviderPaycoAccessTokenAdaptor oAuthProviderPaycoAccessTokenAdaptor;
	private final OAuthPaycoMemberInfoAdaptor oAuthPaycoMemberInfoAdaptor;
	private final OAuthLoginAdaptor oAuthLoginAdaptor;
	private final OAuthRegisterAdaptor oAuthRegisterAdaptor;

	private final AuthService authService;
	private final MemberCartService memberCartService;
	private final CartService cartService;

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
			throw new PaycoProcessingException();
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

			throw new PaycoProcessingException();
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
				throw new PaycoProcessingException();
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
					throw new ServerErrorException();
				}
				// memberCartService.createCartByMember(responsePaycoMemberInfoDTO.getData().getMember().getIdNo());
			}

			RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO(
				responsePaycoMemberInfoDTO.getData().getMember().getIdNo());

			ResponseEntity<Void> latestLoginResponse = oAuthLoginAdaptor.loginOAuthLastLogin(
				requestJwtTokenDTO.getMemberId());
			if (!latestLoginResponse.getStatusCode().is2xxSuccessful()) {
				throw new ServerErrorException();
			}

			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);

			// HttpSession session = request.getSession();
			//
			// // 게스트 키가 있으면 장바구니를 꺼내서 병합 후 항목 개수 적용
			// String guestCookieValue = CookieUtil.getCookieValue("guestKey", request);
			// if (Objects.nonNull(guestCookieValue)) {
			// 	Integer mergedCount = cartService.mergeCartItemsToMemberFromGuest(new RequestMergeCartItemDTO(responsePaycoMemberInfoDTO.getData().getMember().getIdNo(), guestCookieValue));
			//
			// 	session.setAttribute("cartItemsCounts", mergedCount);
			// 	CookieUtil.clearCookie(guestCookieValue, response); // 쿠키 삭제
			// } else {
			// 	// 없으면 기존 회원 장바구니 항목 개수 적용
			// 	session.setAttribute("cartItemsCounts", cartService.getCartItemsCountsForMember(responsePaycoMemberInfoDTO.getData().getMember().getIdNo()));
			// }
			// Cookie[] cookies = request.getCookies();
			// if (Objects.nonNull(cookies)) {
			// 	for (Cookie cookie : cookies) {
			// 		if ("orderCart".equals(cookie.getName())) {
			// 			리다이렉트("/members/order");
			// 		}
			// 	}
			// }
		} catch (FeignException ex) {
			throw new ServerErrorException();
		}
	}

}
