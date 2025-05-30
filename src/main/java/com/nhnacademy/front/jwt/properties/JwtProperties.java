package com.nhnacademy.front.jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String accessSecret;
	private String refreshSecret;
	private long accessExpiration;
	private long refreshExpiration;
}
