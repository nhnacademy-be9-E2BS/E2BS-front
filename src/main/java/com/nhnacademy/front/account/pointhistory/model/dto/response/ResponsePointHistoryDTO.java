package com.nhnacademy.front.account.pointhistory.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePointHistoryDTO {
	private Long pointAmount;
	private String pointReason;
	private LocalDateTime pointCreatedAt;
}
