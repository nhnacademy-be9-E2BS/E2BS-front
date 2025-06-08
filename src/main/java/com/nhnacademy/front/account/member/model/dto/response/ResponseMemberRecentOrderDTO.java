package com.nhnacademy.front.account.member.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberRecentOrderDTO {

	private LocalDateTime orderCreatedAt;
	private String orderCode;
	private List<ResponseOrderProductDTO> products;
	private String orderStateName;

}
