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

@FeignClient(name = "member-address-adaptor", url = "${auth.member.mypage.url}")
public interface AddressAdaptor {

	@GetMapping("/{memberId}/addresses")
	ResponseEntity<List<ResponseMemberAddressDTO>> getMemberAddresses(@PathVariable("memberId") String memberId);

	@PostMapping("/{memberId}/addresses/save")
	ResponseEntity<Void> saveMemberAddress(@PathVariable("memberId") String memberId,
		@RequestBody RequestMemberAddressSaveDTO requestMemberAddressSaveDTO);

	@GetMapping("/{memberId}/addresses/{addressId}")
	ResponseEntity<ResponseMemberAddressDTO> getAddress(@PathVariable("memberId") String memberId,
		@PathVariable("addressId") long addressId);

	@PutMapping("/{memberId}/addresses/{addressId}")
	ResponseEntity<Void> updateMemberAddress(@PathVariable("memberId") String memberId,
		@PathVariable("addressId") long addressId,
		@RequestBody RequestMemberAddressSaveDTO requestMemberAddressSaveDTO);

	@DeleteMapping("/{memberId}/addresses/{addressId}")
	ResponseEntity<Void> deleteAddress(@PathVariable("memberId") String memberId,
		@PathVariable("addressId") long addressId);

}
