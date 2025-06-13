package com.nhnacademy.front.account.customer.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerDTO;

@FeignClient(name = "gateway-service", contextId = "customer-register-adaptor")
public interface CustomerRegisterAdaptor {

	@PostMapping("/api/customers/register")
	ResponseEntity<ResponseCustomerDTO> customerRegister(@RequestBody RequestCustomerRegisterDTO requestCustomerRegisterDTO);

}
