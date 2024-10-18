package shop.freenanum.trade.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.model.domain.ChatMessage;
import shop.freenanum.trade.service.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatRestController {
    private SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/send") // 클라이언트에서 메시지를 보낼 경로
    public Mono<Void> sendMessage(ChatMessage chatMessage) {
        // Redis에 메시지 저장
        chatService.saveMessage(chatMessage);

        // 모든 클라이언트에게 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);
        return Mono.empty();
    }
}
