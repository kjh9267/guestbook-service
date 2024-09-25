package me.jun.guestbookservice.core.infra;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.core.application.WriterService;
import me.jun.guestbookservice.core.application.dto.RetrieveWriterIdRequest;
import me.jun.guestbookservice.core.application.dto.WriterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class WriterServiceImpl implements WriterService {

    private final WebClient.Builder writerWebClientBuilder;

    private final String writerUri;

    public WriterServiceImpl(
            WebClient.Builder writerWebClientBuilder,
            @Value("#{${writer-uri}}") String writerUri
    ) {
        this.writerWebClientBuilder = writerWebClientBuilder;
        this.writerUri = writerUri;
    }

    @Override
    @CircuitBreaker(name = "writerCircuitBreaker")
    public Mono<Object> retrieveWriterIdByEmail(String email) {
        RetrieveWriterIdRequest request = RetrieveWriterIdRequest.builder()
                .email(email)
                .build();

        return writerWebClientBuilder
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build()
                .post()
                .uri(writerUri)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(request), RetrieveWriterIdRequest.class)
                .retrieve()
                .bodyToMono(WriterResponse.class).log()
                .map(writer -> writer.getId()).log()
                .map(id -> (Object) id).log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
}
