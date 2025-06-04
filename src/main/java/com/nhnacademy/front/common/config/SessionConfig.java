package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession   // Redis를 세션 저장소로 사용
public class SessionConfig {

	// 내부에 아무 것도 정의하지 않아도 됨
	// application.yml의 spring.data.redis. 설정이 자동으로 적용

}
