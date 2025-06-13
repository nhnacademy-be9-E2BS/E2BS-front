package com.nhnacademy.front.account.address.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;

@FeignClient(name = "gateway-service", contextId = "member-address-adaptor")
public interface AddressAdaptor {

	@GetMapping("/api/auth/mypage/{member-id}/addresses")
	ResponseEntity<List<ResponseMemberAddressDTO>> getMemberAddresses(@PathVariable("member-id") String memberId);

	@PostMapping("/api/auth/mypage/{member-id}/addresses/form")
	ResponseEntity<Void> saveMemberAddress(@PathVariable("member-id") String memberId,
		@RequestBody RequestMemberAddressSaveDTO requestMemberAddressSaveDTO);

	@GetMapping("/api/auth/mypage/{member-id}/addresses/{address-id}")
	ResponseEntity<ResponseMemberAddressDTO> getAddress(@PathVariable("member-id") String memberId,
		@PathVariable("address-id") long addressId);

	@PutMapping("/api/auth/mypage/{member-id}/addresses/{address-id}")
	ResponseEntity<Void> updateMemberAddress(@PathVariable("member-id") String memberId,
		@PathVariable("address-id") long addressId,
		@RequestBody RequestMemberAddressSaveDTO requestMemberAddressSaveDTO);

	@DeleteMapping("/api/auth/mypage/{member-id}/addresses/{address-id}")
	ResponseEntity<Void> deleteAddress(@PathVariable("member-id") String memberId,
		@PathVariable("address-id") long addressId);

	@PostMapping("/api/auth/mypage/{member-id}/addresses/{address-id}/default")
	ResponseEntity<Void> setDefaultAddress(@PathVariable("member-id") String memberId,
		@PathVariable("address-id") long addressId);

}
