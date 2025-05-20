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

import com.nhnacademy.front.cart.adaptor.MemberCartAdaptor;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;
import com.nhnacademy.front.cart.service.impl.MemberCartServiceImpl;

@ExtendWith(MockitoExtension.class)
class MemberCartServiceImplTest {

	@Mock
	private MemberCartAdaptor memberCartAdaptor;

	@InjectMocks
	private MemberCartServiceImpl memberCartService;


	@Test
	@DisplayName("회원 장바구니 조회 테스트")
	void getCartItemsByCustomer() {
		// given
		String memberId = "id123";
		List<ResponseCartItemsForMemberDTO> expectedList = List.of(new ResponseCartItemsForMemberDTO());
		ResponseEntity<List<ResponseCartItemsForMemberDTO>> responseEntity = new ResponseEntity<>(expectedList, HttpStatus.OK);

		when(memberCartAdaptor.getCartItemsByMember(memberId)).thenReturn(responseEntity);

		// when
		List<ResponseCartItemsForMemberDTO> result = memberCartService.getCartItemsByMember(memberId);

		// then
		assertEquals(expectedList, result);
	}

	@Test
	@DisplayName("회원 장바구니 항목 추가 테스트")
	void createCartItemForCustomer() {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);

		when(memberCartAdaptor.createCartItemForMember(requestDto)).thenReturn(responseEntity);

		// when & then
		assertThatCode(() -> memberCartService.createCartItemForMember(requestDto))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원 장바구니 항목 추가 - 실패(FeignException 발생)")
	void createCartItemForCustomer_Fail_FeignException() {
		// given
		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		when(memberCartAdaptor.createCartItemForMember(requestDto)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> memberCartService.createCartItemForMember(requestDto))
			.isInstanceOf(CartProcessException.class);
	}

	@Test
	@DisplayName("회원 장바구니 항목 수량 변경 테스트")
	void updateCartItemForCustomer() {
		// given
		long cartItemId = 1L;
		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO();

		when(memberCartAdaptor.updateCartItemForMember(eq(cartItemId), any())).thenReturn(ResponseEntity.noContent().build());

		// when then
		assertThatCode(() -> memberCartService.updateCartItemForMember(cartItemId, requestDto))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원 장바구니 항목 삭제 테스트")
	void deleteCartItemForCustomer() {
		// given
		long cartItemId = 1L;
		when(memberCartAdaptor.deleteCartItemForMember(cartItemId)).thenReturn(ResponseEntity.noContent().build());

		// when & then
		assertThatCode(() -> memberCartService.deleteCartItemForMember(cartItemId))
			.doesNotThrowAnyException();
	}

}
