package com.nhnacademy.front.account.customer.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerDTO;

@FeignClient(name = "gateway-service", contextId = "customer-login-adaptor")
public interface CustomerLoginAdaptor {

	@PostMapping("/api/customers/login")
	ResponseEntity<ResponseCustomerDTO> customerLogin(@RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO);

}
