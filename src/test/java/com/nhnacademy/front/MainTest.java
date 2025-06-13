package com.nhnacademy.front;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest
class MainTest {

	@Test
	void contextLoads() {
		// 애플리케이션 컨텍스트가 정상적으로 로드되면 테스트 성공
	}

}