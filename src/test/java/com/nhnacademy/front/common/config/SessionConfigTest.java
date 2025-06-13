package com.nhnacademy.front.common.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {
	SessionConfig.class,
	RedisAutoConfiguration.class,
	SessionAutoConfiguration.class
})
class SessionConfigTest {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		// then
		assertNotNull(context);
	}

}
