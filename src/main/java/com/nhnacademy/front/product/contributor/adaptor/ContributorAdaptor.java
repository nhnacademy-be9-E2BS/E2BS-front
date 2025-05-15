package com.nhnacademy.front.product.contributor.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
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

@FeignClient(name = "contributor-service", url = "${product.contributor.url}")
public interface ContributorAdaptor {
	@GetMapping
	ResponseEntity<PageResponse<ResponseContributorDTO>> getContributors(@SpringQueryMap Pageable pageable);

	@GetMapping
	ResponseContributorDTO getContributor(@RequestParam("contributorId") long contributorId);

	@PostMapping
	ResponseContributorDTO postCreateContributor(@RequestBody RequestContributorDTO contributorDTO);


	@PutMapping("/{contributorId}")
	ResponseContributorDTO putUpdateContributor(@PathVariable long contributorId,
		@RequestBody RequestContributorDTO requestContributorDTO);

}
