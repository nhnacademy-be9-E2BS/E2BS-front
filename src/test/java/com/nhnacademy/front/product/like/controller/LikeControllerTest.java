package com.nhnacademy.front.product.like.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;
import com.nhnacademy.front.product.like.service.LikeService;

import jakarta.servlet.http.HttpServletRequest;

@WithMockUser(username = "member", roles = "MEMBER")
@ActiveProfiles("dev")
@WebMvcTest(LikeController.class)
class LikeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private LikeService likeService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	private String memberId;
	private Long productId;

	@BeforeEach
	void setUp() throws Exception {
		memberId = "testMember";
		productId = 1L;

		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("POST /products/{productId}/likes - 좋아요 추가")
	void addLike() throws Exception {
		// given
		RequestCreateLikeDTO requestCreateLikeDTO = new RequestCreateLikeDTO();
		String jsonRequest = objectMapper.writeValueAsString(requestCreateLikeDTO);

		doNothing().when(likeService).createLike(productId, requestCreateLikeDTO);

		// when & then
		mockMvc.perform(post("/products/{productId}/likes", productId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.with(csrf()))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("POST /products/{productId}/likes - 좋아요 추가 실패(파라미터 검증)")
	void addLike_Fail_Validation() throws Exception {
		// given
		productId = null;

		// when & then
		mockMvc.perform(post("/products/{productId}/likes", productId)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("DELETE /products/{productId}/likes - 좋아요 삭제")
	void deleteLike() throws Exception {
		// given
		doNothing().when(likeService).deleteLike(eq(productId), eq(memberId));

		// when & then
		mockMvc.perform(delete("/products/{productId}/likes", productId)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET /products/likes - 회원이 좋아요한 페이징 목록 조회")
	void getLikedProductsByCustomer() throws Exception {
		// given
		List<ResponseLikedProductDTO> likedProductsList = List.of(
			new ResponseLikedProductDTO(1L, "ProductA", 20000, "출판사A", "http:localhost:9000/aaa.jpg", 5, 4.5, 3,
				LocalDateTime.now()),
			new ResponseLikedProductDTO(2L, "ProductB", 20000, "출판사B", "http:localhost:9000/bbb.jpg", 5, 4.5, 3,
				LocalDateTime.now())
		);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		PageResponse<ResponseLikedProductDTO> result =
			new PageResponse<>(likedProductsList, pageableInfo, true,
				2, 1, 6, 0, sortInfo, true, 2, false);

		when(likeService.getLikeProductsByCustomer(eq(memberId), any(Pageable.class))).thenReturn(result);

		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(memberId);

			// when & then
			mockMvc.perform(get("/products/likes")
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("likeProducts"))
				.andExpect(view().name("product/products-like"));
		}
	}

}
