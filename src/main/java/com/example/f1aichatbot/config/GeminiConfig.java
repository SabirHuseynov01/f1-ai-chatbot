package com.example.f1aichatbot.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
@ConfigurationProperties(prefix = "gemini.api")
public class GeminiConfig {
    private String key;
    private String model;
    private Integer maxTokens;

    public void setKey(String key) { this.key = key; }
    public void setModel(String model) { this.model = model; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(30000);
        return new RestTemplate(factory);
    }
}
