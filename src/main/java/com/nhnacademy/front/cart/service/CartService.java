package com.nhnacademy.front.cart.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.cart.model.dto.MergeCartItemDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestCartCountDTO;

public interface CartService {
	Integer getCartItemsCounts(@RequestBody RequestCartCountDTO request);
	Integer mergeCartItemsToMemberFromGuest(@RequestBody List<MergeCartItemDTO> cartItemDTO);
}
