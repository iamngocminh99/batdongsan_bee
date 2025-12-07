package com.ngocminh.batdongsan_be.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.base-url}")
    private String baseUrl;

    @Bean
    public OpenAiService openAiService() {

        System.setProperty("OPENAI_API_BASE", baseUrl);
        System.out.println("Using base URL: " + baseUrl);
        return new OpenAiService(apiKey, Duration.ofSeconds(60));
    }
}
