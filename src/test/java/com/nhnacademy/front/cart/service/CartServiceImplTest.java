package com.nhnacademy.front.cart.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.cart.adaptor.CartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartOrderDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestMergeCartItemDTO;
import com.nhnacademy.front.cart.service.impl.CartServiceImpl;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

	@Mock
	private CartAdaptor cartAdaptor;

	@InjectMocks
	private CartServiceImpl cartService;

	@Test
	@DisplayName("회원 장바구니 항목 개수 카운트 테스트")
	void getCartItemsCountsForMember() {
		// given
		String memberId = "member123";
		ResponseEntity<Integer> response = new ResponseEntity<>(5, HttpStatus.OK);
		Mockito.when(cartAdaptor.getCartItemsCounts(memberId)).thenReturn(response);

		// when
		Integer result = cartService.getCartItemsCountsForMember(memberId);

		// then
		assertEquals(5, result);
	}

	@Test
	@DisplayName("회원 장바구니 항목 개수 카운트 테스트 - 실패(HTTP 응답 실패)")
	void getCartItemsCountsForMember_Fail_StatusNot2xx() {
		// given
		String memberId = "member123";
		ResponseEntity<Integer> response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		Mockito.when(cartAdaptor.getCartItemsCounts(memberId)).thenReturn(response);

		// when & then
		assertThrows(CartProcessException.class, () ->
			cartService.getCartItemsCountsForMember(memberId)
		);
	}

	@Test
	@DisplayName("회원 장바구니 항목 개수 카운트 테스트 - 실패(FeignException)")
	void getCartItemsCountsForMember_Fail_FeignException() {
		// given
		String memberId = "member123";
		Mockito.when(cartAdaptor.getCartItemsCounts(memberId))
			.thenThrow(FeignException.class);

		// when & then
		assertThrows(CartProcessException.class, () ->
			cartService.getCartItemsCountsForMember(memberId)
		);
	}

	@Test
	@DisplayName("게스트 장바구니 병합 테스트")
	void mergeCartItemsToMemberFromGuest() {
		// given
		RequestMergeCartItemDTO dto = new RequestMergeCartItemDTO();
		ResponseEntity<Integer> response = new ResponseEntity<>(3, HttpStatus.OK);
		Mockito.when(cartAdaptor.mergeCartItemsToMemberFromGuest(dto)).thenReturn(response);

		// when
		Integer result = cartService.mergeCartItemsToMemberFromGuest(dto);

		// then
		assertEquals(3, result);
	}

	@Test
	@DisplayName("게스트 장바구니 병합 테스트 - 실패(HTTP 응답 실패)")
	void mergeCartItemsToMemberFromGuest_Fail_StatusNot2xx() {
		// given
		RequestMergeCartItemDTO dto = new RequestMergeCartItemDTO();
		ResponseEntity<Integer> response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		Mockito.when(cartAdaptor.mergeCartItemsToMemberFromGuest(dto)).thenReturn(response);

		// when & then
		assertThrows(CartProcessException.class, () ->
			cartService.mergeCartItemsToMemberFromGuest(dto)
		);
	}

	@Test
	@DisplayName("게스트 장바구니 병합 테스트 - 실패(FeignException)")
	void mergeCartItemsToMemberFromGuest_Fail_FeignException() {
		// given
		RequestMergeCartItemDTO dto = new RequestMergeCartItemDTO();
		Mockito.when(cartAdaptor.mergeCartItemsToMemberFromGuest(dto))
			.thenThrow(FeignException.class);

		// when & then
		assertThrows(CartProcessException.class, () ->
			cartService.mergeCartItemsToMemberFromGuest(dto)
		);
	}

	@Test
	@DisplayName("주문한 상품을 장바구니에서 삭제 테스트")
	void deleteOrderCompleteCartItems() {
		// given
		RequestDeleteCartOrderDTO dto = new RequestDeleteCartOrderDTO();
		ResponseEntity<Integer> response = new ResponseEntity<>(2, HttpStatus.OK);
		Mockito.when(cartAdaptor.deleteOrderCompleteCartItems(dto)).thenReturn(response);

		// when
		Integer result = cartService.deleteOrderCompleteCartItems(dto);

		// then
		assertEquals(2, result);
	}

	@Test
	@DisplayName("주문한 상품을 장바구니에서 삭제 테스트 - 실패(Http 응답 실패)")
	void deleteOrderCompleteCartItems_Fail_StatusNot2xx() {
		// given
		RequestDeleteCartOrderDTO dto = new RequestDeleteCartOrderDTO();
		ResponseEntity<Integer> response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(cartAdaptor.deleteOrderCompleteCartItems(dto)).thenReturn(response);

		// when & then
		assertThrows(CartProcessException.class, () ->
			cartService.deleteOrderCompleteCartItems(dto)
		);
	}

	@Test
	@DisplayName("주문한 상품을 장바구니에서 삭제 테스트 - 실패(FeignException)")
	void deleteOrderCompleteCartItems_Fail_FeignException() {
		// given
		RequestDeleteCartOrderDTO dto = new RequestDeleteCartOrderDTO();
		Mockito.when(cartAdaptor.deleteOrderCompleteCartItems(dto))
			.thenThrow(FeignException.class);

		// when & then
		assertThrows(CartProcessException.class, () ->
			cartService.deleteOrderCompleteCartItems(dto)
		);
	}

}
