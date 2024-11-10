package shop.freenanum.trade.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.ChatMessageModel;
import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.ChatMessageEntity;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.repository.ChatMessageRepository;
import shop.freenanum.trade.model.repository.ChatRoomRepository;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.ProductService;
import shop.freenanum.trade.service.UserService;
import shop.freenanum.trade.service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//import shop.freenanum.trade.util.JwtUtil;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 허용할 출처
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final HttpSession httpSession;
    private final ChatRoomRepository chatRoomRepository;
    private final ProductServiceImpl productService;

    @GetMapping("/signup")
    public String signup() {
        return "/users/signup";
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

    @GetMapping("/chat")
    public String chatHome(HttpSession httpSession, Model model) {
        UserModel loginUser = (UserModel) httpSession.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/api/products/list";
        }
        List<ChatRoomModel> loginUserChatRoomList = chatRoomRepository.getLoginUserChatRooms(loginUser.getId())
                .stream().map(ChatRoomModel::toModel).toList();

        List<ChatMessageModel> loginUserChatMessageList = new ArrayList<>();
        loginUserChatRoomList.forEach(chatRoomModel -> {
            List<ChatMessageEntity> chatMessageEntityList = chatMessageRepository.findByChatRoomId(chatRoomModel.getId()).collectList().block();
            // chatMessageEntityList가 null이 아니고 비어있지 않으면
            if (chatMessageEntityList != null && !chatMessageEntityList.isEmpty()) {
                // 각 채팅방의 메시지를 리스트에 추가
                loginUserChatMessageList.addAll(chatMessageEntityList.stream().map(ChatMessageModel::toModel).toList());
            }
        });

        model.addAttribute("loginUserChatRoomList", loginUserChatRoomList);
        model.addAttribute("loginUserChatMessageList", loginUserChatMessageList);

        return "users/chat";
    }
    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession httpSession){
        // 세션에서 로그인된 사용자 정보 호출

        UserModel loginUser = (UserModel) httpSession.getAttribute("loginUser");
        // 로그인된 사용자가 올린 상품 목록 조회
        List<ProductModel> userProducts = productService.findProductsByUserId(loginUser.getId());
        // 모델에 로그인된 사용자 정보 및 상품 목록 추가
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("userProducts", userProducts);

        return "users/mypage";  // mypage.html 페이지로 이동
    }

    //회원가입
    @PostMapping("/register")
    public String register(@ModelAttribute UserModel userModel) {
        userService.registerUser(userModel);
        return "redirect:/api/products/list";
    }
}
