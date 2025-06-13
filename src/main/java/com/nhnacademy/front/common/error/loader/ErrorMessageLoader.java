package com.nhnacademy.front.common.error.loader;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.nhnacademy.front.common.error.exception.ServerErrorException;

import lombok.Getter;

@Getter
@Component
public class ErrorMessageLoader {
	private static final Logger logger = LoggerFactory.getLogger(ErrorMessageLoader.class);

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
			logger.error("에러 메시지 파일 로드 실패", e);
			errorMessages = Collections.emptyMap();
		}

	}

	private void loadYamlFromUrl(String url) {
		Yaml yaml = new Yaml();

		try (InputStream inputStream = new URL(url).openStream()) { // NOSONAR
			Map<String, Object> data = yaml.load(inputStream);
			Map<String, String> errors = (Map<String, String>)data.get("errors"); // NOSONAR

			errorMessages.putAll(errors);
		} catch (Exception ex) {
			throw new ServerErrorException();
		}

	}

	public String getMessage(String code) {
		return errorMessages.getOrDefault(code, "알 수 없는 오류입니다.");
	}

}
