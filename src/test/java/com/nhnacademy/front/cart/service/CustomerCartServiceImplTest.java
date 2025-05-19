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

import com.nhnacademy.front.cart.adaptor.CustomerCartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForCustomerDTO;
import com.nhnacademy.front.cart.service.impl.CustomerCartServiceImpl;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class CustomerCartServiceImplTest {

	@Mock
	private CustomerCartAdaptor customerCartAdaptor;

	@InjectMocks
	private CustomerCartServiceImpl customerCartService;


	@Test
	@DisplayName("회원 장바구니 조회 테스트")
	void getCartItemsByCustomer() {
		// given
		long customerId = 1L;
		List<ResponseCartItemsForCustomerDTO> expectedList = List.of(new ResponseCartItemsForCustomerDTO());
		ResponseEntity<List<ResponseCartItemsForCustomerDTO>> responseEntity = new ResponseEntity<>(expectedList, HttpStatus.OK);

		when(customerCartAdaptor.getCartItemsByCustomer(customerId)).thenReturn(responseEntity);

		// when
		List<ResponseCartItemsForCustomerDTO> result = customerCartService.getCartItemsByCustomer(customerId);

		// then
		assertEquals(expectedList, result);
	}

	@Test
	@DisplayName("회원 장바구니 조회 테스트 - 실패(FeignException 발생)")
	void getCartItemsByCustomer_Fail_FeignException() {
		// given
		long customerId = 1L;
		when(customerCartAdaptor.getCartItemsByCustomer(customerId)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> customerCartService.getCartItemsByCustomer(customerId))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("회원 장바구니 항목 추가 테스트")
	void createCartItemForCustomer() {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

		when(customerCartAdaptor.createCartItemForCustomer(requestDto)).thenReturn(responseEntity);

		// when & then
		assertThatCode(() -> customerCartService.createCartItemForCustomer(requestDto))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원 장바구니 항목 추가 - 실패(FeignException 발생)")
	void createCartItemForCustomer_Fail_FeignException() {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		when(customerCartAdaptor.createCartItemForCustomer(requestDto)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> customerCartService.createCartItemForCustomer(requestDto))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("회원 장바구니 항목 수량 변경 테스트")
	void updateCartItemForCustomer() {
		// given
		long cartItemId = 1L;
		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO();

		when(customerCartAdaptor.updateCartItemForCustomer(eq(cartItemId), any())).thenReturn(ResponseEntity.noContent().build());

		// when then
		assertThatCode(() -> customerCartService.updateCartItemForCustomer(cartItemId, requestDto))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원 장바구니 항목 수량 변경 테스트 - 실패(FeignException 발생)")
	void updateCartItemForCustomer_Fail_FeignException() {
		// given
		long cartItemId = 1L;
		RequestUpdateCartItemsDTO dto = new RequestUpdateCartItemsDTO();

		when(customerCartAdaptor.updateCartItemForCustomer(eq(cartItemId), any())).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> customerCartService.updateCartItemForCustomer(cartItemId, dto))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("회원 장바구니 항목 삭제 테스트")
	void deleteCartItemForCustomer() {
		// given
		long cartItemId = 1L;
		when(customerCartAdaptor.deleteCartItemForCustomer(cartItemId)).thenReturn(ResponseEntity.noContent().build());

		// when & then
		assertThatCode(() -> customerCartService.deleteCartItemForCustomer(cartItemId))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원 장바구니 전체 삭제 - 실패 응답")
	void deleteCartForCustomer_Fail_FeignException() {
		// given
		long customerId = 1L;
		when(customerCartAdaptor.deleteCartForCustomer(customerId)).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

		// when & then
		assertThatThrownBy(() -> customerCartService.deleteCartForCustomer(customerId))
			.isInstanceOf(CartProcessException.class);
	}

}
