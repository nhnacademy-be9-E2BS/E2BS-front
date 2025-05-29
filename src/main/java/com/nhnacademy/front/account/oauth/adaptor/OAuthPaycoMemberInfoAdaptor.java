package com.nhnacademy.front.account.oauth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthPaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;

@FeignClient(name = "oauth-payco-member-info-adaptor", url = "https://apis-payco.krp.toastoven.net/payco/friends/find_member_v2.json")
public interface OAuthPaycoMemberInfoAdaptor {

	@PostMapping
	ResponseEntity<ResponsePaycoMemberInfoDTO> getPaycoMemberInfo(
		@RequestBody RequestOAuthPaycoMemberInfoDTO requestOAuthPaycoMemberInfoDTO);

}
