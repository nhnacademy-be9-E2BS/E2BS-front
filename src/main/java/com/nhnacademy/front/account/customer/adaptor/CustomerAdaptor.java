package com.nhnacademy.front.account.customer.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;

@FeignClient(name = "customer-adaptor", url = "${customer.url}")
public interface CustomerAdaptor {

	@PostMapping("/register")
	ResponseEntity<Void> registerCustomer(@RequestBody RequestCustomerRegisterDTO request);

	@PostMapping("/login")
	ResponseEntity<Boolean> loginCustomer(@RequestBody RequestCustomerLoginDTO request);

}
