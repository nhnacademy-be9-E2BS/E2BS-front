package com.nhnacademy.front.product.contributor;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.position.adaptor.PositionAdaptor;
import com.nhnacademy.front.product.contributor.position.controller.PositionController;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;
import com.nhnacademy.front.product.contributor.position.service.PositionService;

@TestPropertySource(properties = {
	"auth.jwt.create.url=http://localhost:8080/mock-auth"
})
@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = PositionController.class)
@ActiveProfiles("dev")
public class PositionControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PositionService positionService;

	@MockitoBean
	private PositionAdaptor positionAdaptor;

	@Test
	@DisplayName("position 등록")
	void createPositionSuccess() throws Exception {
		// given & when & then
		mockMvc.perform(post("/admin/mypage/positions")
				.param("positionName", "작가")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/mypage/positions"));
	}

	@Test
	@DisplayName("position 등록 실패 - positionName 누락")
	void createPositionFail() throws Exception {
		mockMvc.perform(post("/admin/mypage/positions")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content("positionName=")
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}
	@Test
	@DisplayName("position 수정")
	void updatePositionSuccess() throws Exception {
		mockMvc.perform(put("/admin/mypage/positions/1")
			.param("positionName", "newPos")
			.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/mypage/positions"));
	}


	@Test
	@DisplayName("position 단건 조회 성공")
	void getPositionSuccess() throws Exception {
		ResponsePositionDTO response = new ResponsePositionDTO(1L, "작가");
		when(positionService.getPositionById(1L)).thenReturn(response);

		mockMvc.perform(get("/admin/mypage/positions/1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("position"))
			.andExpect(view().name("/admin/product/positions"));
	}


	@Test
	@DisplayName("position 전체 조회 성공")
	void getPositionsSuccess() throws Exception {
		ResponsePositionDTO response1 = new ResponsePositionDTO(1L, "position1");
		ResponsePositionDTO response2 = new ResponsePositionDTO(2L, "position2");
		List<ResponsePositionDTO> positions = List.of(response1, response2);

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

		PageResponse<ResponsePositionDTO> pageResponse = new PageResponse<>(
			positions, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		Mockito.when(positionService.getPositions(any())).thenReturn(pageResponse);

		mockMvc.perform(get("/admin/mypage/positions"))
			.andExpect(status().isOk())
			.andExpect(view().name("/admin/product/positions"))
			.andExpect(model().attributeExists("positions"));
	}

}

