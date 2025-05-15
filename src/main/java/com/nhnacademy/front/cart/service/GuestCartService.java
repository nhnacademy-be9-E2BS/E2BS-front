package com.nhnacademy.front.cart.service;

import java.util.List;

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;

public interface GuestCartService {
	List<ResponseCartItemsForGuestDTO> getCartItemsByGuest(String sessionId);
	void createCartItemForGuest(RequestAddCartItemsDTO request);
	void updateCartItemForGuest(RequestUpdateCartItemsDTO request);
	void deleteCartItemForGuest(RequestDeleteCartItemsForGuestDTO request);
	void deleteCartForGuest(String sessionId);
}
