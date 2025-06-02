package com.nhnacademy.front.account.customer.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerLoginDTO;

@FeignClient(name = "customer-login-adaptor", url = "${customer.login.url}")
public interface CustomerLoginAdaptor {

	@PostMapping
	ResponseEntity<ResponseCustomerLoginDTO> customerLogin(
		@RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO);

}
