package com.nhnacademy.front.account.address.service;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.account.address.adaptor.AddressAdaptor;
import com.nhnacademy.front.account.address.exception.DeleteAddressFailedException;
import com.nhnacademy.front.account.address.exception.GetAddressFailedException;
import com.nhnacademy.front.account.address.exception.SaveAddressFailedException;
import com.nhnacademy.front.account.address.exception.UpdateAddressFailedException;
import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;

@ExtendWith({MockitoExtension.class})
class AddressServiceTest {

	@InjectMocks
	private AddressService addressService;

	@Mock
	private AddressAdaptor addressAdaptor;

	@Test
	@DisplayName("회원 배송지 조회 메서드 테스트")
	void getMemberAddressesMethodTest() throws Exception {

		// Given
		List<ResponseMemberAddressDTO> memberAddresses = List.of(new ResponseMemberAddressDTO());
		ResponseEntity<List<ResponseMemberAddressDTO>> response = new ResponseEntity<>(memberAddresses, HttpStatus.OK);

		// When
		when(addressAdaptor.getMemberAddresses("user")).thenReturn(response);

		List<ResponseMemberAddressDTO> result = addressService.getMemberAddresses("user");

		// Then
		Assertions.assertThat(result).isEqualTo(memberAddresses);

	}

	@Test
	@DisplayName("회원 배송지 조회 메서드 is2xx 에러 테스트")
	void getMemberAddressesIs2xxMethodTest() throws Exception {

		// Given
		ResponseEntity<List<ResponseMemberAddressDTO>> response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		// When
		when(addressAdaptor.getMemberAddresses("user")).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(GetAddressFailedException.class, () -> {
			addressService.getMemberAddresses("user");
		});

	}

