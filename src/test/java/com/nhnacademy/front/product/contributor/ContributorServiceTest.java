package com.nhnacademy.front.product.contributor;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.adaptor.ContributorAdaptor;
import com.nhnacademy.front.product.contributor.dto.request.RequestContributorDTO;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;
import com.nhnacademy.front.product.contributor.service.ContributorService;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class ContributorServiceTest {

	@InjectMocks
	ContributorService contributorService;

	@Mock
	ContributorAdaptor contributorAdaptor;

	@Test
	@DisplayName("컨트리뷰터 생성 성공")
	void createContributorSuccess() {
		RequestContributorDTO request = new RequestContributorDTO("name1", 1L);
		ResponseContributorDTO response = new ResponseContributorDTO("name1", 1L, "작가", 1L);

		when(contributorAdaptor.postCreateContributor(request)).thenReturn(response);

		assertThatCode(() -> contributorService.createContributor(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("컨트리뷰터 생성 실패 - FeignException")
	void createContributorFeignFail() {
		RequestContributorDTO request = new RequestContributorDTO("name1", 1L);

		when(contributorAdaptor.postCreateContributor(request)).thenThrow(FeignException.class);

		assertThatThrownBy(() -> contributorService.createContributor(request))
			.isInstanceOf(FeignException.class);
	}

	@Test
	@DisplayName("컨트리뷰터 수정 성공")
	void updateContributorSuccess() {
		RequestContributorDTO request = new RequestContributorDTO("name1", 2L);
		ResponseContributorDTO response = new ResponseContributorDTO("name1", 1L, "기획자", 2L);

		when(contributorAdaptor.putUpdateContributor(1L, request)).thenReturn(response);

		contributorService.updateContributor(1L, request);

		verify(contributorAdaptor, times(1)).putUpdateContributor(1L, request);
	}

	@Test
	@DisplayName("컨트리뷰터 수정 실패 - FeignException")
	void updateContributorFail() {
		RequestContributorDTO request = new RequestContributorDTO("name1", 3L);

		when(contributorAdaptor.putUpdateContributor(1L, request)).thenThrow(FeignException.class);

		assertThatThrownBy(() -> contributorService.updateContributor(1L, request))
			.isInstanceOf(FeignException.class);
	}

	@Test
	@DisplayName("컨트리뷰터 전체 조회 성공")
	void getContributorsSuccess() {
		List<ResponseContributorDTO> list = List.of(
			new ResponseContributorDTO("name1", 1L, "작가", 1L),
			new ResponseContributorDTO("name2", 2L, "기획자", 2L)
		);

		Pageable pageable = PageRequest.of(0, 10);
		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();

		PageResponse<ResponseContributorDTO> pageResponse = new PageResponse<>(
			list, pageableInfo, true, 2, 1, 10, 0, sortInfo, true, 2, false
		);

		ResponseEntity<PageResponse<ResponseContributorDTO>> response = ResponseEntity.ok(pageResponse);

		when(contributorAdaptor.getContributors(pageable)).thenReturn(response);

		PageResponse<ResponseContributorDTO> result = contributorService.getContributors(pageable);

		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent().get(0).getContributorName()).isEqualTo("name1");
		verify(contributorAdaptor, times(1)).getContributors(pageable);
	}

	@Test
	@DisplayName("position 전체 조회 실패")
	void getPositionsFail() {
		Pageable pageable = PageRequest.of(0, 10);

		when(contributorAdaptor.getContributors(pageable)).thenThrow(FeignException.class);
		assertThatThrownBy(() -> contributorService.getContributors(pageable)).isInstanceOf(
			FeignException.class);
	}

	@Test
	@DisplayName("컨트리뷰터 단건 조회 성공")
	void getContributorSuccess() {
		ResponseContributorDTO response = new ResponseContributorDTO("name", 1L, "작가", 1L);

		when(contributorAdaptor.getContributor(1L)).thenReturn(response);

		contributorService.getContributor(1L);

		verify(contributorAdaptor, times(1)).getContributor(1L);
	}

	@Test
	@DisplayName("컨트리뷰터 단건 조회 실패 - FeignException")
	void getContributorFail() {
		when(contributorAdaptor.getContributor(999L)).thenThrow(FeignException.class);

		assertThatThrownBy(() -> contributorService.getContributor(999L))
			.isInstanceOf(FeignException.class);
	}



}
