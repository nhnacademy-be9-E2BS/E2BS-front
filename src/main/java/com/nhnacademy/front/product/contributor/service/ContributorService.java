package com.nhnacademy.front.product.contributor.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.adaptor.ContributorAdaptor;
import com.nhnacademy.front.product.contributor.dto.request.RequestContributorDTO;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;
import com.nhnacademy.front.product.contributor.exception.ContributorProcessException;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContributorService {
	private final ContributorAdaptor contributorAdaptor;

	/**
	 * contributor 생성
	 */
	public ResponseContributorDTO createContributor(RequestContributorDTO requestContributorDTO) {
		try {
			ResponseContributorDTO responseContributorDTO = contributorAdaptor.postCreateContributor(requestContributorDTO);
			return responseContributorDTO;
		} catch (FeignException e) {
			throw new ContributorProcessException("contributor 등록 실패");
		}
	}

	/**
	 * contributor 수정
	 */
	public void updateContributor(Long contributorId, RequestContributorDTO requestContributorDTO) {
		try {
			ResponseContributorDTO responseContributorDTO
				= contributorAdaptor.putUpdateContributor(contributorId, requestContributorDTO);
		} catch (FeignException e) {
			throw new ContributorProcessException("contributor 수정 실패");
		}
	}
	/**
	 * contributor 단건 조회
	 */
	public ResponseContributorDTO getContributor(Long contributorId) {
		try {
			return contributorAdaptor.getContributor(contributorId);
		} catch (FeignException e) {
			throw new ContributorProcessException("contributor 조회 실패");
		}
	}

	/**
	 * contributor 전체 조회
	 */
	public PageResponse<ResponseContributorDTO> getContributors(Pageable pageable) {
		try {
			ResponseEntity<PageResponse<ResponseContributorDTO>> response = contributorAdaptor.getContributors(pageable);
			return response.getBody();
		} catch (FeignException e) {
			throw new ContributorProcessException("contributor 리스트 조회 실패");
		}
	}
}
