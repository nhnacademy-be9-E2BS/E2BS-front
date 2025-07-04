package com.nhnacademy.front.product.publisher;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.publisher.controller.PublisherController;
import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.publisher.service.PublisherService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = PublisherController.class)
@ActiveProfiles("dev")
class PublisherControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PublisherService publisherService;

	@MockitoBean
	private CartInterceptor cartInterceptor;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@BeforeEach
	void setUp() throws Exception {
		when(cartInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("출판사 리스트 조회")
	void get_publishers_test() throws Exception {
		// given
		ResponsePublisherDTO responseA = new ResponsePublisherDTO(1L, "Publisher A");
		ResponsePublisherDTO responseB = new ResponsePublisherDTO(2L, "Publisher B");
		List<ResponsePublisherDTO> publishers = List.of(responseA, responseB);

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

		PageResponse<ResponsePublisherDTO> pageResponse = new PageResponse<>(
			publishers, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		Mockito.when(publisherService.getPublishers(any())).thenReturn(pageResponse);

		// when & then
		mockMvc.perform(get("/admin/settings/companies"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/publishers"))
			.andExpect(model().attributeExists("publishers"));
	}

	@Test
	@DisplayName("출판사 생성 - success")
	void create_publisher_success_test() throws Exception {
		// given & when & then
		mockMvc.perform(post("/admin/settings/companies")
				.param("publisherName", "Publisher A")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/companies"));
	}

	@Test
	@DisplayName("출판사 생성 - fail")
	void create_publisher_fail_test() throws Exception {
		// given
		String publisherName = null;

		// when & then
		mockMvc.perform(post("/admin/settings/companies")
				.param("publisherName", publisherName)
				.with(csrf()))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	@DisplayName("출판사 수정 - success")
	void update_publisher_success_test() throws Exception {
		// given
		RequestPublisherDTO dto = new RequestPublisherDTO();
		dto.setPublisherName("Publisher A");

		// when & then
		mockMvc.perform(put("/admin/settings/companies/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("출판사 수정 - fail")
	void update_publisher_fail_test() throws Exception {
		// given
		RequestPublisherDTO dto = new RequestPublisherDTO();
		dto.setPublisherName(null);

		// when & then
		mockMvc.perform(put("/admin/settings/companies/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
				.with(csrf()))
			.andExpect(status().is3xxRedirection());
	}
}
