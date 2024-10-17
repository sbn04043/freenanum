package shop.freenanum.trade.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.util.JwtUtil;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        // JWT 토큰 생성
        String jwtToken = jwtUtil.generateToken(authentication.getName());

        // 응답 본문에 포함할 데이터 준비
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", jwtToken);

        // 응답 설정
        webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
        webFilterExchange.getExchange().getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // JSON 형식으로 응답 본문 작성
        String jsonResponse = String.format("{\"token\":\"%s\"}", jwtToken);

        // 응답 전송
        return webFilterExchange.getExchange().getResponse().writeWith(Mono.just(
                webFilterExchange.getExchange().getResponse().bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8))
        ));
    }
}


