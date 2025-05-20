package com.nhnacademy.front.product.tag.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.tag.adaptor.TagAdaptor;
import com.nhnacademy.front.product.tag.execption.TagCreateProcessException;
import com.nhnacademy.front.product.tag.execption.TagGetProcessException;
import com.nhnacademy.front.product.tag.execption.TagUpdateProcessException;
import com.nhnacademy.front.product.tag.model.dto.request.RequestTagDTO;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

	private final TagAdaptor tagAdaptor;

	/**
	 * Tag를 back - tag table에 저장
	 */
	public void createTag(RequestTagDTO request) {

		try {
			ResponseEntity<Void> response = tagAdaptor.postCreateTag(request);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new TagCreateProcessException("태그 등록 실패");
			}
		} catch (FeignException ex) {
			throw new TagCreateProcessException(ex.getMessage() + ": 태그 등록 실패");
		}
	}

	/**
	 * Tag 리스트를 back에서 조회
	 */
	public PageResponse<ResponseTagDTO> getTags(Pageable pageable) {
		try {
			ResponseEntity<PageResponse<ResponseTagDTO>> response = tagAdaptor.getTags(pageable);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new TagGetProcessException("태그 리스트 조회 실패");
			}
			return response.getBody();

		} catch (FeignException ex) {
			throw new TagGetProcessException(ex.getMessage() + "태그 리스트 조회 실패");
		}
	}

	/**
	 * Tag를 back - Tag table에서 수정
	 */
	public void updateTag(Long tagId, RequestTagDTO request) {
		try {
			ResponseEntity<Void> response = tagAdaptor.putUpdateTag(tagId, request);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new TagUpdateProcessException("태그 정보 수정 실패");
			}
		} catch (FeignException ex) {
			throw new TagUpdateProcessException(ex.getMessage() + ": 태그 정보 수정 실패");
		}
	}
}
