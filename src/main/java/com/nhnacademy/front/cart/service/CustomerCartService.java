package com.nhnacademy.front.cart.service;

import java.util.List;

import com.nhnacademy.front.cart.model.dto.ResponseCartItemsForCustomerDTO;

public interface CustomerCartService {
	List<ResponseCartItemsForCustomerDTO> getCartItemsByCustomer(long customerId);
}
