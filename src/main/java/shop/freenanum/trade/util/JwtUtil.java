package shop.freenanum.trade.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey; // 비밀 키

    @Value("${jwt.expiration}")
    private Long expirationTime;

    // JWT에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // JWT 만료 여부 확인
    public boolean isExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    private SecretKey getSigningKey() {
        System.out.println("JwtUtil.getSigningKey");
        System.out.println();
        byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Base64로 디코딩
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"); // SecretKey 생성
    }

    public Claims parseToken(String token) {
        System.out.println("JwtUtil.parseToken");
        try {
            // JWT를 파싱하여 Claims를 추출
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JWT token: " + e.getMessage());
        }
    }

    public String generateToken(String username) {
        System.out.println("JwtUtil.generateToken");
        System.out.println("username: " + username);
        // 필요한 경우 claims에 추가 정보 저장
        Map<String, Object> claims = new HashMap<>();

        String jwts = Jwts.builder()
                .setClaims(claims) // Claims 설정
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
        System.out.println("jwts: " + jwts);

        return jwts;
    }

    public String extractUsername(String token) {
        System.out.println("JwtUtil.extractUsername");
        System.out.println("token = " + token);
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // SecretKey로 서명 검증
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        System.out.println("JwtUtil.validateToken");
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        System.out.println("JwtUtil.isTokenExpired");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            // 토큰 파싱 중 오류 발생 시 만료 처리
            return true;
        }
    }
}
