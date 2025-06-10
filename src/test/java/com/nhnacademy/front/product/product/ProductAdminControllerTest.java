package com.nhnacademy.front.product.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;
import com.nhnacademy.front.product.contributor.service.ContributorService;
import com.nhnacademy.front.product.product.controller.ProductAdminController;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductAdminService;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.publisher.service.PublisherService;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;
import com.nhnacademy.front.product.state.model.dto.service.ProductStateService;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.TagService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = ProductAdminController.class)
@ActiveProfiles("dev")
class ProductAdminControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductAdminService productAdminService;
	@MockitoBean
	private ProductService productService;
	@MockitoBean
	private AdminCategoryService adminCategoryService;
	@MockitoBean
	private PublisherService publisherService;
	@MockitoBean
	private TagService tagService;
	@MockitoBean
	private ContributorService contributorService;
	@MockitoBean
	private ProductStateService productStateService;
	@MockitoBean
	private CategoryInterceptor categoryInterceptor;
	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;
	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("전체 도서 리스트 조회")
	void get_products_test() throws Exception {
		// given
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryADTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO responseA = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>(), 3.0, 2, false, 10);
		ResponseProductReadDTO responseB = new ResponseProductReadDTO(2L, productStateDTO, publisherDTO, "title B",
			"content B", "description B",
			LocalDate.now(), "978-89-12345-01-2", 9000, 7000, false, 500, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>(), 4.3, 7, false, 10);
		List<ResponseProductReadDTO> dtos = List.of(responseA, responseB);

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

		PageResponse<ResponseProductReadDTO> pageResponse = new PageResponse<>(
			dtos, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		Pageable pageable = PageRequest.of(0, 10);
		Mockito.when(productAdminService.getProducts(pageable)).thenReturn(pageResponse);

		// when & then
		mockMvc.perform(get("/admin/settings/books"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/books/view"))
			.andExpect(model().attributeExists("products"));
	}

	@Test
	@DisplayName("도서 단일 조회")
	void get_product_by_id_test() throws Exception {
		// given
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryADTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO response = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>(), 3.0, 2, false, 10);

		when(productService.getProduct(anyLong(), anyString())).thenReturn(response);

		ResponseContributorDTO mockContributor = new ResponseContributorDTO("name", 1L, "position", 1L);
		List<ResponseContributorDTO> contributorList = List.of(mockContributor);

		ResponsePublisherDTO mockPublisher = new ResponsePublisherDTO(1L, "publisher");
		List<ResponsePublisherDTO> publisherList = List.of(mockPublisher);

		PageResponse<ResponseContributorDTO> contributorPageResponse = new PageResponse<>();
		contributorPageResponse.setContent(contributorList);
		contributorPageResponse.setTotalElements(contributorList.size());
		contributorPageResponse.setTotalPages(1);
		contributorPageResponse.setNumber(0);
		contributorPageResponse.setSize(10);
		contributorPageResponse.setNumberOfElements(contributorList.size());
		contributorPageResponse.setFirst(true);
		contributorPageResponse.setLast(true);
		contributorPageResponse.setEmpty(false);

		PageResponse<ResponsePublisherDTO> publisherPageResponse = new PageResponse<>();
		publisherPageResponse.setContent(publisherList);
		publisherPageResponse.setTotalElements(publisherList.size());
		publisherPageResponse.setTotalPages(1);
		publisherPageResponse.setNumber(0);
		publisherPageResponse.setSize(10);
		publisherPageResponse.setNumberOfElements(publisherList.size());
		publisherPageResponse.setFirst(true);
		publisherPageResponse.setLast(true);
		publisherPageResponse.setEmpty(false);

		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		pageableInfo.setPageNumber(0);
		pageableInfo.setPageSize(10);
		pageableInfo.setOffset(0);
		pageableInfo.setPaged(true);
		pageableInfo.setUnpaged(false);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		sortInfo.setEmpty(true);
		sortInfo.setSorted(false);
		sortInfo.setUnsorted(true);

		pageableInfo.setSort(sortInfo);
		contributorPageResponse.setPageable(pageableInfo);
		contributorPageResponse.setSort(sortInfo);
		publisherPageResponse.setPageable(pageableInfo);
		publisherPageResponse.setSort(sortInfo);

		when(contributorService.getContributors(any())).thenReturn(contributorPageResponse);
		when(publisherService.getPublishers(any())).thenReturn(publisherPageResponse);

		List<ResponseCategoryDTO> mockCategories = List.of(new ResponseCategoryDTO(1L, "category", null));
		when(adminCategoryService.getCategories()).thenReturn(mockCategories);

		ResponseTagDTO tag = new ResponseTagDTO(1L, "tag");
		List<ResponseTagDTO> mockTagList = List.of(tag);

		PageResponse<ResponseTagDTO> tagPageResponse = new PageResponse<>();
		tagPageResponse.setContent(mockTagList);
		tagPageResponse.setTotalElements(1);
		tagPageResponse.setTotalPages(1);
		tagPageResponse.setSize(10);
		tagPageResponse.setNumber(0);
		tagPageResponse.setNumberOfElements(1);
		tagPageResponse.setFirst(true);
		tagPageResponse.setLast(true);
		tagPageResponse.setEmpty(false);

		tagPageResponse.setPageable(pageableInfo);
		tagPageResponse.setSort(sortInfo);

		when(tagService.getTags(Pageable.unpaged())).thenReturn(tagPageResponse);

		List<ResponseProductStateDTO> mockStates = List.of(new ResponseProductStateDTO(1L, ProductStateName.SALE.name()));
		when(productStateService.getProductStates()).thenReturn(mockStates);

		// when & then
		mockMvc.perform(get("/admin/settings/books/register/1")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/books/register"))
			.andExpect(model().attributeExists("bookId"))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attributeExists("contributors"))
			.andExpect(model().attributeExists("publishers"))
			.andExpect(model().attributeExists("categories"))
			.andExpect(model().attributeExists("tags"))
			.andExpect(model().attributeExists("states"));
	}

	@Test
	@DisplayName("도서 등록 뷰 이동")
	void get_register_view_test() throws Exception {
		// given
		ResponseContributorDTO mockContributor = new ResponseContributorDTO("name", 1L, "position", 1L);
		List<ResponseContributorDTO> contributorList = List.of(mockContributor);

		ResponsePublisherDTO mockPublisher = new ResponsePublisherDTO(1L, "publisher");
		List<ResponsePublisherDTO> publisherList = List.of(mockPublisher);

		PageResponse<ResponseContributorDTO> contributorPageResponse = new PageResponse<>();
		contributorPageResponse.setContent(contributorList);
		contributorPageResponse.setTotalElements(contributorList.size());
		contributorPageResponse.setTotalPages(1);
		contributorPageResponse.setNumber(0);
		contributorPageResponse.setSize(10);
		contributorPageResponse.setNumberOfElements(contributorList.size());
		contributorPageResponse.setFirst(true);
		contributorPageResponse.setLast(true);
		contributorPageResponse.setEmpty(false);

		PageResponse<ResponsePublisherDTO> publisherPageResponse = new PageResponse<>();
		publisherPageResponse.setContent(publisherList);
		publisherPageResponse.setTotalElements(publisherList.size());
		publisherPageResponse.setTotalPages(1);
		publisherPageResponse.setNumber(0);
		publisherPageResponse.setSize(10);
		publisherPageResponse.setNumberOfElements(publisherList.size());
		publisherPageResponse.setFirst(true);
		publisherPageResponse.setLast(true);
		publisherPageResponse.setEmpty(false);

		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		pageableInfo.setPageNumber(0);
		pageableInfo.setPageSize(10);
		pageableInfo.setOffset(0);
		pageableInfo.setPaged(true);
		pageableInfo.setUnpaged(false);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		sortInfo.setEmpty(true);
		sortInfo.setSorted(false);
		sortInfo.setUnsorted(true);

		pageableInfo.setSort(sortInfo);
		contributorPageResponse.setPageable(pageableInfo);
		contributorPageResponse.setSort(sortInfo);
		publisherPageResponse.setPageable(pageableInfo);
		publisherPageResponse.setSort(sortInfo);

		when(contributorService.getContributors(any())).thenReturn(contributorPageResponse);
		when(publisherService.getPublishers(any())).thenReturn(publisherPageResponse);

		List<ResponseCategoryDTO> mockCategories = List.of(new ResponseCategoryDTO(1L, "category", null));
		when(adminCategoryService.getCategories()).thenReturn(mockCategories);

		ResponseTagDTO tag = new ResponseTagDTO(1L, "tag");
		List<ResponseTagDTO> mockTagList = List.of(tag);

		PageResponse<ResponseTagDTO> tagPageResponse = new PageResponse<>();
		tagPageResponse.setContent(mockTagList);
		tagPageResponse.setTotalElements(1);
		tagPageResponse.setTotalPages(1);
		tagPageResponse.setSize(10);
		tagPageResponse.setNumber(0);
		tagPageResponse.setNumberOfElements(1);
		tagPageResponse.setFirst(true);
		tagPageResponse.setLast(true);
		tagPageResponse.setEmpty(false);

		tagPageResponse.setPageable(pageableInfo);
		tagPageResponse.setSort(sortInfo);

		when(tagService.getTags(Pageable.unpaged())).thenReturn(tagPageResponse);

		List<ResponseProductStateDTO> mockStates = List.of(new ResponseProductStateDTO(1L, ProductStateName.SALE.name()));
		when(productStateService.getProductStates()).thenReturn(mockStates);

		// when & then
		mockMvc.perform(get("/admin/settings/books/register")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/books/register"))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attributeExists("contributors"))
			.andExpect(model().attributeExists("publishers"))
			.andExpect(model().attributeExists("categories"))
			.andExpect(model().attributeExists("tags"))
			.andExpect(model().attributeExists("states"));
	}

	@Test
	@DisplayName("도서 등록 - success")
	void create_product_success_test() throws Exception {
		// given
		MockMultipartFile mockFile = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());

		// when & then
		mockMvc.perform(multipart("/admin/settings/books/register")
				.file(mockFile)
				.param("productStateId", "1")
				.param("publisherId", "1")
				.param("productTitle", "title")
				.param("productContent", "content")
				.param("productDescription", "description")
				.param("productPublishedAt", LocalDate.now().toString())
				.param("productIsbn", "978-89-12345-01-1")
				.param("productRegularPrice", "10000")
				.param("productSalePrice", "8000")
				.param("productPackageable", "true")
				.param("productStock", "100")
				.param("tagIds", "1")
				.param("categoryIds", "1")
				.param("contributorIds", "1")
				.with(csrf()))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	@DisplayName("도서 등록 - fail")
	void create_product_fail_test() throws Exception {
		// given
		String productTitle = null;
		MockMultipartFile mockFile = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());

		// when & then
		mockMvc.perform(multipart("/admin/settings/books/register")
				.file(mockFile)
				.param("productStateId", "1")
				.param("publisherId", "1")
				.param("productTitle", productTitle)
				.param("productContent", "content")
				.param("productDescription", "description")
				.param("productPublishedAt", LocalDate.now().toString())
				.param("productIsbn", "978-89-12345-01-1")
				.param("productRegularPrice", "10000")
				.param("productSalePrice", "8000")
				.param("productPackageable", "true")
				.param("productStock", "100")
				.param("productImagePaths", "a.png", "b.png")
				.param("tagIds", "1")
				.param("categoryIds", "1")
				.param("contributorIds", "1")
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("도서 수정 - success")
	void update_product_success_test() throws Exception {
		// given
		MockMultipartFile mockFile = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());

		// when & then
		mockMvc.perform(multipart("/admin/settings/books/register/1")
				.file(mockFile)
				.param("productStateId", "1")
				.param("publisherId", "1")
				.param("productTitle", "title")
				.param("productContent", "content")
				.param("productDescription", "description")
				.param("productPublishedAt", LocalDate.now().toString())
				.param("productIsbn", "978-89-12345-01-1")
				.param("productRegularPrice", "10000")
				.param("productSalePrice", "8000")
				.param("productPackageable", "true")
				.param("productStock", "100")
				.param("tagIds", "1")
				.param("categoryIds", "1")
				.param("contributorIds", "1")
				.with(csrf())
				.with(request -> {
					request.setMethod("PUT");
					return request;
				}))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	@DisplayName("도서 수정 - fail")
	void update_product_fail_test() throws Exception {
		// given
		String productTitle = null;
		MockMultipartFile mockFile = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());

		// when & then
		mockMvc.perform(multipart("/admin/settings/books/register/1")
				.file(mockFile)
				.param("productStateId", "1")
				.param("publisherId", "1")
				.param("productTitle", productTitle)
				.param("productContent", "content")
				.param("productDescription", "description")
				.param("productPublishedAt", LocalDate.now().toString())
				.param("productIsbn", "978-89-12345-01-1")
				.param("productRegularPrice", "10000")
				.param("productSalePrice", "8000")
				.param("productPackageable", "true")
				.param("productStock", "100")
				.param("tagIds", "1")
				.param("categoryIds", "1")
				.param("contributorIds", "1")
				.with(csrf())
				.with(request -> {
					request.setMethod("PUT");
					return request;
				}))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("도서 판매가 수정 - success")
	void update_productSalePrice_success_test() throws Exception {
		// given
		RequestProductSalePriceUpdateDTO request = new RequestProductSalePriceUpdateDTO(500L);

		// when & then
		mockMvc.perform(put("/admin/settings/books/1/salePrice")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	@DisplayName("도서 판매가 수정 - fail")
	void update_productSalePrice_fail_test() throws Exception {
		// given
		RequestProductSalePriceUpdateDTO request = new RequestProductSalePriceUpdateDTO(null);

		// when & then
		mockMvc.perform(put("/admin/settings/books/1/salePrice")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}
}
