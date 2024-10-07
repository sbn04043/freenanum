package shop.freenanum.trade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.querydsl.UserQuerydsl;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.UserService;
import shop.freenanum.trade.util.JwtUtil;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup(Model model) {
        return "/users/signup";
    }

    @PostMapping("/register")
    public String register(@RequestBody UserModel userModel, Model model) {
        System.out.println("userModel: " + userModel);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(UserEntity.toRegisterEntity(userModel));
        return "redirect:/api/products/list";
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
