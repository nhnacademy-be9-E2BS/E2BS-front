package com.nhnacademy.front.common.page;

// 페이징 파싱 유틸리티
public class PaginationUtils {

	/**
	 * 페이징 범위(startPage, endPage) 계산
	 *
	 * @param currentPage 현재 페이지 (0-based)
	 * @param totalPages 전체 페이지 수
	 * @param windowSize 보여줄 페이지 수 (예: 5)
	 * @return int[] → [startPage, endPage]
	 */
	public static int[] getPageRange(int currentPage, int totalPages, int windowSize) {
		int startPage = Math.max(0, currentPage - windowSize / 2);
		int endPage = startPage + windowSize - 1;

		if (endPage >= totalPages) {
			endPage = totalPages - 1;
			startPage = Math.max(0, endPage - windowSize + 1);
		}

		return new int[]{startPage, endPage};
	}
}
