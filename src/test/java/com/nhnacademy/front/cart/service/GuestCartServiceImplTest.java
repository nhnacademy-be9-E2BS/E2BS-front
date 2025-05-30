package com.nhnacademy.front.cart.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.cart.adaptor.GuestCartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;
import com.nhnacademy.front.cart.service.impl.GuestCartServiceImpl;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class GuestCartServiceImplTest {

	@Mock
	private GuestCartAdaptor guestCartAdaptor;

	@InjectMocks
	private GuestCartServiceImpl guestCartService;

	private static final String SESSION_ID = "test-session";


	@Test
	@DisplayName("게스트 장바구니 목록 조회 테스트")
	void getCartItemsByGuest() {
		// given
		List<ResponseCartItemsForGuestDTO> expectedList = List.of(new ResponseCartItemsForGuestDTO());
		when(guestCartAdaptor.getCartItemsByGuest(SESSION_ID)).thenReturn(ResponseEntity.ok(expectedList));

		// when
		List<ResponseCartItemsForGuestDTO> result = guestCartService.getCartItemsByGuest(SESSION_ID);

		// then
		assertEquals(expectedList, result);
	}

	@Test
	@DisplayName("게스트 장바구니 목록 조회 테스트 - 실패(FeignException 발생)")
	void getCartItemsByGuest_Fail_FeignException() {
		// given
		when(guestCartAdaptor.getCartItemsByGuest(SESSION_ID)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> guestCartService.getCartItemsByGuest(SESSION_ID))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("게스트 장바구니 항목 추가 테스트")
	void createCartItemForGuest() {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		when(guestCartAdaptor.createCartItemForGuest(requestDto)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(anyInt()));

		// when & then
		assertThatCode(() -> guestCartService.createCartItemForGuest(requestDto))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("게스트 장바구니 항목 추가 테스트 - 실패(FeignException 발생)")
	void createCartItemForGuest_Fail_FeignException() {
		// given
		RequestAddCartItemsDTO dto = new RequestAddCartItemsDTO();
		when(guestCartAdaptor.createCartItemForGuest(dto))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThatThrownBy(() -> guestCartService.createCartItemForGuest(dto))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("게스트 장바구니 항목 수량 변경 테스트")
	void updateCartItemForGuest() {
		// given
		RequestUpdateCartItemsDTO dto = new RequestUpdateCartItemsDTO();
		when(guestCartAdaptor.updateCartItemForGuest(dto))
			.thenReturn(ResponseEntity.ok(anyInt()));

		// when & then
		assertThatCode(() -> guestCartService.updateCartItemForGuest(dto))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("게스트 장바구니 항목 수량 변경 테스트 - 실패(FeignException 발생)")
	void updateCartItemForGuest_Fail_FeignException() {
		// given
		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO();
		when(guestCartAdaptor.updateCartItemForGuest(requestDto)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> guestCartService.updateCartItemForGuest(requestDto))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("게스트 장바구니 항목 삭제 테스트")
	void deleteCartItemForGuest() {
		// given
		RequestDeleteCartItemsForGuestDTO requestDto = new RequestDeleteCartItemsForGuestDTO();
		when(guestCartAdaptor.deleteCartItemForGuest(requestDto)).thenReturn(ResponseEntity.noContent().build());

		// when & then
		assertThatCode(() -> guestCartService.deleteCartItemForGuest(requestDto))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("게스트 장바구니 항목 삭제 테스트 - 실패(FeignException 발생)")
	void deleteCartItemForGuest_Fail_FeignException() {
		// given
		RequestDeleteCartItemsForGuestDTO requestDto = new RequestDeleteCartItemsForGuestDTO();
		when(guestCartAdaptor.deleteCartItemForGuest(requestDto)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> guestCartService.deleteCartItemForGuest(requestDto))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("게스트 장바구니 전체 삭제 테스트")
	void deleteCartForGuest() {
		// given
		when(guestCartAdaptor.deleteCartForGuest(SESSION_ID)).thenReturn(ResponseEntity.noContent().build());

		// when & then
		assertThatCode(() -> guestCartService.deleteCartForGuest(SESSION_ID))
			.doesNotThrowAnyException();
	}
	
	@Test
	@DisplayName("게스트 장바구니 전체 삭제 테스트 - 실패(FeignException 발생)")
	void deleteCartForGuest_Fail_FeignException() {
		// given
		when(guestCartAdaptor.deleteCartForGuest(SESSION_ID)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> guestCartService.deleteCartForGuest(SESSION_ID))
			.isInstanceOf(CartProcessException.class);
	}

}
