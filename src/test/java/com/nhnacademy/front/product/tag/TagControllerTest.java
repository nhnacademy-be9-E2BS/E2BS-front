package com.nhnacademy.front.product.tag;

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
import com.nhnacademy.front.product.tag.controller.TagController;
import com.nhnacademy.front.product.tag.model.dto.request.RequestTagDTO;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.impl.TagServiceImpl;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = TagController.class)
@ActiveProfiles("dev")
class TagControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TagServiceImpl tagService;

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

	@Test
	@DisplayName("태그 리스트 조회")
	void getTagsTest() throws Exception {
		//given
		ResponseTagDTO responseA = new ResponseTagDTO(1L, "Tag A");
		ResponseTagDTO responseB = new ResponseTagDTO(2L, "Tag B");
		List<ResponseTagDTO> tags = List.of(responseA, responseB);

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

		PageResponse<ResponseTagDTO> pageResponse = new PageResponse<>(
			tags, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		Mockito.when(tagService.getTags(any())).thenReturn(pageResponse);

		//when & then
		mockMvc.perform(get("/admin/settings/tags"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/tags"))
			.andExpect(model().attributeExists("tags"));
	}

	@Test
	@DisplayName("태그 생성 - success")
	void createTagSuccessTest() throws Exception {
		// given & when & then
		mockMvc.perform(post("/admin/settings/tags")
				.param("tagName", "Tag A")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/tags"));
	}

	@Test
	@DisplayName("태그 생성 - fail")
	void createTagFailTest() throws Exception {
		// given
		String tagName = null;

		// when & then
		mockMvc.perform(post("/admin/settings/tags")
				.param("tagName", tagName)
				.with(csrf()))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	@DisplayName("태그 수정 - success")
	void updateTagSuccessTest() throws Exception {
		// given
		RequestTagDTO requestTagDTO = new RequestTagDTO();
		requestTagDTO.setTagName("Tag A");

		// when & then
		mockMvc.perform(put("/admin/settings/tags/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestTagDTO))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("태그 수정 - fail")
	void updateTagFailTest() throws Exception {
		// given
		RequestTagDTO requestTagDTO = new RequestTagDTO();
		requestTagDTO.setTagName(null); // 유효성 검사 실패를 유도

		// when & then
		mockMvc.perform(put("/admin/settings/tags/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestTagDTO))
				.with(csrf()))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	@DisplayName("태그 삭제 - success")
	void deleteTagSuccessTest() throws Exception {
		// given
		RequestTagDTO requestTagDTO = new RequestTagDTO();
		requestTagDTO.setTagName("Tag A");

		// when & then
		mockMvc.perform(delete("/admin/settings/tags/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestTagDTO))
				.with(csrf()))
			.andExpect(status().isOk());
	}

}
