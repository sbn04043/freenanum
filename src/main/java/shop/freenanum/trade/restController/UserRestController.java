package shop.freenanum.trade.restController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.service.UserService;
import shop.freenanum.trade.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody UserModel userModel, HttpServletResponse response) {
        String email = userService.login(userModel.getEmail(), userModel.getPassword());
        if (email.equals("Invalid")) {
            return email;
        } else {
            String token = jwtUtil.generateToken(email);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/"); // 모든 경로에서 접근 가능하게
            response.addCookie(cookie);
            return cookie.getValue();
        }
    }

    @GetMapping("/isValidateEmail")
    public ResponseEntity<String> isValidateEmail(@RequestParam("email") String email) {
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.ok("available");
        } else {
            return ResponseEntity.ok("unavailable");
        }
    }
}