	@Test
	@DisplayName("회원 배송지 조회 메서드 EmptyResponseException 에러 테스트")
	void getMemberAddressesMethodEmptyResponseExceptionTest() throws Exception {

		// Given
		ResponseEntity<List<ResponseMemberAddressDTO>> response = new ResponseEntity<>(null, HttpStatus.CREATED);

		// When
		when(addressAdaptor.getMemberAddresses("user")).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			addressService.getMemberAddresses("user");
		});

	}

	@Test
	@DisplayName("회원 배송지 저장 메서드 테스트")
	void saveMemberAddressMethodTest() throws Exception {

		// Given
		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO = new RequestMemberAddressSaveDTO(
			"우리집",
			"12345",
			"서울시 강남구",
			"101동 202호",
			"엘리베이터 있음",
			true,
			"홍길동",
			"01012345678"
		);
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(addressAdaptor.saveMemberAddress("user", requestMemberAddressSaveDTO)).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> {
				addressService.saveMemberAddress("user", requestMemberAddressSaveDTO);
			})
			.doesNotThrowAnyException();

	}

	@Test
	@DisplayName("회원 배송지 저장 메서드 SaveAddressFailedException 에러 테스트")
	void saveMemberAddressMethodSaveAddressFailedException() throws Exception {

		// Given
		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO = new RequestMemberAddressSaveDTO(
			"우리집",
			"12345",
			"서울시 강남구",
			"101동 202호",
			"엘리베이터 있음",
			true,
			"홍길동",
			"01012345678"
		);
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(addressAdaptor.saveMemberAddress("user", requestMemberAddressSaveDTO)).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(SaveAddressFailedException.class, () -> {
			addressService.saveMemberAddress("user", requestMemberAddressSaveDTO);
		});

	}

	@Test
	@DisplayName("회원의 특정 배송지 조회 메서드 테스트")
	void getAddressByAddressIdMethodTest() throws Exception {

		// Given
		ResponseMemberAddressDTO responseMemberAddressDTO = new ResponseMemberAddressDTO(
			1L,
			"12345",
			"서울특별시 송파구 올림픽로",
			"1000호",
			"아파트 10동",
			"집",
			true,
			LocalDateTime.now(),
			"김도윤",
			"010-1234-5678"
		);
		ResponseEntity<ResponseMemberAddressDTO> response = new ResponseEntity<>(responseMemberAddressDTO,
			HttpStatus.CREATED);

		// When
		when(addressAdaptor.getAddress("user", 1)).thenReturn(response);

		ResponseMemberAddressDTO result = addressService.getAddressByAddressId("user", 1);

		// Then
		Assertions.assertThat(result).isEqualTo(response.getBody());

	}

	@Test
	@DisplayName("회원의 특정 배송지 조회 메서드 EmptyResponseException 테스트")
	void getAddressByAddressIdMethodEmptyResponseExceptionTest() throws Exception {

		// Given
		ResponseEntity<ResponseMemberAddressDTO> response = new ResponseEntity<>(null,
			HttpStatus.CREATED);

		// When
		when(addressAdaptor.getAddress("user", 1)).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			ResponseMemberAddressDTO result = addressService.getAddressByAddressId("user", 1);
		});

	}

	@Test
	@DisplayName("회원의 특정 배송지 조회 메서드 EmptyResponseException2 테스트")
	void getAddressByAddressIdMethodEmptyResponseException2Test() throws Exception {

		// Given
		ResponseMemberAddressDTO responseMemberAddressDTO = new ResponseMemberAddressDTO(
			1L,
			"12345",
			"서울특별시 송파구 올림픽로",
			"1000호",
			"아파트 10동",
			"집",
			true,
			LocalDateTime.now(),
			"김도윤",
			"010-1234-5678"
		);
		ResponseEntity<ResponseMemberAddressDTO> response = new ResponseEntity<>(responseMemberAddressDTO,
			HttpStatus.BAD_REQUEST);

		// When
		when(addressAdaptor.getAddress("user", 1)).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			ResponseMemberAddressDTO result = addressService.getAddressByAddressId("user", 1);
		});

	}

	@Test
	@DisplayName("회원 배송지 수정 메서드 테스트")
	void updateAddressMethodTest() throws Exception {

		// Given
		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO = new RequestMemberAddressSaveDTO();
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(addressAdaptor.updateMemberAddress("user", 1, requestMemberAddressSaveDTO)).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> addressService.updateAddress(requestMemberAddressSaveDTO, "user", 1))
			.doesNotThrowAnyException();

	}

	@Test
	@DisplayName("회원 배송지 수정 메서드 UpdateAddressFailedException 테스트")
	void updateAddressMethodUpdateAddressFailedExceptionTest() throws Exception {

		// Given
		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO = new RequestMemberAddressSaveDTO();
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(addressAdaptor.updateMemberAddress("user", 1, requestMemberAddressSaveDTO)).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(UpdateAddressFailedException.class, () -> {
			addressService.updateAddress(requestMemberAddressSaveDTO, "user", 1);
		});

	}

	@Test
	@DisplayName("회원 배송지 삭제 메서드 테스트")
	void deleteAddressMethodTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(addressAdaptor.deleteAddress("user", 1)).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> addressService.deleteAddress("user", 1))
			.doesNotThrowAnyException();

	}

	@Test
	@DisplayName("회원 배송지 삭제 메서드 DeleteAddressFailedException 테스트")
	void deleteAddressMethodDeleteAddressFailedExceptionTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(addressAdaptor.deleteAddress("user", 1)).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(DeleteAddressFailedException.class, () -> {
			addressService.deleteAddress("user", 1);
		});

	}

	@Test
	@DisplayName("회원 기본 배송지 설정 메서드 테스트")
	void setDefaultAddressMethodTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(addressAdaptor.setDefaultAddress("user", 1)).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> addressService.setDefaultAddress("user", 1))
			.doesNotThrowAnyException();

	}

	@Test
	@DisplayName("회원 기본 배송지 설정 메서드 EmptyResponseException 테스트")
	void setDefaultAddressMethodEmptyResponseExceptionTest() throws Exception {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(addressAdaptor.setDefaultAddress("user", 1)).thenReturn(response);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyResponseException.class, () -> {
			addressService.setDefaultAddress("user", 1);
		});

	}

}