package shop.freenanum.trade.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.filter.JwtRequestFilter;
import shop.freenanum.trade.handler.MyAuthenticationFailureHandler;
import shop.freenanum.trade.handler.MyAuthenticationSuccessHandler;
import shop.freenanum.trade.service.impl.MyUserDetailsServiceImpl;
import shop.freenanum.trade.util.JwtUtil;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final MyUserDetailsServiceImpl userDetailsService;
    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
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


                // 로그인 필터 추가
                .addFilterAt(new LoginFilter(customReactiveAuthenticationManager, successHandler, failureHandler), SecurityWebFiltersOrder.AUTHENTICATION)

//                // 로그아웃 필터 추가
//                .addFilterBefore(new LogoutFilter(jwtTokenService), SecurityWebFiltersOrder.AUTHENTICATION)
//
//                // OAuth2 설정
//                .oauth2Login(oauth2 -> oauth2
//                        .authenticationSuccessHandler(customSuccessHandler)
//                )
//                //reissue 필터
//                .addFilterBefore(new ReissueFilter(jwtUtil, jwtTokenService), SecurityWebFiltersOrder.AUTHORIZATION)
                // 권한 설정
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/users/login", "/oauth2/**").permitAll()  // /login과 /oauth2/** 경로는 인증 없이 접근 가능
                        .pathMatchers("/api/products/list", "/api/products/{id}").permitAll()
                        .pathMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/assets/**").permitAll()
                        .anyExchange().authenticated()
                );
        return http.build();
    }

    private String resolveToken(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("Authorization");
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // 사용자 인증 설정
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityContext(context -> context.requireExplicitSave(false))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/users/login").permitAll() // 로그인 요청 허용
//                        .requestMatchers("/api/products/list").permitAll()
//                        .requestMatchers("/api/products/{id}").permitAll()
//                        .requestMatchers("/static/**").permitAll() // 모든 정적 리소스 허용
//                        .requestMatchers("/css/**").permitAll() // CSS 파일 허용
//                        .requestMatchers("/js/**").permitAll() // JS 파일 허용
//                        .requestMatchers("/images/**").permitAll() // 이미지 파일 허용
//                        .requestMatchers("/assets/**").permitAll()
//                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
//                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/api/users/login")
//                        .successHandler(myAuthenticationSuccessHandler) // 로그인 성공 핸들러 설정
//                        .failureHandler(myAuthenticationFailureHandler) // 로그인 실패 핸들러 설정 (필요시 추가)
//                )
//                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("*") // 특정 출처로 변경 가능
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders("*");
//            }
//        };
//    }