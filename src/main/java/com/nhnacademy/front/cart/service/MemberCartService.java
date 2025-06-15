package com.nhnacademy.front.cart.service;

import java.util.List;

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForMemberDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;

public interface MemberCartService {
	void createCartByMember(String memberId);
	List<ResponseCartItemsForMemberDTO> getCartItemsByMember(String memberId);
	Integer createCartItemForMember(RequestAddCartItemsDTO requestDto);
	Integer updateCartItemForMember(RequestUpdateCartItemsDTO requestDto);
	void deleteCartItemForMember(RequestDeleteCartItemsForMemberDTO request);
	void deleteCartForMember(String memberId);
}
