package com.nhnacademy.front.common.advice;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalHeaderCategoryAdvice {

	private final UserCategoryService userCategoryService;

	@ModelAttribute("headerCategories")
	public List<ResponseCategoryDTO> getHeaderCategories() {
		try {
			return userCategoryService.getCategoriesToDepth3();
		} catch (Exception e) {
			log.error("헤더 카테고리 조회 중 오류 발생", e);
			return Collections.emptyList(); // 빈 리스트 반환하면 페이지는 계속 뜰 수 있음
		}
	}
}
