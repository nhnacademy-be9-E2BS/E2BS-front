package com.nhnacademy.front.cart.model.dto.request;

import java.util.List;

import com.nhnacademy.front.cart.model.dto.MergeCartItemDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCartItemMergeDTO {

	@NotBlank
	private String memberId;

	@NotNull
	private List<MergeCartItemDTO> mergeCartItemDTOS;

}
