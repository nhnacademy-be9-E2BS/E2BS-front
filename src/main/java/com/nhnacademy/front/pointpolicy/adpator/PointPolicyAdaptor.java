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

@FeignClient(name = "point-policy-service", url = "${point.admin.url}")
public interface PointPolicyAdaptor {

	@PostMapping("/register")
	ResponseEntity<Void> createPointPolicy(@RequestBody RequestPointPolicyRegisterDTO request);

	@GetMapping("/registerPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getRegisterPointPolicies();

	@GetMapping("/reviewImgPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getReviewImgPointPolicies();

	@GetMapping("/reviewPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getReviewPointPolicies();

	@GetMapping("/bookPolicy")
	ResponseEntity<List<ResponsePointPolicyDTO>> getBookPointPolicies();

	@PutMapping("/{pointPolicyId}/activate")
	ResponseEntity<Void> activatePointPolicy(@PathVariable("pointPolicyId") Long pointPolicyId);

	@PutMapping("/{pointPolicyId}")
	ResponseEntity<Void> updatePointPolicy(@PathVariable("pointPolicyId") Long pointPolicyId, @RequestBody RequestPointPolicyUpdateDTO request);

}
