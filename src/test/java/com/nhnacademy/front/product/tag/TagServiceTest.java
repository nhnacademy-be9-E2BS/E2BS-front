package com.nhnacademy.front.product.tag;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.tag.adaptor.TagAdaptor;
import com.nhnacademy.front.product.tag.execption.TagCreateProcessException;
import com.nhnacademy.front.product.tag.execption.TagDeleteProcessException;
import com.nhnacademy.front.product.tag.execption.TagGetProcessException;
import com.nhnacademy.front.product.tag.execption.TagUpdateProcessException;
import com.nhnacademy.front.product.tag.model.dto.request.RequestTagDTO;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.impl.TagServiceImpl;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

	@InjectMocks
	private TagServiceImpl tagService;

	@Mock
	private TagAdaptor tagAdaptor;

	@Test
	@DisplayName("create tag - success")
	void createTag_successTest() {
		// given
		RequestTagDTO request = new RequestTagDTO("Tag A");
		when(tagAdaptor.postCreateTag(request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		tagService.createTag(request);

		// then
		verify(tagAdaptor, times(1)).postCreateTag(request);
	}

	@Test
	@DisplayName("create tag - fail1")
	void createTagFail1Test() {
		// Given
		RequestTagDTO request = new RequestTagDTO("Tag A");
		ResponseEntity<Void> errorResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		when(tagAdaptor.postCreateTag(any(RequestTagDTO.class))).thenReturn(errorResponse);

		// When & Then
		assertThatThrownBy(() -> tagService.createTag(request))
			.isInstanceOf(TagCreateProcessException.class)
			.hasMessageContaining("태그 등록 실패");
	}

	@Test
	@DisplayName("create tag - fail2")
	void createTagFail2Test() {
		// given
		RequestTagDTO request = new RequestTagDTO("Tag A");
		when(tagAdaptor.postCreateTag(request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> tagService.createTag(request))
			.isInstanceOf(TagCreateProcessException.class);
	}

	@Test
	@DisplayName("get tags - success")
	void getTagsTest() {
		// given
		ResponseTagDTO responseA = new ResponseTagDTO(1L, "Tag A");
		ResponseTagDTO responseB = new ResponseTagDTO(2L, "Tag B");
		List<ResponseTagDTO> tags = List.of(responseA, responseB);

		Pageable pageable = PageRequest.of(0, 10);
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

		ResponseEntity<PageResponse<ResponseTagDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(tagAdaptor.getTags(pageable)).thenReturn(response);

		// when
		PageResponse<ResponseTagDTO> result = tagService.getTags(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(tagAdaptor, times(1)).getTags(pageable);
	}

	@Test
	@DisplayName("get tags - fail1")
	void getTagsFail1Test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseTagDTO>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(tagAdaptor.getTags(pageable)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> tagService.getTags(pageable))
			.isInstanceOf(TagGetProcessException.class);
	}

	@Test
	@DisplayName("update tag - success")
	void updateTag_successTest() {
		// given
		Long tagId = 1L;
		RequestTagDTO request = new RequestTagDTO("update Tag A");
		when(tagAdaptor.putUpdateTag(tagId, request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		tagService.updateTag(tagId, request);

		// then
		verify(tagAdaptor, times(1)).putUpdateTag(tagId, request);
	}

	@Test
	@DisplayName("update tag - fail1")
	void updateTagFail1Test() {
		// Given
		RequestTagDTO request = new RequestTagDTO("update Tag A");
		ResponseEntity<Void> errorResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		when(tagAdaptor.putUpdateTag(isNull(), eq(request))).thenReturn(errorResponse);
		// When & Then
		assertThatThrownBy(() -> tagService.updateTag(null, request))
			.isInstanceOf(TagUpdateProcessException.class)
			.hasMessageContaining("태그 정보 수정 실패");
	}

	@Test
	@DisplayName("update tag - fail2")
	void updateTagFail2Test() {
		// Given
		Long tagId = 1L;
		ResponseEntity<Void> errorResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		when(tagAdaptor.putUpdateTag(eq(tagId), isNull())).thenReturn(errorResponse);

		// When & Then
		assertThatThrownBy(() -> tagService.updateTag(tagId, null))
			.isInstanceOf(TagUpdateProcessException.class)
			.hasMessageContaining("태그 정보 수정 실패");
	}

	@Test
	@DisplayName("update tag - fail3")
	void updateTagFail3Test() {
		// given
		RequestTagDTO request = new RequestTagDTO("update Tag A");
		when(tagAdaptor.putUpdateTag(1L, request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> tagService.updateTag(1L, request))
			.isInstanceOf(TagUpdateProcessException.class);
	}

	@Test
	@DisplayName("delete tag - success")
	void deleteTagSuccessTest() {
		// given
		Long tagId = 1L;
		RequestTagDTO request = new RequestTagDTO("Tag A");
		when(tagAdaptor.deleteTag(tagId, request)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		// when
		tagService.deleteTag(tagId, request);

		// then
		verify(tagAdaptor, times(1)).deleteTag(tagId, request);
	}

	@Test
	@DisplayName("delete tag - fail1")
	void deleteTagFail1Test() {
		// given
		Long tagId = 1L;
		RequestTagDTO request = new RequestTagDTO("Tag A");
		when(tagAdaptor.deleteTag(tagId, request)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

		// when & then
		assertThatThrownBy(() -> tagService.deleteTag(tagId, request))
			.isInstanceOf(TagDeleteProcessException.class)
			.hasMessageContaining("태그 삭제 실패");
	}

	@Test
	@DisplayName("delete tag - fail2")
	void deleteTagFail2Test() {
		// given
		Long tagId = 1L;
		RequestTagDTO request = new RequestTagDTO("Tag A");
		when(tagAdaptor.deleteTag(tagId, request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> tagService.deleteTag(tagId, request))
			.isInstanceOf(TagDeleteProcessException.class)
			.hasMessageContaining("태그 삭제 실패");
	}

}

