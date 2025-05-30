package com.nhnacademy.front.cart.service;

import java.util.List;

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;

public interface MemberCartService {
	List<ResponseCartItemsForMemberDTO> getCartItemsByMember(String memberId);
	Integer createCartItemForMember(RequestAddCartItemsDTO requestDto);
	Integer updateCartItemForMember(long cartItemId, RequestUpdateCartItemsDTO requestDto);
	void deleteCartItemForMember(long cartItemId);
	void deleteCartForMember(String memberId);
}
