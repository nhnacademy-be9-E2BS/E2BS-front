package com.nhnacademy.front.review.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUpdateReviewDTO {

	private String reviewContent;
	private String reviewImageUrl;

}
