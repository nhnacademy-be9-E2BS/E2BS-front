package com.nhnacademy.front.account.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MemberLoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("로그인 페이지 접근 가능")
	void loginPageAccessibleTest() throws Exception {
		mockMvc.perform(get("/login"))
			.andExpect(status().isOk());
	}

}