package shop.freenanum.trade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.UserService;
//import shop.freenanum.trade.util.JwtUtil;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 허용할 출처
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
//    private final JwtUtil jwtUtil;
//    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup() {
        return "/users/signup";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           @RequestParam("nickname") String nickname,
                           @RequestParam("phone") String phone,
                           @RequestParam("userAddress") String userAddress,
                           @RequestParam("gender") String gender,
                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        if (profileImage != null) {
            userService.save(UserEntity.toRegisterEntity(
                    UserModel.builder()
                            .username(username)
                            .password(password)
                            .name(name)
                            .nickname(nickname)
                            .phone(phone)
                            .userAddress(userAddress)
                            .gender(gender)
                            .build()
            ), profileImage);
        } else {
            userRepository.save(UserEntity.toRegisterEntity(
                    UserModel.builder()
                            .username(username)
                            .password(password)
                            .name(name)
                            .nickname(nickname)
                            .phone(phone)
                            .userAddress(userAddress)
                            .gender(gender)
                            .build()
            ));
        }

        return "redirect:/api/users/signup";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("users", userRepository.getList().stream().map(userEntity -> UserModel.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .phone(userEntity.getPhone())
                .sellCount(userEntity.getSellCount())
                .buyCount(userEntity.getBuyCount())
                .userAddress(userEntity.getUserAddress())
                .score(userEntity.getScore())
                .build()).toList());

        return "users/list";
    }
}
