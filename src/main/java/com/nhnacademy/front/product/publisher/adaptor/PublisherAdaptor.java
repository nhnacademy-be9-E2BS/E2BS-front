package com.nhnacademy.front.product.publisher.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.PageResponse;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;

@FeignClient(name = "publisher-service", url = "http://localhost:10236/api/admin/publishers")
public interface PublisherAdaptor {

	@GetMapping
	PageResponse<ResponsePublisherDTO> getPublishers(@SpringQueryMap Pageable pageable);

	@PostMapping
	void postCreatePublisher(@RequestBody RequestPublisherDTO request);

	@PutMapping("/{publisherId}")
	void putUpdatePublisher(@PathVariable("publisherId") Long publisherId, @RequestBody RequestPublisherDTO request);
}
