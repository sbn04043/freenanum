package shop.freenanum.trade.restController;

import lombok.RequiredArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody UserModel userModel) {
        String username = userService.login(userModel.getEmail(), userModel.getPassword());
        return jwtUtil.generateToken(username);
    }

    @GetMapping("/isValidateEmail")
    public String isValidateEmail(@RequestParam("email") String email) {
        if (userService.findByEmail(email) != null) {
            return "available";
        } else {
            return "unavailable";
        }
    }
}
