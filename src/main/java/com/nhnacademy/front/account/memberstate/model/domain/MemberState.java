package com.nhnacademy.front.account.memberstate.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberState {

	private long memberStateId;
	private MemberStateName memberStateName;

}
