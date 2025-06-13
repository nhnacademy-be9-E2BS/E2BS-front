package com.nhnacademy.front.product.publisher.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;

@FeignClient(name = "gateway-service", contextId = "publisher-service")
public interface PublisherAdaptor {

	@GetMapping("/api/auth/admin/publishers")
	ResponseEntity<PageResponse<ResponsePublisherDTO>> getPublishers(Pageable pageable);

	@PostMapping("/api/auth/admin/publishers")
	ResponseEntity<Void> postCreatePublisher(@RequestBody RequestPublisherDTO request);

	@PutMapping("/api/auth/admin/publishers/{publisherId}")
	ResponseEntity<Void> putUpdatePublisher(@PathVariable("publisherId") Long publisherId,
		@RequestBody RequestPublisherDTO request);
}
