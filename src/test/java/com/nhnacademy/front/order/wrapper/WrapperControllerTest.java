package com.nhnacademy.front.order.wrapper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.wrapper.controller.WrapperController;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;
import com.nhnacademy.front.order.wrapper.service.WrapperService;
import com.nhnacademy.front.product.category.service.UserCategoryService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = WrapperController.class)
@ActiveProfiles("dev")
class WrapperControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private WrapperService wrapperService;

	@MockitoBean
	private UserCategoryService userCategoryService;

	@MockitoBean
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("모든 포장지 리스트 조회")
	void get_wrappers_test() throws Exception {
		// given
		ResponseWrapperDTO responseA = new ResponseWrapperDTO(1L, 1000, "Wrapper A", "a.jpg", true);
		ResponseWrapperDTO responseB = new ResponseWrapperDTO(2L, 1500, "Wrapper B", "b.jpg", false);
		ResponseWrapperDTO responseC = new ResponseWrapperDTO(3L, 1200, "Wrapper C", "c.jpg", true);
		List<ResponseWrapperDTO> wrappers = List.of(responseA, responseB, responseC);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		sortInfo.setEmpty(true);
		sortInfo.setSorted(false);
		sortInfo.setUnsorted(true);

		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		pageableInfo.setPageNumber(0);
		pageableInfo.setPageSize(10);
		pageableInfo.setSort(sortInfo);
		pageableInfo.setOffset(0);
		pageableInfo.setPaged(true);
		pageableInfo.setUnpaged(false);

		PageResponse<ResponseWrapperDTO> pageResponse = new PageResponse<>(
			wrappers, pageableInfo, true, 3, 1, 10, 0,
			sortInfo, true, 3, false
		);

		Mockito.when(wrapperService.getWrappers(any())).thenReturn(pageResponse);

		// when & then
		mockMvc.perform(get("/admin/settings/wrappers"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/wrappers"))
			.andExpect(model().attributeExists("wrappers"));
	}

	@Test
	@DisplayName("포장지 생성 - success")
	void create_wrapper_success_test() throws Exception {
		// given & when & then
		mockMvc.perform(post("/admin/settings/wrappers")
				.param("wrapperPrice", String.valueOf(1000L))
				.param("wrapperName", "Wrapper A")
				.param("wrapperImage", "a.jpg")
				.param("wrapperSaleable", String.valueOf(true))
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/wrappers"));
	}

	@Test
	@DisplayName("포장지 생성 - fail")
	void create_wrapper_fail_test() throws Exception {
		// given
		String wrapperName = null;

		// when & then
		mockMvc.perform(post("/admin/settings/wrappers")
				.param("wrapperPrice", String.valueOf(1000L))
				.param("wrapperName", wrapperName)
				.param("wrapperImage", "a.jpg")
				.param("wrapperSaleable", String.valueOf(true))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("포장지 수정 - success")
	void update_wrapper_success_test() throws Exception {
		// given
		RequestModifyWrapperDTO dto = new RequestModifyWrapperDTO();
		dto.setWrapperSaleable(true);

		// when & then
		mockMvc.perform(put("/admin/settings/wrappers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("포장지 수정 - fail")
	void update_wrapper_fail_test() throws Exception {
		// given
		RequestModifyWrapperDTO dto = new RequestModifyWrapperDTO();
		dto.setWrapperSaleable(null);

		// when & then
		mockMvc.perform(put("/admin/settings/wrappers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}
}
