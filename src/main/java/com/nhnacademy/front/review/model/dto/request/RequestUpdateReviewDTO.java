package com.nhnacademy.front.review.model.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateReviewDTO {

	@NotBlank
	private String reviewContent;
	private MultipartFile reviewImage;

}
