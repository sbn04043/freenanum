package shop.freenanum.trade.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.freenanum.trade.util.JwtUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 로그인 요청 URL을 확인하여 필터를 적용할지 결정합니다.
        return !request.getServletPath().equals("/api/users/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username != null && password != null) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            try {
                Authentication authentication = authenticationManager.authenticate(authRequest);
                // 인증에 성공한 경우 JWT를 생성하여 응답 헤더에 추가합니다.
//                String jwtToken = jwtUtil.generateToken(authentication);
//                response.setHeader("Authorization", "Bearer " + jwtToken);
                // 인증 정보를 WebAuthenticationDetailsSource에 설정합니다.
                authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            } catch (AuthenticationException e) {
                // 인증 실패 시 적절한 오류 처리를 할 수 있습니다.
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authentication failed: " + e.getMessage());
                return;
            }
        }

        // 다음 필터로 진행합니다.
        filterChain.doFilter(request, response);
    }
}
