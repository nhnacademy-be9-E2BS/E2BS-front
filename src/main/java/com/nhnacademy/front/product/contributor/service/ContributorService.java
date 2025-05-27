package com.nhnacademy.front.product.contributor.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.adaptor.ContributorAdaptor;
import com.nhnacademy.front.product.contributor.dto.request.RequestContributorDTO;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;

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
	@JwtTokenCheck
	public void createContributor(RequestContributorDTO requestContributorDTO) throws FeignException {
		contributorAdaptor.postCreateContributor(requestContributorDTO);
	}

	/**
	 * contributor 이름 수정
	 */
	@JwtTokenCheck
	public void updateContributor(long contributorId, RequestContributorDTO requestContributorDTO) throws FeignException {
		ResponseContributorDTO response = contributorAdaptor.putUpdateContributor(contributorId, requestContributorDTO);
		log.info("Updated contributor: {}", response);
	}

	/**
	 * contributor 단건 조회
	 */
	@JwtTokenCheck
	public void getContributor(Long contributorId) throws FeignException {
		contributorAdaptor.getContributor(contributorId);
	}

	/**
	 * contributor 전체 조회
	 */
	@JwtTokenCheck
	public PageResponse<ResponseContributorDTO> getContributors(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseContributorDTO>> response = contributorAdaptor.getContributors(pageable);
		return response.getBody();
	}
}
