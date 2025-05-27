package com.nhnacademy.front.product.tag.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.tag.model.dto.request.RequestTagDTO;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;

@FeignClient(name = "tag-service", url = "${product.tag.url}")
public interface TagAdaptor {
	@GetMapping
	ResponseEntity<PageResponse<ResponseTagDTO>> getTags(Pageable pageable);

	@PostMapping
	ResponseEntity<Void> postCreateTag(@RequestBody RequestTagDTO request);

	@PutMapping("/{tagId}")
	ResponseEntity<Void> putUpdateTag(@PathVariable Long tagId, @RequestBody RequestTagDTO request);

	@DeleteMapping("/{tagId}")
	ResponseEntity<Void> deleteTag(@PathVariable Long tagId, RequestTagDTO request);
}
