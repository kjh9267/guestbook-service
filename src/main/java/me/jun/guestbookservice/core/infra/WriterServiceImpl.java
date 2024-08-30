package me.jun.guestbookservice.core.infra;

import me.jun.guestbookservice.core.application.WriterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        return writerWebClient.get()
                .uri(writerUri + "/" + email)
                .retrieve()
                .bodyToMono(Object.class);
    }
}
