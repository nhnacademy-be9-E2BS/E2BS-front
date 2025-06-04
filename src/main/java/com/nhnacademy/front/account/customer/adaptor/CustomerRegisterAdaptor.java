package com.nhnacademy.front.account.customer.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;

@FeignClient(name = "customer-register-adaptor", url = "${customer.register.url}")
public interface CustomerRegisterAdaptor {

	@PostMapping
	ResponseEntity<Long> customerRegister(@RequestBody RequestCustomerRegisterDTO requestCustomerRegisterDTO);

}
