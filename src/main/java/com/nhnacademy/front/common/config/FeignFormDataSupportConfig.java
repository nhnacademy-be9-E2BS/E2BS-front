package com.nhnacademy.front.common.config;

import org.springframework.cloud.openfeign.support.JsonFormWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignFormDataSupportConfig {

	/**
	 * Feign에서 @RequestPart DTO로 받으면서 multipart를 전송하려면
	 * JsonFormWriter 사용이 가장 안전하고 직관적이다!
	 */
	@Bean
	JsonFormWriter jsonFormWriter() {
		return new JsonFormWriter();
	}

}
