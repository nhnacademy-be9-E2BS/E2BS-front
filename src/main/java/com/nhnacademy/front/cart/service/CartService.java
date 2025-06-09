package com.nhnacademy.front.cart.service;

import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartOrderDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestMergeCartItemDTO;

public interface CartService {
	Integer getCartItemsCountsForMember(String memberId);
	Integer mergeCartItemsToMemberFromGuest(RequestMergeCartItemDTO request);
	Integer deleteOrderCompleteCartItems(RequestDeleteCartOrderDTO request);
}
