package shop.freenanum.trade.restController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 허용할 출처
public class UserRestController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
//    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserModel userModel) throws Exception {
        UserEntity userEntity = userRepository.findByUsernameAndPassword(userModel.getUsername(), userModel.getPassword());
        if (userEntity == null) {
            System.out.println("로그인 실패");
            return ResponseEntity.ok("로그인 실패");
        } else {
            httpSession.setAttribute("user", userEntity);
            System.out.println("로그인 성공");
            return ResponseEntity.ok("로그인 성공");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        httpSession.removeAttribute("user");
        return ResponseEntity.ok("로그아웃");
    }

//    @GetMapping("/protected")
//    @PreAuthorize("hasAuthority('ROLE_USER')") // ROLE_USER 권한을 가진 사용자만 접근 가능
//    public ResponseEntity<String> getCurrentUser() {
//        System.out.println("protected");
//        String protectedResource = "이것은 보호된 리소스입니다.";
//        return ResponseEntity.ok(protectedResource);
//    }

}
