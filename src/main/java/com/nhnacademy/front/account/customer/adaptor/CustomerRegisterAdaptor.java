package com.nhnacademy.front.account.customer.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerRegisterDTO;

@FeignClient(name = "customer-register-adaptor", url = "${customer.register.url}")
public interface CustomerRegisterAdaptor {

	@PostMapping
	ResponseEntity<ResponseCustomerRegisterDTO> customerRegister(
		@RequestBody RequestCustomerRegisterDTO requestCustomerRegisterDTO
	);

}
