package shop.freenanum.trade.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class MyAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        // 응답 상태 코드 설정: 401 Unauthorized
        webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        webFilterExchange.getExchange().getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 실패 메시지 작성
        String errorMessage = String.format("{\"error\": \"%s\"}", exception.getMessage());

        // 응답 전송
        return webFilterExchange.getExchange().getResponse().writeWith(Mono.just(
                webFilterExchange.getExchange().getResponse().bufferFactory().wrap(errorMessage.getBytes(StandardCharsets.UTF_8))
        ));
    }
}
