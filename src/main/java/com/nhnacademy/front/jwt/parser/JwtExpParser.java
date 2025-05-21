package com.nhnacademy.front.jwt.parser;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JwtTokenAndRoleAop 에서 토큰이 유효한지 확인하기 위해서 exp 유효시간이 남아있는지 확인하는 메서드
 * parser 를 통해서 exp 시간 추출
 */
public class JwtExpParser {

	private JwtExpParser() {
	}

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

			return switch (exp) {
				case Number number -> number.longValue();
				case String str -> Long.parseLong(str);
				default -> null;
			};

		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 현재 시각과 exp 값을 비교하여 토큰이 아직 유효한지 확인
	 */
	public static boolean isTokenValid(String token) {
		Long exp = getExp(token);
		if (exp == null) {
			return false;
		}

		long now = Instant.now().getEpochSecond(); // 현재 시각 (초 단위)
		return exp > now;
	}

}
