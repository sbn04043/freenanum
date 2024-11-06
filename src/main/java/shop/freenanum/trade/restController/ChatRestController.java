package shop.freenanum.trade.restController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.model.domain.ChatMessageModel;
import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.ChatMessageEntity;
import shop.freenanum.trade.model.entity.ChatRoomEntity;
import shop.freenanum.trade.model.repository.ChatMessageRepository;
import shop.freenanum.trade.model.repository.ChatRoomRepository;
import shop.freenanum.trade.model.repository.UserRepository;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;


    @GetMapping("/checkChatRoom/{userId}")
    public ResponseEntity<Map<String, Object>> checkChatRoom(@PathVariable Long userId, HttpSession session) {
        System.out.println("userId = " + userId + ", session = " + session);
        Long loginUserId = ((UserModel) session.getAttribute("loginUser")).getId();
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findByUserIdAndOtherUserId(loginUserId, userId);

        Map<String, Object> response = new HashMap<>();
        if (chatRoomEntity != null) {
            response.put("exists", true);
            response.put("chatRoomId", chatRoomEntity.getId());
            return ResponseEntity.ok(response);
        } else {
            response.put("exists", false);
            return ResponseEntity.ok(response);
        }
    }


    @GetMapping("/createChatRoom/{userId}")
    public ResponseEntity<Map<String, Object>> createChatRoom(@PathVariable Long userId, HttpSession session) {
        UserModel loginUser = ((UserModel) session.getAttribute("loginUser"));


        ChatRoomModel chatRoomModel = ChatRoomModel.toModel(chatRoomRepository.save(ChatRoomEntity.builder()
                .userId1(userId)
                .nickname1(userRepository.getByUserId(userId).getNickname())
                .userId2(loginUser.getId())
                .nickname2(loginUser.getNickname())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build()));

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", chatRoomModel.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getChatMessages/{chatRoomId}")
    public ResponseEntity<Map<String, Object>> getChatMessages(@PathVariable Long chatRoomId, HttpSession httpSession) {
        List<ChatMessageEntity> chatMessageEntityList = chatMessageRepository.findByChatRoomId(chatRoomId)
                .collectList()
                .block();

        Map<String, Object> result = new HashMap<>();

        UserModel loginUser = (UserModel) httpSession.getAttribute("loginUser");
        ChatRoomEntity chatRoomEntity = chatRoomRepository.getById(chatRoomId);
        UserModel opponentUser = new UserModel();
        if (chatRoomEntity.getUserId1() == loginUser.getId()) {
            opponentUser = UserModel.toModel(userRepository.getByUserId(chatRoomEntity.getUserId2()));
        } else {
            opponentUser = UserModel.toModel(userRepository.getByUserId(chatRoomEntity.getUserId1()));
        }

        result.put("chatMessages", chatMessageEntityList != null ? chatMessageEntityList : new ArrayList<>()); // 빈 리스트 반환
        result.put("opponentUser", opponentUser);


        return ResponseEntity.ok(result);
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessageEntity chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Long loginUserId = (Long) headerAccessor.getSessionAttributes().get("loginUserId");
        System.out.println("loginUserId: " + loginUserId);
        if (loginUserId != null) {
            chatMessage.setSenderId(loginUserId);
            chatMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            ChatRoomEntity chatRoomEntity = chatRoomRepository.getByChatRoomId(chatMessage.getChatRoomId());
            if (loginUserId.equals(chatRoomEntity.getUserId1())) {
                chatMessage.setReceiverId(chatRoomEntity.getUserId2());
            } else {
                chatMessage.setReceiverId(chatRoomEntity.getUserId1());
            }

            System.out.println("chatMessage = " + chatMessage);

            messagingTemplate.convertAndSend("/queue/chat/" + chatMessage.getChatRoomId(), chatMessage);
        } else {
            System.out.println("No user session found.");
        }

//        chatMessageRepository.save(chatMessage);

        messagingTemplate.convertAndSend("/queue/chat/" + chatMessage.getChatRoomId(), chatMessage);
    }
}
