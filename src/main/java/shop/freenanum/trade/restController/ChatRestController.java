//package shop.freenanum.trade.restController;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
//import shop.freenanum.trade.model.domain.ChatMessage;
////import shop.freenanum.trade.service.ChatService;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/chat")
//public class ChatRestController {
//    private SimpMessagingTemplate messagingTemplate;
////    private final ChatService chatService;
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendChatMessage(@RequestBody ChatMessage message) {
//        // 채팅 메시지를 처리하는 로직 추가
//        return ResponseEntity.ok("Message sent");
//    }
//
//    @GetMapping("/history/{userId}")
//    public List<ChatMessage> getChatHistory(@PathVariable String userId) {
//        // 사용자와의 채팅 기록을 조회하는 로직 추가
////        return chatService.getChatHistory(userId);
//    }
//}
