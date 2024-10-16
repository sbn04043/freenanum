package shop.freenanum.trade.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.util.JwtUtil;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secretKey; // 비밀 키

    @Value("${jwt.expiration}")
    private Long expirationTime;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            String username = authentication.getName();
            String jwtToken = jwtUtil.generateToken(username);

            response.setHeader("Authorization", "Bearer " + jwtToken);
            response.getWriter().write("Login successful. Token: " + jwtToken);
            response.getWriter().flush();
        } catch (Exception e) {
            // 예외 발생 시 처리
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Authentication failed: " + e.getMessage());
            response.getWriter().flush();
        }
    }

    private String createJwtToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", authentication.getName()); // 사용자의 닉네임 또는 ID 추가
        // 필요한 추가 claims 추가 가능

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey()) // 비밀 키를 사용하여 서명
                .compact();
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes); // String을 Key로 변환
    }
}

