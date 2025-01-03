package shop.freenanum.trade.restController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.repository.ChatRoomRepository;
import shop.freenanum.trade.model.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 허용할 출처
public class UserRestController {
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping("/login")
    public ResponseEntity<UserModel> login(@RequestBody UserModel userModel, HttpSession httpSession) throws Exception {
        UserModel loginUser = UserModel.toModel(userRepository.findByUsernameAndPassword(userModel.getUsername(), userModel.getPassword()));
        if (loginUser == null) {
            System.out.println("로그인 실패");
            return ResponseEntity.ok(null);
        } else {
            System.out.println("로그인 성공: " + loginUser);
            httpSession.setAttribute("loginUser", loginUser);

//            redisTemplate.opsForValue().set("user:" + loginUser.getId() + ":chatRooms"
//                    , chatRoomRepository.getLoginUserChatRooms(loginUser.getId()));
            return ResponseEntity.ok(loginUser);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession httpSession) throws Exception {
        UserModel loginUser = (UserModel) httpSession.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.ok("로그인 하지 않은 상태입니다");
        }

        httpSession.removeAttribute("loginUser");
        return ResponseEntity.ok("로그아웃");
    }

    
}
