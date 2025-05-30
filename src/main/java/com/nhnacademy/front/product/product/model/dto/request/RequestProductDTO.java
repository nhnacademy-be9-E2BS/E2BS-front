package com.nhnacademy.front.product.product.model.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestProductDTO {
	/**
	 * 도서를 생성할 때 필요한 정보
	 */

	//상품상태
	private Long productStateId;
	//출판사
	private Long publisherId;

	//제목, 목차, 설명
	@NotNull
	private String productTitle;
	@NotNull
	private String productContent;
	@NotNull
	private String productDescription;

	//출판일시
	@NotNull
	private LocalDate productPublishedAt;
	//isbn
	@NotNull
	private String productIsbn;
	//정가
	@NotNull
	private Long productRegularPrice;
	//판매가
	@NotNull
	private Long productSalePrice;
	//포장가능여부
	@NotNull
	private boolean productPackageable;
	//상품재고
	@NotNull
	private Integer productStock;

	// 이미지
	private List<MultipartFile> productImages;
	// 태그
	private List<Long> tagIds;
	// 카테고리 Id
	@NotNull
	private List<Long> categoryIds;
	// 기여자 Id
	private List<Long> contributorIds;

}
