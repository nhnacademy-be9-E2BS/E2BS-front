package com.nhnacademy.front.common.page;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class PageResponseConverter {

	public static <T> Page<T> toPage(PageResponse<T> response) {
		List<T> content = response.getContent();
		int pageNumber = response.getNumber();
		int pageSize = response.getSize();
		long totalElements = response.getTotalElements();

		return new PageImpl<>(content, PageRequest.of(pageNumber, pageSize), totalElements);
	}
}

