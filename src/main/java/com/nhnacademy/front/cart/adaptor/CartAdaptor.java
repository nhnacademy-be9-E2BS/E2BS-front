package com.nhnacademy.front.cart.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.cart.model.dto.MergeCartItemDTO;

@FeignClient(name = "cart-adaptor", url = "${cart.url}")
public interface CartAdaptor {

	@GetMapping("/counts")
	ResponseEntity<Integer> getCartItemsCounts(@RequestParam String memberId, @RequestParam String sessionId);

	@GetMapping("/merge")
	ResponseEntity<Integer> mergeCartItemsToMemberFromGuest(@RequestBody List<MergeCartItemDTO> mergeCartItemDTOS);

}