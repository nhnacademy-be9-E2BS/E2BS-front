package com.nhnacademy.front.order.wrapper.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;

@FeignClient(name = "wrapper-service", url = "${order.wrapper.url}")
public interface WrapperAdaptor {

	@GetMapping("/wrappers")
	ResponseEntity<PageResponse<ResponseWrapperDTO>> getWrappersBySaleable(@SpringQueryMap Pageable pageable);

	@GetMapping("/admin/wrappers")
	ResponseEntity<PageResponse<ResponseWrapperDTO>> getWrappers(@SpringQueryMap Pageable pageable);

	@PostMapping("/admin/wrappers")
	ResponseEntity<Void> postCreateWrapper(@RequestBody RequestRegisterWrapperDTO registerRequest);

	@PutMapping("/admin/wrappers/{wrapperId}")
	ResponseEntity<Void> putUpdateWrapper(@PathVariable("wrapperId") Long wrapperId, @RequestBody RequestModifyWrapperDTO modifyRequest);
}
