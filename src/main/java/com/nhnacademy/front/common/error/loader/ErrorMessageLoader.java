package com.nhnacademy.front.common.error.loader;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import lombok.Getter;

@Getter
@Component
public class ErrorMessageLoader {

	private Map<String, String> errorMessages = Collections.emptyMap();

	@Value("${app.locale}")
	private String locale;
	@Value("${error.message.kr}")
	private String krUrl;
	@Value("${error.message.eng}")
	private String engUrl;

	public ErrorMessageLoader() {

		try {
			String targetUrl = locale.equals("eng") ? engUrl : krUrl;
			loadYamlFromUrl(targetUrl);
		} catch (Exception e) {
			System.err.println("에러 메시지 파일 로드 실패: " + e.getMessage());
			errorMessages = Collections.emptyMap();
		}

	}

	private void loadYamlFromUrl(String url) throws Exception {
		Yaml yaml = new Yaml();

		try (InputStream inputStream = new URL(url).openStream()) {
			Map<String, Object> data = yaml.load(inputStream);
			Map<String, String> errors = (Map<String, String>)data.get("errors");

			errorMessages.putAll(errors);
		}

	}

	public String getMessage(String code) {
		return errorMessages.getOrDefault(code, "알 수 없는 오류입니다.");
	}

}
