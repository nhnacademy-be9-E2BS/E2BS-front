package com.nhnacademy.front.review.model.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateReviewDTO {

	@NotNull
	private Long productId;

	private Long customerId;

	private String memberId;

	private String reviewContent;

	@NotNull
	Integer reviewGrade;

	private MultipartFile reviewImage;

}
