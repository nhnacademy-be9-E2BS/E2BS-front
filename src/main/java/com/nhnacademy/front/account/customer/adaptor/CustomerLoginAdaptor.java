package com.nhnacademy.front.account.customer.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;

@FeignClient(name = "customer-login-adaptor", url = "${customer.login.url}")
public interface CustomerLoginAdaptor {

	@PostMapping
	ResponseEntity<Long> customerLogin(@RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO);

}
