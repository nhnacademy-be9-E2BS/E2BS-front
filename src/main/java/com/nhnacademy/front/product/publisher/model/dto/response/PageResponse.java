package com.nhnacademy.front.product.publisher.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {

	private List<T> content;

	private PageableInfo pageable;
	private boolean last;
	private long totalElements;
	private int totalPages;
	private int size;
	private int number;
	private SortInfo sort;
	private boolean first;
	private int numberOfElements;
	private boolean empty;

	@Data
	public static class PageableInfo {
		private int pageNumber;
		private int pageSize;
		private SortInfo sort;
		private long offset;
		private boolean paged;
		private boolean unpaged;
	}

	@Data
	public static class SortInfo {
		private boolean empty;
		private boolean sorted;
		private boolean unsorted;
	}
}

