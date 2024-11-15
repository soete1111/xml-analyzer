package com.example.xml_analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class XmlAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlAnalyzerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.messageConverters(new ByteArrayHttpMessageConverter())
				.build();
	}

}
