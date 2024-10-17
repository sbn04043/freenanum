package shop.freenanum.trade.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.filter.JwtRequestFilter;
import shop.freenanum.trade.filter.LoginFilter;
import shop.freenanum.trade.handler.MyAuthenticationFailureHandler;
import shop.freenanum.trade.handler.MyAuthenticationSuccessHandler;
import shop.freenanum.trade.service.impl.MyUserDetailsServiceImpl;
import shop.freenanum.trade.util.JwtUtil;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final MyUserDetailsServiceImpl userDetailsService;
    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // CORS 설정
                .cors(corsCustomizer -> corsCustomizer
                        .configurationSource(request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            configuration.setAllowCredentials(true);
                            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "nickname"));
                            configuration.setMaxAge(3600L); // 1 hour
                            configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                            return configuration;
                        }))
                // 기본 보안 설정 비활성화
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // JWT 필터 추가
                .addFilterBefore((exchange, chain) -> {
                    String jwtToken = resolveToken(exchange);

                    if (jwtToken != null && !jwtUtil.isExpired(jwtToken)) {
                        String nickname = jwtUtil.getUsernameFromToken(jwtToken);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(nickname, null, null);

                        // ReactiveSecurityContextHolder에 인증 정보 설정
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                                .then(Mono.defer(() -> exchange.getResponse().setComplete()))
                                .doOnSuccess(unused -> {
                                    System.out.println(nickname + " User has been authenticated and JWT tokens returned");
                                })
                                .doOnError(Throwable::printStackTrace);
                    }

                    // JWT 토큰이 없거나 유효하지 않은 경우 필터 체인 계속 진행
                    return chain.filter(exchange);
                }, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterBefore(new LoginFilter(userDetailsService, myAuthenticationSuccessHandler, myAuthenticationFailureHandler), SecurityWebFiltersOrder.AUTHENTICATION)

                // 권한 설정
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/users/login", "/api/users/oauth2/**").permitAll()
                        .pathMatchers("/api/products/list", "/api/products/{id}").permitAll()
                        .pathMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/assets/**").permitAll()
                        .anyExchange().authenticated()
                );

        return http.build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String resolveToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer "를 제외한 토큰 반환
        }
        return null; // 토큰이 없으면 null 반환
    }
}

//                // JWT 필터 추가
//                .addFilterBefore((exchange, chain) -> {
//                    String jwtToken = resolveToken(exchange);
//
//                    if (jwtToken != null && !jwtUtil.isExpired(jwtToken)) {
//                        String nickname = jwtUtil.getUsernameFromToken(jwtToken);
//                        UsernamePasswordAuthenticationToken authentication =
//                                new UsernamePasswordAuthenticationToken(nickname, null, null);
//
//                        // ReactiveSecurityContextHolder에 인증 정보 설정
//                        return chain.filter(exchange)
//                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
//                                .then(Mono.defer(() -> exchange.getResponse().setComplete()))
//                                .doOnSuccess(unused -> {
//                                    System.out.println(nickname + " User has been authenticated and JWT tokens returned");
//                                })
//                                .doOnError(Throwable::printStackTrace);
//                    }
//
//                    // JWT 토큰이 없거나 유효하지 않은 경우 필터 체인 계속 진행
//                    return chain.filter(exchange);
//                }, SecurityWebFiltersOrder.AUTHENTICATION)


//                // 로그인 필터 추가
//                .addFilterAt(new LoginFilter(customReactiveAuthenticationManager, successHandler, failureHandler), SecurityWebFiltersOrder.AUTHENTICATION)

//                // 로그아웃 필터 추가
//                .addFilterBefore(new LogoutFilter(jwtTokenService), SecurityWebFiltersOrder.AUTHENTICATION)
//
//                // OAuth2 설정
//                .oauth2Login(oauth2 -> oauth2
//                        .authenticationSuccessHandler(customSuccessHandler)
//                )
//                //reissue 필터
//                .addFilterBefore(new ReissueFilter(jwtUtil, jwtTokenService), SecurityWebFiltersOrder.AUTHORIZATION)