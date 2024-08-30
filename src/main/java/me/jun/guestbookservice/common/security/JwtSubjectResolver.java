package me.jun.guestbookservice.common.security;

import lombok.RequiredArgsConstructor;
import me.jun.guestbookservice.core.application.WriterService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
        String token = exchange.getRequest()
                .getHeaders()
                .get(AUTHORIZATION)
                .get(0);

        jwtProvider.validateToken(token);
        String email = jwtProvider.extractSubject(token);

        return writerServiceImpl.retrieveWriterIdByEmail(email);
    }
}
