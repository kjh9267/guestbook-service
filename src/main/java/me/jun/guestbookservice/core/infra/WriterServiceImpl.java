package me.jun.guestbookservice.core.infra;

import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.core.application.WriterService;
import me.jun.guestbookservice.core.application.dto.WriterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class WriterServiceImpl implements WriterService {

    private final WebClient writerWebClient;

    private final String writerUri;

    public WriterServiceImpl(WebClient writerWebClient, @Value("#{${writer-uri}}") String writerUri) {
        this.writerWebClient = writerWebClient;
        this.writerUri = writerUri;
    }

    @Override
    public Mono<Object> retrieveWriterIdByEmail(String email) {
        return writerWebClient.post()
                .uri(writerUri)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(email), String.class)
                .retrieve()
                .bodyToMono(WriterResponse.class)
                .log()
                .map(writer -> writer.getId())
                .map(id -> (Object) id)
                .doOnError(throwable -> log.info("{}", throwable));
    }
}
