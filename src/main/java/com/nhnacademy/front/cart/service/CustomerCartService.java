package com.nhnacademy.front.cart.service;

import java.util.List;

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForCustomerDTO;

public interface CustomerCartService {
	List<ResponseCartItemsForCustomerDTO> getCartItemsByCustomer(long customerId);
	void createCartItemForCustomer(RequestAddCartItemsDTO requestDto);
	void updateCartItemForCustomer(long cartItemId, RequestUpdateCartItemsDTO requestDto);
	void deleteCartItemForCustomer(long cartItemId);
	void deleteCartForCustomer(long cartItemId);
}
