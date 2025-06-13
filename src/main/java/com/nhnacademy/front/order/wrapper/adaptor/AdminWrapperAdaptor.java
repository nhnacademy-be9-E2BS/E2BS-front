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

@FeignClient(name = "gateway-service", contextId = "admin-wrapper-service", configuration = FeignFormDataSupportConfig.class)
public interface AdminWrapperAdaptor {

	@GetMapping("/api/auth/admin/wrappers")
	ResponseEntity<PageResponse<ResponseWrapperDTO>> getWrappers(Pageable pageable);

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/api/auth/admin/wrappers")
	ResponseEntity<Void> postCreateWrapper(@RequestPart("requestMeta") RequestRegisterWrapperMetaDTO requestMeta,
		@RequestPart("wrapperImage") MultipartFile wrapperImage);

	@PutMapping("/api/auth/admin/wrappers/{wrapperId}")
	ResponseEntity<Void> putUpdateWrapper(@PathVariable("wrapperId") Long wrapperId, @RequestBody RequestModifyWrapperDTO modifyRequest);
}
