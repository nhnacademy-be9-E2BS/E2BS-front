package com.nhnacademy.front.account.member.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberCouponDTO {

	private String memberId;
	private int couponCnt;

}
