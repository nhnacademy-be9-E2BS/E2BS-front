package com.nhnacademy.front.pointpolicy.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.pointpolicy.adpator.PointPolicyAdaptor;
import com.nhnacademy.front.pointpolicy.exception.PointPolicyGetException;
import com.nhnacademy.front.pointpolicy.exception.PointPolicyPostException;
import com.nhnacademy.front.pointpolicy.exception.PointPolicyUpdateException;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyRegisterDTO;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyUpdateDTO;
import com.nhnacademy.front.pointpolicy.model.dto.response.ResponsePointPolicyDTO;
import com.nhnacademy.front.pointpolicy.service.PointPolicyService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointPolicyServiceImpl implements PointPolicyService {

	private final PointPolicyAdaptor pointPolicyAdaptor;

	@Override
	public void createPointPolicy(RequestPointPolicyRegisterDTO request) throws FeignException {
		ResponseEntity<Void> response = pointPolicyAdaptor.createPointPolicy(request);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointPolicyPostException("포인트 정책 등록 실패");
		}
	}

	@Override
	public List<ResponsePointPolicyDTO> getRegisterPointPolicy() throws FeignException {
		ResponseEntity<List<ResponsePointPolicyDTO>> response = pointPolicyAdaptor.getRegisterPointPolicies();
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointPolicyGetException("포인트 정책 리스트 조회 실패 : 회원가입");
		}
		return response.getBody();
	}

	@Override
	public List<ResponsePointPolicyDTO> getReviewImgPointPolicy() {
		ResponseEntity<List<ResponsePointPolicyDTO>> response = pointPolicyAdaptor.getReviewImgPointPolicies();
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointPolicyGetException("포인트 정책 리스트 조회 실패 : 이미지 리뷰");
		}
		return response.getBody();
	}

	@Override
	public List<ResponsePointPolicyDTO> getReviewPointPolicy() {
		ResponseEntity<List<ResponsePointPolicyDTO>> response = pointPolicyAdaptor.getReviewPointPolicies();
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointPolicyGetException("포인트 정책 리스트 조회 실패 : 일반 리뷰");
		}
		return response.getBody();
	}

	@Override
	public List<ResponsePointPolicyDTO> getBookPointPolicy() {
		ResponseEntity<List<ResponsePointPolicyDTO>> response = pointPolicyAdaptor.getBookPointPolicies();
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointPolicyGetException("포인트 정책 리스트 조회 실패 : 기본 적립률");
		}
		return response.getBody();
	}

	@Override
	public void activatePointPolicy(Long pointPolicyId) throws FeignException {
		ResponseEntity<Void> response = pointPolicyAdaptor.activatePointPolicy(pointPolicyId);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointPolicyUpdateException("포인트 정책 활성화 실패");
		}
	}

	@Override
	public void updatePointPolicy(Long pointPolicyId, RequestPointPolicyUpdateDTO request) {
		ResponseEntity<Void> response = pointPolicyAdaptor.updatePointPolicy(pointPolicyId, request);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointPolicyUpdateException("포인트 정책 수정 실패");
		}
	}
}
