package com.nhnacademy.front.account.address.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberAddressDTO {

	private long addressId;
	private String addressCode;
	private String addressInfo;
	private String addressDetail;
	private String addressExtra;
	private String addressAlias;
	private boolean addressDefault;
	private LocalDateTime addressCreatedAt;

}
