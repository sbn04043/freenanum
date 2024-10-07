package shop.freenanum.trade.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.util.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 허용할 출처
public class UserRestController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth")
    public ResponseEntity<String> login(@RequestBody Map<String, String> map) {
        System.out.println("진입0" + map);

        String username = map.get("username");
        String password = map.get("password");
//        System.out.println("진입1" + username + password);
        try {
            // 인증 객체 생성
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password));

            // 인증이 성공하면 JWT 생성
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

            // 성공 응답 반환
            return ResponseEntity.ok("성공");

        } catch (BadCredentialsException e) {
            // 잘못된 자격 증명 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            // 일반 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }

}
