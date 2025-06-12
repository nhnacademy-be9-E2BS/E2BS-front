package com.nhnacademy.front.Index;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.home.controller.HomeController;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;
import com.nhnacademy.front.index.service.IndexService;

@WebMvcTest(HomeController.class)
@ActiveProfiles("dev")
class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private HomeService homeService;

	@MockitoBean
	private IndexService indexService;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;


	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;


	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}


	@Test
	@DisplayName("메인 페이지 호출 - 정상 흐름")
	@WithMockUser
	void index_success() throws Exception {
		// given
		List<ResponseMainPageProductDTO> bestSellers = List.of(
			new ResponseMainPageProductDTO(1L, "Book1", "name1", "img1.jpg", 100, 100,"Publisher1", "pubA"),
			new ResponseMainPageProductDTO(2L, "Book2", "name2", "img2.jpg", 100, 100,"Publisher2", "pubB"),
			new ResponseMainPageProductDTO(3L, "Book3", "name3", "img3.jpg", 100, 100,"Publisher3", "pubC"),
			new ResponseMainPageProductDTO(4L, "Book4", "name4", "img4.jpg", 100, 100,"Publisher4", "pubD")
		);

		List<ResponseMainPageProductDTO> newItems = List.of(
			new ResponseMainPageProductDTO(5L, "NewBook1", "name5","img5.jpg", 100, 100,"Publisher5", "pubE")
		);

		given(indexService.getBestSellerProducts()).willReturn(bestSellers);
		given(indexService.getNewItemsProducts()).willReturn(newItems);

		// when & then
		mockMvc.perform(get("/").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("BestSellerList"))
			.andExpect(model().attributeExists("ItemNewAllList"))
			.andExpect(model().attributeExists("today"))
			.andExpect(view().name("home"));

	}

}
