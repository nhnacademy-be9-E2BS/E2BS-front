package com.nhnacademy.front.order.wrapper.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.common.config.FeignFormDataSupportConfig;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;

@FeignClient(name = "gateway-service", contextId = "user-wrapper-service", configuration = FeignFormDataSupportConfig.class)
public interface UserWrapperAdaptor {

	@GetMapping("/api/wrappers")
	ResponseEntity<PageResponse<ResponseWrapperDTO>> getWrappersBySaleable(Pageable pageable);
}
