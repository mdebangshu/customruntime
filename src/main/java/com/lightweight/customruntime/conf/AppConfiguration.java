package com.lightweight.customruntime.conf;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public class AppConfiguration {

	private final GenericApplicationContext context;

	public AppConfiguration(GenericApplicationContext context) {
		this.context = context;
	}

	public RestTemplate getRestTemplate() {
		Environment env = context.getEnvironment();
		String connectionTimeout = env.getProperty("rest.template.connection.timeout");
		String readTimeout = env.getProperty("rest.template.read.timeout");
		return this.getRestTemplateBuilder().setConnectTimeout(Duration.ofMillis(Integer.valueOf(connectionTimeout)))
				.setReadTimeout(Duration.ofMillis(Integer.valueOf(readTimeout))).setBufferRequestBody(true).build();
	}

	public RestTemplateBuilder getRestTemplateBuilder() {
		CustomRestTemplateCustomizer customRestTemplateCustomizer = new CustomRestTemplateCustomizer();
		return new RestTemplateBuilder(customRestTemplateCustomizer);
	}
}