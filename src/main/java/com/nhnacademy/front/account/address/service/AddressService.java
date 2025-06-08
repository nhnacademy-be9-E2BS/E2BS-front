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
import com.nhnacademy.front.common.error.exception.EmptyResponseException;

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
			throw new GetAddressFailedException();
		}

		if (Objects.isNull(response.getBody())) {
			throw new EmptyResponseException();
		}

		return response.getBody();
	}

	public void saveMemberAddress(String memberId,
		RequestMemberAddressSaveDTO requestMemberAddressSaveDTO) throws FeignException {

		ResponseEntity<Void> response = addressAdaptor.saveMemberAddress(
			memberId, requestMemberAddressSaveDTO
		);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new SaveAddressFailedException();
		}
	}

	public ResponseMemberAddressDTO getAddressByAddressId(String memberId, long addressId) {
		ResponseEntity<ResponseMemberAddressDTO> response = addressAdaptor.getAddress(memberId, addressId);

		if (Objects.isNull(response.getBody())) {
			throw new EmptyResponseException();
		}

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new EmptyResponseException();
		}

		return response.getBody();
	}

	public void updateAddress(RequestMemberAddressSaveDTO requestMemberAddressSaveDTO, String memberId,
		long addressId) {
		ResponseEntity<Void> response = addressAdaptor.updateMemberAddress(memberId, addressId,
			requestMemberAddressSaveDTO);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new UpdateAddressFailedException();
		}
	}

	public void deleteAddress(String memberId, long addressId) {
		ResponseEntity<Void> response = addressAdaptor.deleteAddress(memberId, addressId);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new DeleteAddressFailedException();
		}
	}

	public void setDefaultAddress(String memberId, long addressId) {
		ResponseEntity<Void> response = addressAdaptor.setDefaultAddress(memberId, addressId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new EmptyResponseException();
		}
	}

}
