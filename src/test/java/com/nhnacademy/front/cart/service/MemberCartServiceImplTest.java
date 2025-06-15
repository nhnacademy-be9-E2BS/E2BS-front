// package com.nhnacademy.front.cart.service;
//
// import static org.assertj.core.api.AssertionsForClassTypes.*;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
//
// import com.nhnacademy.front.cart.adaptor.MemberCartAdaptor;
// import com.nhnacademy.front.cart.adaptor.MemberRegisterCartAdaptor;
// import com.nhnacademy.front.cart.exception.CartProcessException;
// import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
// import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
// import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;
// import com.nhnacademy.front.cart.service.impl.MemberCartServiceImpl;
//
// import feign.FeignException;
//
// @ExtendWith(MockitoExtension.class)
// class MemberCartServiceImplTest {
//
// 	@Mock
// 	private MemberRegisterCartAdaptor memberRegisterCartAdaptor;
//
// 	@Mock
// 	private MemberCartAdaptor memberCartAdaptor;
//
// 	@InjectMocks
// 	private MemberCartServiceImpl memberCartService;
//
// 	@Test
// 	@DisplayName("회원 장바구니 생성 테스트")
// 	void createCartForMember() {
// 		// given
// 		String memberId = "id123";
//
// 		// when
// 		when(memberRegisterCartAdaptor.createCartByMember(memberId)).thenReturn(ResponseEntity.noContent().build());
//
// 		// when & then
// 		assertThatCode(() -> memberCartService.createCartByMember(memberId))
// 			.doesNotThrowAnyException();
// 	}
//
//
// 	@Test
// 	@DisplayName("회원 장바구니 조회 테스트")
// 	void getCartItemsByMember() {
// 		// given
// 		String memberId = "id123";
// 		List<ResponseCartItemsForMemberDTO> expectedList = List.of(new ResponseCartItemsForMemberDTO());
// 		ResponseEntity<List<ResponseCartItemsForMemberDTO>> responseEntity = new ResponseEntity<>(expectedList, HttpStatus.OK);
//
// 		when(memberCartAdaptor.getCartItemsByMember(memberId)).thenReturn(responseEntity);
//
// 		// when
// 		List<ResponseCartItemsForMemberDTO> result = memberCartService.getCartItemsByMember(memberId);
//
// 		// then
// 		assertEquals(expectedList, result);
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 조회 테스트 - 실패(HTTP 응답 실패)")
// 	void getCartItemsByMember_Fail_StatusNot2xx() {
// 		// given
// 		String memberId = "id123";
// 		ResponseEntity<List<ResponseCartItemsForMemberDTO>> responseEntity =
// 			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//
// 		when(memberCartAdaptor.getCartItemsByMember(memberId)).thenReturn(responseEntity);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.getCartItemsByMember(memberId))
// 			.isInstanceOf(CartProcessException.class)
// 			.hasMessageContaining("회원 장바구니 조회 실패");
// 	}
//
//
// 	@Test
// 	@DisplayName("회원 장바구니 항목 추가 테스트")
// 	void createCartItemForCustomer() {
// 		// given
// 		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
//
// 		when(memberCartAdaptor.createCartItemForMember(requestDto)).thenReturn(ResponseEntity.ok(anyInt()));
//
// 		// when & then
// 		assertThatCode(() -> memberCartService.createCartItemForMember(requestDto))
// 			.doesNotThrowAnyException();
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 항목 추가 - 실패(Http 응답 실패)")
// 	void createCartItemForMember_Fail_StatusNot2xx() {
// 		// given
// 		RequestAddCartItemsDTO requestDto = new RequestAddCartItemsDTO();
// 		ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
// 		when(memberCartAdaptor.createCartItemForMember(requestDto)).thenReturn(responseEntity);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.createCartItemForMember(requestDto))
// 			.isInstanceOf(CartProcessException.class);
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 항목 추가 테스트 - 실패(FeignException)")
// 	void createCartItemForMember_Fail_FeignException() {
// 		// given
// 		RequestAddCartItemsDTO dto = new RequestAddCartItemsDTO();
// 		when(memberCartAdaptor.createCartItemForMember(dto)).thenThrow(FeignException.class);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.createCartItemForMember(dto))
// 			.isInstanceOf(FeignException.class);
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 항목 수량 변경 테스트")
// 	void updateCartItemForMember() {
// 		// given
// 		RequestUpdateCartItemsDTO requestDto = new RequestUpdateCartItemsDTO();
//
// 		when(memberCartAdaptor.updateCartItemForMember(any())).thenReturn(ResponseEntity.ok(1));
//
// 		// when then
// 		assertThatCode(() -> memberCartService.updateCartItemForMember(requestDto))
// 			.doesNotThrowAnyException();
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 수량 변경 테스트 - 실패(HTTP 응답 실패)")
// 	void updateCartItemForMember_Fail_StatusNot2xx() {
// 		// given
// 		RequestUpdateCartItemsDTO dto = new RequestUpdateCartItemsDTO();
// 		ResponseEntity<Integer> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
//
// 		when(memberCartAdaptor.updateCartItemForMember(dto)).thenReturn(response);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.updateCartItemForMember(dto))
// 			.isInstanceOf(CartProcessException.class)
// 			.hasMessageContaining("회원 장바구니 항목 수량 변경 실패");
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 수량 변경 테스트 - 실패(FeignException)")
// 	void updateCartItemForMember_Fail_FeignException() {
// 		// given
// 		RequestUpdateCartItemsDTO dto = new RequestUpdateCartItemsDTO();
//
// 		when(memberCartAdaptor.updateCartItemForMember(dto))
// 			.thenThrow(FeignException.class);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.updateCartItemForMember(dto))
// 			.isInstanceOf(FeignException.class);
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 항목 삭제 테스트")
// 	void deleteCartItemForMember() {
// 		// given
// 		long cartItemId = 1L;
// 		when(memberCartAdaptor.deleteCartItemForMember(cartItemId)).thenReturn(ResponseEntity.noContent().build());
//
// 		// when & then
// 		assertThatCode(() -> memberCartService.deleteCartItemForMember(cartItemId))
// 			.doesNotThrowAnyException();
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 항목 삭제 테스트 - 실패(HTTP 응답 실패)")
// 	void deleteCartItemForMember_Fail_StatusNot2xx() {
// 		// given
// 		long cartItemId = 1L;
// 		ResponseEntity<Void> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
// 		when(memberCartAdaptor.deleteCartItemForMember(cartItemId)).thenReturn(response);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.deleteCartItemForMember(cartItemId))
// 			.isInstanceOf(CartProcessException.class)
// 			.hasMessageContaining("회원 장바구니 항목 삭제 실패");
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 항목 삭제 테스트 - 실패(FeignException)")
// 	void deleteCartItemForMember_Fail_FeignException() {
// 		// given
// 		long cartItemId = 1L;
// 		when(memberCartAdaptor.deleteCartItemForMember(cartItemId)).thenThrow(FeignException.class);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.deleteCartItemForMember(cartItemId))
// 			.isInstanceOf(FeignException.class);
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 전체 삭제 테스트")
// 	void deleteCartForMember() {
// 		// given
// 		String memberId = "id123";
// 		when(memberCartAdaptor.deleteCartForMember(memberId)).thenReturn(ResponseEntity.noContent().build());
//
// 		// when & then
// 		assertThatCode(() -> memberCartService.deleteCartForMember(memberId))
// 			.doesNotThrowAnyException();
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 전체 삭제 테스트 - 실패(HTTP 응답 실패)")
// 	void deleteCartForMember_Fail_StatusNot2xx() {
// 		// given
// 		String memberId = "id123";
// 		ResponseEntity<Void> response = ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
// 		when(memberCartAdaptor.deleteCartForMember(memberId)).thenReturn(response);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.deleteCartForMember(memberId))
// 			.isInstanceOf(CartProcessException.class)
// 			.hasMessageContaining("회원 장바구니 전체 삭제 실패");
// 	}
//
// 	@Test
// 	@DisplayName("회원 장바구니 전체 삭제 테스트 - 실패(FeignException)")
// 	void deleteCartForMember_Fail_FeignExceptionThrown() {
// 		// given
// 		String memberId = "id123";
// 		when(memberCartAdaptor.deleteCartForMember(memberId)).thenThrow(FeignException.class);
//
// 		// when & then
// 		assertThatThrownBy(() -> memberCartService.deleteCartForMember(memberId))
// 			.isInstanceOf(FeignException.class);
// 	}
//
// }
