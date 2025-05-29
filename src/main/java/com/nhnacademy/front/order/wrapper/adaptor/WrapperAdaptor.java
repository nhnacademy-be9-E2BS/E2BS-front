package com.nhnacademy.front.order.wrapper.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.front.common.config.FeignFormDataSupportConfig;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperMetaDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;

@FeignClient(name = "wrapper-service", url = "${order.wrapper.url}", configuration = FeignFormDataSupportConfig.class)
public interface WrapperAdaptor {

	@GetMapping("/wrappers")
	ResponseEntity<PageResponse<ResponseWrapperDTO>> getWrappersBySaleable(Pageable pageable);

	@GetMapping("/admin/wrappers")
	ResponseEntity<PageResponse<ResponseWrapperDTO>> getWrappers(Pageable pageable);

	@PostMapping(value = "/admin/wrappers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<Void> postCreateWrapper(@RequestPart("requestMeta") RequestRegisterWrapperMetaDTO requestMeta,
		@RequestPart("wrapperImage") MultipartFile wrapperImage);

	@PutMapping("/admin/wrappers/{wrapperId}")
	ResponseEntity<Void> putUpdateWrapper(@PathVariable("wrapperId") Long wrapperId, @RequestBody RequestModifyWrapperDTO modifyRequest);
}
