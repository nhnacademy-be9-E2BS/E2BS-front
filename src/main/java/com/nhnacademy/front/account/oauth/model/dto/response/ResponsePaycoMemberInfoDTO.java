package com.nhnacademy.front.account.oauth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePaycoMemberInfoDTO {
	private Header header;
	private PaycoData data;

	@Data
	public static class Header {
		@JsonProperty("isSuccessful")
		private boolean isSuccessful;
		@JsonProperty("resultCode")
		private int resultCode;
		@JsonProperty("resultMessage")
		private String resultMessage;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaycoData {
		private PaycoMember member;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaycoMember {
		private String idNo;
		private String email;
		private String name;
		private String mobile;

		@JsonProperty("birthdayMMdd")
		private String birthdayMMdd;
	}
}
