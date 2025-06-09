package com.nhnacademy.front.account.member.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrderProductDTO {

	private String productName;
	private int quantity;
	private long price;

}