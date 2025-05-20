package com.nhnacademy.front.product.tag.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.tag.model.dto.request.RequestTagDTO;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;

public interface TagService {
	void createTag(RequestTagDTO request);

	PageResponse<ResponseTagDTO> getTags(Pageable pageable);

	void updateTag(Long tagId, RequestTagDTO request);

	void deleteTag(Long tagId, RequestTagDTO request);
}
