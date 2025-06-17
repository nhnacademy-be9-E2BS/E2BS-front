package com.nhnacademy.front.common.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class CategoryInterceptorTest {

	@InjectMocks
	private CategoryInterceptor interceptor;

	@Mock
	private UserCategoryService userCategoryService;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private ValueOperations<String, Object> valueOperations;

	@Mock
	private ObjectMapper objectMapper;

	private HttpServletRequest request;
	private HttpServletResponse response;

	@BeforeEach
	void setUp() {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	@DisplayName("Redis에 카테고리 캐시가 존재하는 경우 request attribute에 설정됨")
	void preHandle_CacheExists() throws Exception {
		// given
		List<ResponseCategoryDTO> mockCategories = List.of(new ResponseCategoryDTO());
		when(redisTemplate.hasKey("Categories::header")).thenReturn(true);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get("Categories::header")).thenReturn(mockCategories);
		when(objectMapper.convertValue(any(), ArgumentMatchers.<TypeReference<List<ResponseCategoryDTO>>>any()))
			.thenReturn(mockCategories);

		// when
		boolean result = interceptor.preHandle(request, response, new Object());

		// then
		assertTrue(result);
		verify(request).setAttribute("headerCategories", mockCategories);
		verify(userCategoryService, never()).getCategoriesToDepth3();
	}

	@Test
	@DisplayName("Redis에 카테고리 캐시가 없는 경우 DB 조회 후 request attribute에 설정됨")
	void preHandle_CacheNotExists() throws Exception {
		// given
		List<ResponseCategoryDTO> mockCategories = List.of(new ResponseCategoryDTO());
		when(redisTemplate.hasKey("Categories::header")).thenReturn(false);
		when(userCategoryService.getCategoriesToDepth3()).thenReturn(mockCategories);

		// when
		boolean result = interceptor.preHandle(request, response, new Object());

		// then
		assertTrue(result);
		verify(request).setAttribute("headerCategories", mockCategories);
		verify(userCategoryService).getCategoriesToDepth3();
	}

	@Test
	@DisplayName("Redis 캐시 값이 잘못된 타입인 경우 DB에서 조회하도록 fallback")
	void preHandle_InvalidCacheType() throws Exception {
		// given
		when(redisTemplate.hasKey("Categories::header")).thenReturn(true);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get("Categories::header")).thenReturn("Invalid Data"); // 잘못된 타입
		List<ResponseCategoryDTO> fallback = List.of(new ResponseCategoryDTO());
		when(userCategoryService.getCategoriesToDepth3()).thenReturn(fallback);

		// when
		boolean result = interceptor.preHandle(request, response, new Object());

		// then
		assertTrue(result);
		verify(request).setAttribute("headerCategories", fallback);
		verify(userCategoryService).getCategoriesToDepth3();
	}

}