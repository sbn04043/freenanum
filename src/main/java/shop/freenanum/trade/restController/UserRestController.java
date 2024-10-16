package shop.freenanum.trade.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 허용할 출처
public class UserRestController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserModel userModel) throws Exception {
        try {
            // 사용자 인증 시도
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("INVALID_CREDENTIALS", e); // 인증 실패 시 예외 발생
        }

        System.out.println("after try-catch");

        // 인증에 성공한 후 사용자 정보를 가져옴
        final UserEntity userEntity = (UserEntity) userDetailsService.loadUserByUsername(userModel.getUsername());
        System.out.println(userEntity);

        // JWT 토큰 생성
        String authToken = jwtUtil.generateToken(userEntity.getUsername());
        System.out.println("authToken: " + authToken);

        // JWT 토큰을 응답으로 반환
        return ResponseEntity.ok(authToken);
    }

    @GetMapping("/protected")
    @PreAuthorize("hasAuthority('ROLE_USER')") // ROLE_USER 권한을 가진 사용자만 접근 가능
    public ResponseEntity<String> getCurrentUser() {
        System.out.println("protected");
        String protectedResource = "이것은 보호된 리소스입니다.";
        return ResponseEntity.ok(protectedResource);
    }

}
