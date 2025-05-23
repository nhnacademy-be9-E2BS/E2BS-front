package com.nhnacademy.front.product.contributor;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.controller.ContributorController;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;
import com.nhnacademy.front.product.contributor.position.service.PositionService;
import com.nhnacademy.front.product.contributor.service.ContributorService;

@WebMvcTest(controllers = ContributorController.class)
@WithMockUser(roles = "ADMIN")
@ActiveProfiles("dev")
@TestPropertySource(properties = {
	"auth.jwt.create.url=http://localhost:8080/mock-auth"
})
class ContributorControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	ContributorService contributorService;

	@MockitoBean
	PositionService positionService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("컨트리뷰터 생성 성공")
	void createContributorSuccess() throws Exception {
		mockMvc.perform(post("/admin/mypage/contributors")
				.param("contributorName", "name1")
				.param("positionId", "1")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/mypage/contributors"));
	}

	@Test
	@DisplayName("컨트리뷰터 생성 실패 - 이름 누락")
	void createContributorFail() throws Exception {
		mockMvc.perform(post("/admin/mypage/contributors")
				.param("contributorName", "")
				.param("positionId", "1")
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("컨트리뷰터 수정 성공")
	void updateContributorSuccess() throws Exception {
		mockMvc.perform(put("/admin/mypage/contributors/1")
				.param("contributorName", "수정됨")
				.param("positionId", "2")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/mypage/contributors"));
	}

	@Test
	@DisplayName("컨트리뷰터 전체 조회 성공")
	void getAllContributorsSuccess() throws Exception {
		List<ResponseContributorDTO> contributorList = List.of(
			new ResponseContributorDTO("name1", 1L, "작가", 1L),
			new ResponseContributorDTO("name2", 2L, "기획자", 2L)
		);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		PageResponse<ResponseContributorDTO> pageResponse =
			new PageResponse<>(contributorList, pageableInfo, true, 2, 1, 10, 0, sortInfo, true, 2, false);

		when(contributorService.getContributors(any())).thenReturn(pageResponse);
		when(positionService.getPositionList()).thenReturn(List.of());

		mockMvc.perform(get("/admin/mypage/contributors"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("contributors"))
			.andExpect(view().name("admin/product/contributors"));
	}

}
