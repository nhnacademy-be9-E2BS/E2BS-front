package com.nhnacademy.front.review.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReviewInfoDTO {
	private double totalGradeAvg;
	private int totalCount;
	private List<Integer> starCounts;
}
