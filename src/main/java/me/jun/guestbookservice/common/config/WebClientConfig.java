package me.jun.guestbookservice.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.ACCEPT;

@Configuration
public class WebClientConfig {

    private final String writerUrl;

    public WebClientConfig(@Value("#{${writer-base-url}}") String writerUrl) {
        this.writerUrl = writerUrl;
    }

    @Bean
    public WebClient writerWebClient() {
        return WebClient.builder()
                .baseUrl(writerUrl)
                .defaultHeader(ACCEPT, APPLICATION_JSON)
                .build();
    }
}
