package shop.freenanum.trade.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.entity.ChatRoomEntity;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.service.ChatService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/chat")
    public String chat(@RequestParam("recipientId") Long recipientId, Model model, HttpSession session) {
        return null;
    }

    @GetMapping("/home")
    public String chatHome(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/api/products/hotList";
        }

        model.addAttribute("chatRoomList", chatService.getLoginUserChatRooms(user.getId()));
        return "/users/chat";
    }
}
