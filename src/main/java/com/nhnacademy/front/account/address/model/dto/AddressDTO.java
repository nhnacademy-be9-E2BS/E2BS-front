package com.nhnacademy.front.account.address.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

	private long addressId;
	private String addressCode;
	private String addressInfo;
	private String addressDetail;
	private String addressExtra;
	private String addressAlias;
	private boolean addressDefault;
	private LocalDateTime addressCreatedAt;

}
