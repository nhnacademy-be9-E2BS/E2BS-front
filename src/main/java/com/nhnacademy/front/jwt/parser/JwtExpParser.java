package com.nhnacademy.front.jwt.parser;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JwtTokenAndRoleAop 에서 토큰이 유효한지 확인하기 위해서 exp 유효시간이 남아있는지 확인하는 메서드
 * parser 를 통해서 exp 시간 추출
 */
public class JwtExpParser {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static Long getExp(String accessToken) {

		try {
			String[] parts = accessToken.split("\\.");
			if (parts.length != 3) {
				return null;
			}

			String payload = parts[1];
			byte[] decodeBytes = Base64.getUrlDecoder().decode(payload);
			String payloadJson = new String(decodeBytes);

			Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);
			Object exp = claims.get("exp");

			if (exp instanceof Number) {
				return ((Number)exp).longValue();
			} else if (exp instanceof String) {
				return Long.parseLong((String)exp);
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}

	}

}
