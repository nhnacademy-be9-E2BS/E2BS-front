package com.nhnacademy.front.account.memberrank.model.dto.response;

import com.nhnacademy.front.account.memberrank.model.domain.RankName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberRankDTO {

	private RankName rankName;
	private int memberRankTierBonusRate;
	private long memberRankRequireAmount;

}
