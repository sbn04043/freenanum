//package shop.freenanum.trade.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import shop.freenanum.trade.filter.JwtRequestFilter;
//import shop.freenanum.trade.model.domain.UserModel;
//import shop.freenanum.trade.util.JwtUtil;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
//        http
//                // CORS 설정
//                .cors(corsCustomizer -> corsCustomizer
//                        .configurationSource(request -> {
//                            CorsConfiguration configuration = new CorsConfiguration();
//                            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                            configuration.setAllowCredentials(true);
//                            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "nickname"));
//                            configuration.setMaxAge(3600L); // 1 hour
//                            configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
//                            return configuration;
//                        }))
//                // 기본 보안 설정 비활성화
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//
//                // JWT 필터 추가
//                .addFilterBefore((exchange, chain) -> {
//                    String jwtToken = resolveToken(exchange);
//
//                    if (jwtToken != null && !jwtUtil.isExpired(jwtToken)) {
//                        String nickname = jwtUtil.getNickNameFromToken(jwtToken);
//                        UsernamePasswordAuthenticationToken authentication =
//                                new UsernamePasswordAuthenticationToken(nickname, null, null);
//
//                        // ReactiveSecurityContextHolder에 인증 정보 설정
//                        return chain.filter(exchange)
//                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
//                                .then(Mono.defer(() -> exchange.getResponse().setComplete()))
//                                .doOnSuccess(unused -> {
//                                    log.info("User '{}' has been authenticated and JWT tokens returned", nickname);
//                                })
//                                .doOnError(error -> {
//                                    log.error("Failed to authenticate user '{}': {}", nickname, error.getMessage());
//                                });
//                    }
//
//                    // JWT 토큰이 없거나 유효하지 않은 경우 필터 체인 계속 진행
//                    return chain.filter(exchange);
//                }, SecurityWebFiltersOrder.AUTHENTICATION)
//
//
//                // 로그인 필터 추가
//                .addFilterAt(new LoginFilter(customReactiveAuthenticationManager, successHandler, failureHandler), SecurityWebFiltersOrder.AUTHENTICATION)
//
//                // 로그아웃 필터 추가
//                .addFilterBefore(new LogoutFilter(jwtTokenService), SecurityWebFiltersOrder.AUTHENTICATION)
//
//                // OAuth2 설정
//                .oauth2Login(oauth2 -> oauth2
//                        .authenticationSuccessHandler(customSuccessHandler)
//                )
//                //reissue 필터
//                .addFilterBefore(new ReissueFilter(jwtUtil, jwtTokenService), SecurityWebFiltersOrder.AUTHORIZATION)
//
//
//                // 권한 설정
//                .authorizeExchange(auth -> auth
//                        .pathMatchers("/login", "/oauth2/**", "/api/users", "/api/users/checkPassword", "/api/users/checkNickname", "/api/users/checkRole", "/api/users/updateDeclaration", "/api/users/updateLogoutUserTime").permitAll()  // /login과 /oauth2/** 경로는 인증 없이 접근 가능
//                        .pathMatchers("/api/comments/{postId}").permitAll()
//
//                        .pathMatchers(HttpMethod.GET, "/api/files/*").permitAll()
//                        .pathMatchers(HttpMethod.GET, "/api/files").permitAll()
//
//                        .pathMatchers("/api/groups/books").permitAll()
//
//                        .pathMatchers("/api/groups/groups").permitAll()
//                        .pathMatchers("/api/groups/groups/able").hasAuthority(Role.ROLE_ADMIN.getCode())
//                        .pathMatchers("/api/groups/groups/enable").hasAuthority(Role.ROLE_ADMIN.getCode())
//                        .pathMatchers("/api/groups/groups/enable-list").hasAuthority(Role.ROLE_ADMIN.getCode())
//                        .pathMatchers("/api/groups/groups/users/{groupId}").permitAll()
//
//                        .pathMatchers("/api/groups/posts/{groupId}").permitAll()
//                        .pathMatchers(HttpMethod.PUT, "/api/groups/posts/{postId}").permitAll()
//
//                        .pathMatchers("/api/rooms/accounts/room/{roomId}").hasAuthority(Role.ROLE_SELLER.getCode())
//
//                        .pathMatchers(HttpMethod.POST, "/api/rooms/addresses").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers(HttpMethod.PUT, "/api/rooms/addresses").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers("/api/rooms/addresses/{id}").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers("/api/rooms/addresses/search").permitAll()
//
//                        .pathMatchers(HttpMethod.PUT, "/api/rooms/bookings/{id}").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers(HttpMethod.DELETE, "/api/rooms/bookings/{id}").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers("/api/rooms/bookings/room/{roomId}").hasAuthority(Role.ROLE_SELLER.getCode())
//
//                        .pathMatchers(HttpMethod.POST, "/api/rooms").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers(HttpMethod.PUT, "/api/rooms").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers("/api/rooms/{id}").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers("/api/rooms/user").hasAuthority(Role.ROLE_SELLER.getCode())
//                        .pathMatchers("/api/rooms/confirm/{id}").hasAuthority(Role.ROLE_ADMIN.getCode())
//                        .pathMatchers("/api/rooms/enabled").permitAll()
//
//                        .pathMatchers("/api/rooms/reviews/room/{roomId}").permitAll()
//
//                        .pathMatchers("/api/rooms/times/{roomId}").permitAll()
//
//                        .pathMatchers(HttpMethod.GET, "/api/users/aboard", "/api/users/aboard/viewCounts/{id}", "/api/users/aboard/details/{id}").permitAll()
//                        .pathMatchers(HttpMethod.DELETE, "/api/users/aboard/{id}", "/api/users/depost/{id}").hasRole(Role.ROLE_ADMIN.getCode())  // ROLE_ADMIN만 가능
//                        .pathMatchers(HttpMethod.PUT, "/api/users/aboard/{id}", "/api/users/updateRole").hasRole(Role.ROLE_ADMIN.getCode())
//                        .pathMatchers(HttpMethod.POST, "/api/users/aboard").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.GET, "/api/users/aboard/{nickname}", "/api/users/depost", "/api/users/findAllByNickname").hasRole(Role.ROLE_ADMIN.getCode())
//                        .anyExchange().authenticated() // 그 외 모든 경로는 인증 필요
//                );
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        // 사용자 인증 설정
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//        return authenticationManagerBuilder.build();
//    }
//}
//
