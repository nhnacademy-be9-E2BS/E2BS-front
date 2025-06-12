package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.model.dto.request.RequestDoorayAuthenticationDTO;

@FeignClient(name = "dooray-adaptor", url = "${dooray.authentication.url}")
public interface DoorayAdaptor {

	@PostMapping("/{service-id}/{bot-id}/{bot-token}")
	String sendDoorayAuthenticationNumber(@RequestBody RequestDoorayAuthenticationDTO requestDoorayAuthenticationDTO,
		@PathVariable("service-id") String serviceId, @PathVariable("bot-id") String botId,
		@PathVariable("bot-token") String botToken);

}
