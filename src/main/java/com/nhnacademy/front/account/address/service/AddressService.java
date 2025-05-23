package com.nhnacademy.front.account.address.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.address.adaptor.AddressAdaptor;
import com.nhnacademy.front.account.address.exception.DeleteAddressFailedException;
import com.nhnacademy.front.account.address.exception.GetAddressFailedException;
import com.nhnacademy.front.account.address.exception.SaveAddressFailedException;
import com.nhnacademy.front.account.address.exception.UpdateAddressFailedException;
import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;
import com.nhnacademy.front.common.exception.EmptyResponseException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final AddressAdaptor addressAdaptor;

	public List<ResponseMemberAddressDTO> getMemberAddresses(String memberId)
		throws FeignException {

		ResponseEntity<List<ResponseMemberAddressDTO>> response = addressAdaptor.getMemberAddresses(memberId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new GetAddressFailedException("회원의 배송지 정보를 가져오지 못했습니다.");
		}

		if (Objects.isNull(response.getBody())) {
			throw new EmptyResponseException("회원의 배송지 정보를 가져오지 못했습니다.");
		}

		return response.getBody();
	}

	public void saveMemberAddress(String memberId,
		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO) throws FeignException {

		ResponseEntity<Void> response = addressAdaptor.saveMemberAddress(
			memberId, requestMemberAddressSaveDTO
		);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new SaveAddressFailedException("입력하신 배송지 정보를 저장하지 못했습니다.");
		}
	}

	public ResponseMemberAddressDTO getAddressByAddressId(String memberId, long addressId) {
		ResponseEntity<ResponseMemberAddressDTO> response = addressAdaptor.getAddress(memberId, addressId);

		if (Objects.isNull(response.getBody())) {
			throw new EmptyResponseException("배송지 정보를 가져오지 못했습니다.");
		}

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new EmptyResponseException("배송지 정보를 가져오지 못했습니다.");
		}

		return response.getBody();
	}

	public void updateAddress(RequestMemberAddressSaveDTO requestMemberAddressSaveDTO, String memberId,
		long addressId) {
		ResponseEntity<Void> response = addressAdaptor.updateMemberAddress(memberId, addressId,
			requestMemberAddressSaveDTO);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new UpdateAddressFailedException("배송지를 수정하지 못했습니다.");
		}
	}

	public void deleteAddress(String memberId, long addressId) {
		ResponseEntity<Void> response = addressAdaptor.deleteAddress(memberId, addressId);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new DeleteAddressFailedException("배송지를 삭제하지 못했습니다.");
		}
	}

}
