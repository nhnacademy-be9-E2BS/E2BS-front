package com.nhnacademy.front.review.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateReviewMetaDTO {

	@NotNull
	private long productId;

	private Long customerId;

	private String memberId;

	private String reviewContent;

	@NotNull
	int reviewGrade;

}
