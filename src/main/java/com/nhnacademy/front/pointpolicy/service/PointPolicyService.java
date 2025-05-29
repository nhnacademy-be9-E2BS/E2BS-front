package com.nhnacademy.front.pointpolicy.service;

import java.util.List;

import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyRegisterDTO;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyUpdateDTO;
import com.nhnacademy.front.pointpolicy.model.dto.response.ResponsePointPolicyDTO;

public interface PointPolicyService {

	void createPointPolicy(RequestPointPolicyRegisterDTO request);

	List<ResponsePointPolicyDTO> getRegisterPointPolicy();

	List<ResponsePointPolicyDTO> getReviewImgPointPolicy();

	List<ResponsePointPolicyDTO> getReviewPointPolicy();

	List<ResponsePointPolicyDTO> getBookPointPolicy();

	void activatePointPolicy(Long pointPolicyId);

	void updatePointPolicy(Long pointPolicyId, RequestPointPolicyUpdateDTO request);

}
