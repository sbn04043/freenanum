package shop.freenanum.trade.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class LoginFilter extends AuthenticationWebFilter {

    private final UserDetailsService userDetailsService;
    private final ServerAuthenticationSuccessHandler successHandler;
    private final ServerAuthenticationFailureHandler failureHandler;

    public LoginFilter(AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       ServerAuthenticationSuccessHandler successHandler,
                       ServerAuthenticationFailureHandler failureHandler) {
        super(authenticationManager); // 상위 클래스 생성자 호출
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 요청 본문에서 사용자 이름과 비밀번호 추출
        return exchange.getRequest().getBody()
                .flatMap(dataBuffer -> {
                    String requestBody = dataBuffer.toString(StandardCharsets.UTF_8);
                    String[] credentials = requestBody.split("&");
                    String username = null;
                    String password = null;

                    for (String credential : credentials) {
                        String[] keyValue = credential.split("=");
                        if ("username".equals(keyValue[0])) {
                            username = keyValue[1];
                        } else if ("password".equals(keyValue[0])) {
                            password = keyValue[1];
                        }
                    }

                    // 사용자 이름과 비밀번호를 사용하여 인증 토큰 생성
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, password);

                    return this.getAuthenticationManager().authenticate(authenticationToken)
                            .doOnNext(authentication -> {
                                // 인증 성공 시 핸들러 호출
                                successHandler.onAuthenticationSuccess(exchange, authentication);
                            })
                            .onErrorResume(AuthenticationException.class, e -> {
                                // 인증 실패 시 핸들러 호출
                                return failureHandler.onAuthenticationFailure(exchange, e);
                            });
                })
                .flatMap(authentication -> {
                    // 인증 정보가 성공적으로 처리되면 체인을 계속 진행
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange)); // 인증이 실패한 경우에도 체인을 계속 진행
    }
}
