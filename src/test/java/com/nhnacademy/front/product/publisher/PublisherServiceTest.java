package com.nhnacademy.front.product.publisher;

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

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.product.publisher.adaptor.PublisherAdaptor;
import com.nhnacademy.front.product.publisher.exception.PublisherCreateProcessException;
import com.nhnacademy.front.product.publisher.exception.PublisherGetProcessException;
import com.nhnacademy.front.product.publisher.exception.PublisherUpdateProcessException;
import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.publisher.service.PublisherService;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

	@InjectMocks
	private PublisherService publisherService;

	@Mock
	private PublisherAdaptor publisherAdaptor;

	@Test
	@DisplayName("create publisher - success")
	void create_publisher_success_test() {
		// given
		RequestPublisherDTO request = new RequestPublisherDTO("Publisher A");
		when(publisherAdaptor.postCreatePublisher(request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		publisherService.createPublisher(request);

		// then
		verify(publisherAdaptor, times(1)).postCreatePublisher(request);
	}

	@Test
	@DisplayName("create publisher - fail1")
	void create_publisher_fail1_test() {
		// when & then
		assertThatThrownBy(() -> publisherService.createPublisher(null))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("create publisher - fail2")
	void create_publisher_fail2_test() {
		// given
		RequestPublisherDTO request = new RequestPublisherDTO("Publisher A");
		when(publisherAdaptor.postCreatePublisher(request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> publisherService.createPublisher(request))
			.isInstanceOf(PublisherCreateProcessException.class);
	}

	@Test
	@DisplayName("create publisher - fail3")
	void create_publisher_fail3_test() {
		// given
		RequestPublisherDTO request = new RequestPublisherDTO("Publisher A");
		when(publisherAdaptor.postCreatePublisher(request)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> publisherService.createPublisher(request))
			.isInstanceOf(PublisherCreateProcessException.class);
	}

	@Test
	@DisplayName("get publishers - success")
	void get_publishers_test() {
		// given
		ResponsePublisherDTO responseA = new ResponsePublisherDTO(1L, "Publisher A");
		ResponsePublisherDTO responseB = new ResponsePublisherDTO(2L, "Publisher B");
		List<ResponsePublisherDTO> publishers = List.of(responseA, responseB);

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

		PageResponse<ResponsePublisherDTO> pageResponse = new PageResponse<>(
			publishers, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		ResponseEntity<PageResponse<ResponsePublisherDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(publisherAdaptor.getPublishers(pageable)).thenReturn(response);

		// when
		PageResponse<ResponsePublisherDTO> result = publisherService.getPublishers(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(publisherAdaptor, times(1)).getPublishers(pageable);
	}

	@Test
	@DisplayName("get publishers - fail1")
	void get_publishers_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponsePublisherDTO>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(publisherAdaptor.getPublishers(pageable)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> publisherService.getPublishers(pageable))
			.isInstanceOf(PublisherGetProcessException.class);
	}

	@Test
	@DisplayName("get publishers - fail2")
	void get_publishers_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		when(publisherAdaptor.getPublishers(pageable)).thenThrow(mock(FeignException.class));

		// when & then
		assertThatThrownBy(() -> publisherService.getPublishers(pageable))
			.isInstanceOf(PublisherGetProcessException.class);
	}

	@Test
	@DisplayName("update publisher - success")
	void update_publisher_success_test() {
		// given
		Long publisherId = 1L;
		RequestPublisherDTO request = new RequestPublisherDTO("update Publisher A");
		when(publisherAdaptor.putUpdatePublisher(publisherId, request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		publisherService.updatePublisher(publisherId, request);

		// then
		verify(publisherAdaptor, times(1)).putUpdatePublisher(publisherId, request);
	}

	@Test
	@DisplayName("update publisher - fail1")
	void update_publisher_fail1_test() {
		// given
		RequestPublisherDTO request = new RequestPublisherDTO("update Publisher A");

		// when & then
		assertThatThrownBy(() -> publisherService.updatePublisher(null, request))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("update publisher - fail2")
	void update_publisher_fail2_test() {
		// when & then
		assertThatThrownBy(() -> publisherService.updatePublisher(1L, null))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("update publisher - fail3")
	void update_publisher_fail3_test() {
		// given
		RequestPublisherDTO request = new RequestPublisherDTO("update Publisher A");
		when(publisherAdaptor.putUpdatePublisher(1L, request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> publisherService.updatePublisher(1L, request))
			.isInstanceOf(PublisherUpdateProcessException.class);
	}

	@Test
	@DisplayName("update publisher - fail4")
	void update_publisher_fail4_test() {
		// given
		RequestPublisherDTO request = new RequestPublisherDTO("update Publisher A");
		when(publisherAdaptor.putUpdatePublisher(1L, request)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> publisherService.updatePublisher(1L, request))
			.isInstanceOf(PublisherUpdateProcessException.class);
	}
}
