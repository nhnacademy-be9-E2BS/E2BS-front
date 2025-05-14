package com.nhnacademy.front.order.wrapper;

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
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.wrapper.adaptor.WrapperAdaptor;
import com.nhnacademy.front.order.wrapper.exception.WrapperCreateProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperGetProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperUpdateProcessException;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;
import com.nhnacademy.front.order.wrapper.service.WrapperService;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
public class WrapperServiceTest {

	@InjectMocks
	private WrapperService wrapperService;

	@Mock
	private WrapperAdaptor wrapperAdaptor;

	@Test
	@DisplayName("create wrapper - success")
	void create_wrapper_success_test() {
		// given
		RequestRegisterWrapperDTO request = new RequestRegisterWrapperDTO(1000, "Wrapper A", "a.jpg", true);
		when(wrapperAdaptor.postCreateWrapper(request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		wrapperService.createWrapper(request);

		// then
		verify(wrapperAdaptor, times(1)).postCreateWrapper(request);
	}

	@Test
	@DisplayName("create wrapper - fail1")
	void create_wrapper_fail1_test() {
		// when & then
		assertThatThrownBy(() -> wrapperService.createWrapper(null))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("create wrapper - fail2")
	void create_wrapper_fail2_test() {
		// given
		RequestRegisterWrapperDTO request = new RequestRegisterWrapperDTO(1000, "Wrapper A", "a.jpg", true);
		when(wrapperAdaptor.postCreateWrapper(request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> wrapperService.createWrapper(request))
			.isInstanceOf(WrapperCreateProcessException.class);
	}

	@Test
	@DisplayName("create wrapper - fail3")
	void create_wrapper_fail3_test() {
		// given
		RequestRegisterWrapperDTO request = new RequestRegisterWrapperDTO(1000, "Wrapper A", "a.jpg", true);
		when(wrapperAdaptor.postCreateWrapper(request)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> wrapperService.createWrapper(request))
			.isInstanceOf(WrapperCreateProcessException.class);
	}

	@Test
	@DisplayName("get wrappers by saleable - success")
	void get_wrappers_by_saleable_success_test() {
		// given
		ResponseWrapperDTO responseA = new ResponseWrapperDTO(1L, 1000, "Wrapper A", "a.jpg", true);
		ResponseWrapperDTO responseB = new ResponseWrapperDTO(2L, 1500, "Wrapper B", "b.jpg", false);
		ResponseWrapperDTO responseC = new ResponseWrapperDTO(3L, 1200, "Wrapper C", "c.jpg", true);
		List<ResponseWrapperDTO> wrappers = List.of(responseA, responseC);

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

		PageResponse<ResponseWrapperDTO> pageResponse = new PageResponse<>(
			wrappers, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		ResponseEntity<PageResponse<ResponseWrapperDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(wrapperAdaptor.getWrappersBySaleable(pageable)).thenReturn(response);

		// when
		PageResponse<ResponseWrapperDTO> result = wrapperService.getWrappersBySaleable(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(wrapperAdaptor, times(1)).getWrappersBySaleable(pageable);
	}

	@Test
	@DisplayName("get wrappers by saleable - fail1")
	void get_wrappers_by_saleable_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseWrapperDTO>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(wrapperAdaptor.getWrappersBySaleable(pageable)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> wrapperService.getWrappersBySaleable(pageable))
			.isInstanceOf(WrapperGetProcessException.class);
	}

	@Test
	@DisplayName("get wrappers by saleable - fail2")
	void get_wrappers_by_saleable_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		when(wrapperAdaptor.getWrappersBySaleable(pageable)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> wrapperService.getWrappersBySaleable(pageable))
			.isInstanceOf(WrapperGetProcessException.class);
	}

	@Test
	@DisplayName("get all wrappers - success")
	void get_all_wrappers_success_test() {
		// given
		ResponseWrapperDTO responseA = new ResponseWrapperDTO(1L, 1000, "Wrapper A", "a.jpg", true);
		ResponseWrapperDTO responseB = new ResponseWrapperDTO(2L, 1500, "Wrapper B", "b.jpg", false);
		ResponseWrapperDTO responseC = new ResponseWrapperDTO(3L, 1200, "Wrapper C", "c.jpg", true);
		List<ResponseWrapperDTO> wrappers = List.of(responseA, responseB, responseC);

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

		PageResponse<ResponseWrapperDTO> pageResponse = new PageResponse<>(
			wrappers, pageableInfo, true, 3, 1, 10, 0,
			sortInfo, true, 3, false
		);

		ResponseEntity<PageResponse<ResponseWrapperDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(wrapperAdaptor.getWrappers(pageable)).thenReturn(response);

		// when
		PageResponse<ResponseWrapperDTO> result = wrapperService.getWrappers(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(wrapperAdaptor, times(1)).getWrappers(pageable);
	}

	@Test
	@DisplayName("get all wrappers - fail1")
	void get_all_wrappers_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseWrapperDTO>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(wrapperAdaptor.getWrappers(pageable)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> wrapperService.getWrappers(pageable))
			.isInstanceOf(WrapperGetProcessException.class);
	}

	@Test
	@DisplayName("get all wrappers - fail2")
	void get_all_wrappers_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		when(wrapperAdaptor.getWrappers(pageable)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> wrapperService.getWrappers(pageable))
			.isInstanceOf(WrapperGetProcessException.class);
	}

	@Test
	@DisplayName("update wrapper - success")
	void update_wrapper_success_test() {
		// given
		Long wrapperId = 1L;
		RequestModifyWrapperDTO request = new RequestModifyWrapperDTO(false);
		when(wrapperAdaptor.putUpdateWrapper(wrapperId, request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		wrapperService.updateWrapper(wrapperId, request);

		// then
		verify(wrapperAdaptor, times(1)).putUpdateWrapper(wrapperId, request);
	}

	@Test
	@DisplayName("update wrapper - fail1")
	void update_wrapper_fail1_test() {
		// given
		RequestModifyWrapperDTO request = new RequestModifyWrapperDTO(false);

		// when & then
		assertThatThrownBy(() -> wrapperService.updateWrapper(null, request))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("update wrapper - fail2")
	void update_wrapper_fail2_test() {
		// when & then
		assertThatThrownBy(() -> wrapperService.updateWrapper(1L, null))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("update wrapper - fail3")
	void update_wrapper_fail3_test() {
		// given
		Long wrapperId = 1L;
		RequestModifyWrapperDTO request = new RequestModifyWrapperDTO(false);
		when(wrapperAdaptor.putUpdateWrapper(wrapperId, request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> wrapperService.updateWrapper(wrapperId, request))
			.isInstanceOf(WrapperUpdateProcessException.class);
	}

	@Test
	@DisplayName("update wrapper - fail4")
	void update_wrapper_fail4_test() {
		// given
		Long wrapperId = 1L;
		RequestModifyWrapperDTO request = new RequestModifyWrapperDTO(false);
		when(wrapperAdaptor.putUpdateWrapper(wrapperId, request)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> wrapperService.updateWrapper(wrapperId, request))
			.isInstanceOf(WrapperUpdateProcessException.class);
	}
}
