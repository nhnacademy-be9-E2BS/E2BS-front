package com.nhnacademy.front.pointpolicy.adpator;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyRegisterDTO;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyUpdateDTO;
import com.nhnacademy.front.pointpolicy.model.dto.response.ResponsePointPolicyDTO;

@FeignClient(name = "gateway-service", contextId = "point-policy-service")
public interface PointPolicyAdaptor {

	@PostMapping("/api/admin/pointPolicies/register")
	ResponseEntity<Void> createPointPolicy(@RequestBody RequestPointPolicyRegisterDTO request);

	@GetMapping("/api/admin/pointPolicies/registerPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getRegisterPointPolicies();

	@GetMapping("/api/admin/pointPolicies/reviewImgPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getReviewImgPointPolicies();

	@GetMapping("/api/admin/pointPolicies/reviewPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getReviewPointPolicies();

	@GetMapping("/api/admin/pointPolicies/bookPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getBookPointPolicies();

	@PutMapping("/api/admin/pointPolicies/{point-policy-id}/activate")
	ResponseEntity<Void> activatePointPolicy(@PathVariable("point-policy-id") Long pointPolicyId);

	@PutMapping("/api/admin/pointPolicies/{point-policy-id}")
	ResponseEntity<Void> updatePointPolicy(@PathVariable("point-policy-id") Long pointPolicyId, @RequestBody RequestPointPolicyUpdateDTO request);

}
