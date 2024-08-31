package me.jun.guestbookservice.common.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.common.security.exception.InvalidTokenException;
import me.jun.guestbookservice.core.application.WriterService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSubjectResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    private final WriterService writerServiceImpl;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WriterId.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        String token;

        try {
            token = exchange.getRequest()
                    .getHeaders()
                    .get(AUTHORIZATION)
                    .get(0);
        }
        catch (Exception e) {
            throw InvalidTokenException.of(e.getMessage());
        }

        return Mono.fromSupplier(() -> jwtProvider.extractSubject(token))
                .log()
                .flatMap(
                        email -> writerServiceImpl.retrieveWriterIdByEmail(email)
                                .log()
                                .doOnError(throwable -> log.info("{}", throwable))
                )
                .doOnError(throwable -> log.info("{}", throwable));
    }
}
