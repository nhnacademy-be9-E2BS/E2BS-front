package com.nhnacademy.front.account.admin.model.dto.response;

import com.nhnacademy.front.account.customer.model.domain.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminSettingsNonMembersDTO {

	private Customer customer;

}
