package com.nhnacademy.front.account.memberrank.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRank {

	private long memberRankId;
	private RankName memberRankName;
	private int memberRankTierBonusRate;
	private long memberRankRequireAmount;

}
