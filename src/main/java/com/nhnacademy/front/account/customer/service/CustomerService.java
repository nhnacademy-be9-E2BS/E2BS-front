package com.nhnacademy.front.account.customer.service;


import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;

public interface CustomerService {

	void createCustomer(RequestCustomerRegisterDTO request);

	boolean loginCustomer(RequestCustomerLoginDTO request);

}
