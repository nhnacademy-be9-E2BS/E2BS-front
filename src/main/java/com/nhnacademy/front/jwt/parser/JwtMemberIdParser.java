package com.nhnacademy.front.jwt.parser;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JwtToken payload 에 있는 memberId 값 secret 키 없이 파싱을 통해서 가져오는 메서드
 */
public class JwtMemberIdParser {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String getMemberId(String accessToken) {

		try {
			String[] parts = accessToken.split("\\.");
			if (parts.length != 3) {
				return null;
			}

			String payload = parts[1];
			byte[] decodeBytes = Base64.getUrlDecoder().decode(payload);
			String payloadJson = new String(decodeBytes);

			Map<String, String> claims = objectMapper.readValue(payloadJson, Map.class);

			return claims.get("MemberId");

		} catch (Exception e) {
			return null;
		}

	}

}
