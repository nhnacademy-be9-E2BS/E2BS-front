package com.nhnacademy.front.product.contributor.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.dto.request.RequestContributorDTO;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;

@FeignClient(name = "gateway-service", contextId = "contributor-service")
public interface ContributorAdaptor {
	@GetMapping("/api/admin/contributors")
	ResponseEntity<PageResponse<ResponseContributorDTO>> getContributors(Pageable pageable);

	@GetMapping("/api/admin/contributors")
	ResponseContributorDTO getContributor(@RequestParam("contributorId") long contributorId);

	@PostMapping("/api/admin/contributors")
	ResponseContributorDTO postCreateContributor(@RequestBody RequestContributorDTO contributorDTO);


	@PutMapping("/api/admin/contributors/{contributorId}")
	ResponseContributorDTO putUpdateContributor(@PathVariable long contributorId,
		@RequestBody RequestContributorDTO requestContributorDTO);

}
