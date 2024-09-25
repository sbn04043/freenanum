package shop.freenanum.trade.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.service.UserService;
import shop.freenanum.trade.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody UserModel userModel) {
        UserModel foundUser = userService.findByEmail(userModel.getEmail());
        if (foundUser != null && passwordEncoder.matches(userModel.getPassword(), foundUser.getPassword())) {
            return jwtUtil.generateToken(foundUser.getEmail(), foundUser.getId());
        }
        throw new RuntimeException("Invalid credentials");
    }
}
