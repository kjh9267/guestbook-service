package me.jun.guestbookservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("test")
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder writerWebClientBuilder() {
        return WebClient.builder();
    }
}
