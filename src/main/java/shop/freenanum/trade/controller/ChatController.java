package shop.freenanum.trade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send") // 클라이언트가 이 경로로 메시지를 보내면
    public void sendMessage(String message) {
        messagingTemplate.convertAndSend("/topic/messages", message); // 구독자에게 메시지 전송
    }

    @GetMapping("/")
    public String chatHome() {
        return "/chat/index";
    }
}
