package com.nhnacademy.front.product.contributor.position.adaptor;

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
import com.nhnacademy.front.product.contributor.position.dto.request.RequestPositionDTO;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;

@FeignClient(name = "position-service", url = "${product.contributor.position.url}")
public interface PositionAdaptor {
	@GetMapping
	ResponseEntity<PageResponse<ResponsePositionDTO>> getPositions(@SpringQueryMap Pageable pageable);

	@GetMapping
	ResponsePositionDTO getPosition(@RequestParam("positionId") long positionId);

	@PostMapping
	ResponsePositionDTO postCreatePosition( @RequestBody RequestPositionDTO requestPositionDTO);

	@PutMapping("/{positionId}")
	ResponsePositionDTO putUpdatePosition(@PathVariable Long positionId, @RequestBody RequestPositionDTO requestPositionDTO);
}
