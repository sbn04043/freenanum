package shop.freenanum.trade.restController;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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
import java.util.stream.Collectors;

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
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build()));

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", chatRoomModel.getId());
        return ResponseEntity.ok(response);
    }

    @Transactional
    @GetMapping("/getChatMessages/{chatRoomId}")
    public Mono<ResponseEntity<Map<String, Object>>> getChatMessages(@PathVariable Long chatRoomId, HttpSession httpSession) {
        Long loginUserId = ((UserModel) httpSession.getAttribute("loginUser")).getId();

        return chatMessageRepository.findByChatRoomId(chatRoomId) // MongoDB에서 채팅 메시지를 가져옴
                .flatMap(chatMessageEntity -> {
                    if (chatMessageEntity.getReceiverId().equals(loginUserId) && !chatMessageEntity.getRead()) {
                        // 메시지가 로그인한 사용자의 수신 메시지이고, 아직 읽지 않은 경우
                        chatMessageEntity.setRead(true); // 읽음 처리
                        return chatMessageRepository.save(chatMessageEntity); // MongoDB에 저장
                    }
                    return Mono.just(chatMessageEntity); // 이미 읽은 메시지이거나 내가 보낸 메시지인 경우
                })
                .collectList() // Flux를 List로 변환
                .flatMap(chatMessageEntityList -> {
                    Map<String, Object> result = new HashMap<>();
                    UserModel loginUser = (UserModel) httpSession.getAttribute("loginUser");

                    // MySQL 데이터는 동기적으로 조회
                    Optional<ChatRoomEntity> chatRoomEntityOptional = chatRoomRepository.findById(chatRoomId);
                    if (chatRoomEntityOptional.isPresent()) {
                        ChatRoomEntity chatRoomEntity = chatRoomEntityOptional.get();
                        UserModel opponentUser;

                        if (chatRoomEntity.getUserId1().equals(loginUser.getId())) {
                            opponentUser = UserModel.toModel(userRepository.getByUserId(chatRoomEntity.getUserId2()));
                        } else {
                            opponentUser = UserModel.toModel(userRepository.getByUserId(chatRoomEntity.getUserId1()));
                        }

                        result.put("chatMessages", chatMessageEntityList);
                        result.put("opponentUser", opponentUser);

                        return Mono.just(ResponseEntity.ok(result));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build()); // 채팅방이 없는 경우
                    }
                });
//        UserModel loginUser = (UserModel) httpSession.getAttribute("loginUser");
//
//        chatMessageRepository.findByChatRoomId(chatRoomId).map(chatMessageEntity -> {
//            if (chatMessageEntity.getReceiverId() == loginUser.getId()) {
//                chatMessageEntity.setRead(true);
//                chatMessageRepository.save(chatMessageEntity);
//            }
//        });
//
//        return chatMessageRepository.findByChatRoomId(chatRoomId)  // MongoDB에서 가져오는 Flux 데이터
//                .collectList()  // Flux를 List로 변환
//                .flatMap(chatMessageEntity -> {
//                    Map<String, Object> result = new HashMap<>();
//
//                    // MySQL 데이터는 동기적으로 조회
//                    Optional<ChatRoomEntity> chatRoomEntityOptional = chatRoomRepository.findById(chatRoomId);
//                    if (chatRoomEntityOptional.isPresent()) {
//                        ChatRoomEntity chatRoomEntity = chatRoomEntityOptional.get();
//                        UserModel opponentUser;
//
//                        if (chatRoomEntity.getUserId1() == loginUser.getId()) {
//                            opponentUser = UserModel.toModel(userRepository.getByUserId(chatRoomEntity.getUserId2()));
//                        } else {
//                            opponentUser = UserModel.toModel(userRepository.getByUserId(chatRoomEntity.getUserId1()));
//                        }
//
//                        result.put("chatMessages", chatMessageEntity != null ? chatMessageEntity : new ArrayList<>());
//                        result.put("opponentUser", opponentUser);
//
//                        return Mono.just(ResponseEntity.ok(result));
//                    } else {
//                        return Mono.just(ResponseEntity.notFound().build());  // 채팅방이 없는 경우
//                    }
//                });
    }

    @GetMapping("/loadUserList/{loginUserId}")
    public ResponseEntity<List<UserModel>> loadUserList(@PathVariable Long loginUserId, HttpSession session) {
        List<UserModel> users = chatRoomRepository.getLoginUserChatRooms(loginUserId).stream()
                .map(chatRoomEntity -> {
                    Long opponentUserId = Objects.equals(chatRoomEntity.getUserId1(), loginUserId)
                            ? chatRoomEntity.getUserId2() : chatRoomEntity.getUserId1();
                    return UserModel.toModel(userRepository.getByUserId(opponentUserId));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/getChatRoomId/{opponentId}")
    public Mono<ResponseEntity<Map<String, Object>>> getChatRoomId(@PathVariable Long opponentId, HttpSession session) {
        Long loginUserId = ((UserModel) session.getAttribute("loginUser")).getId();
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findByUserIdAndOtherUserId(loginUserId, opponentId);

        Map<String, Object> resultMap = new HashMap<>();
        Long chatRoomId = chatRoomEntity.getId();
        resultMap.put("chatRoomId", chatRoomId);

        Mono<Long> unreadMessageCount = chatMessageRepository
                .findByChatRoomIdAndReceiverIdAndReadFalse(chatRoomId, loginUserId)
                .count();

        return unreadMessageCount.flatMap(count -> {
            resultMap.put("unreadMessageCount", count);
            return Mono.just(ResponseEntity.ok(resultMap));
        });
    }

    @MessageMapping("/sendMessage")
    public Mono<ChatMessageEntity> sendMessage(ChatMessageEntity chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Long loginUserId = (Long) headerAccessor.getSessionAttributes().get("loginUserId");
        System.out.println("loginUserId: " + loginUserId);
        if (loginUserId != null) {
            chatMessage.setSenderId(loginUserId);
            chatMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            chatMessage.setRead(false);

            ChatRoomEntity chatRoomEntity = chatRoomRepository.getByChatRoomId(chatMessage.getChatRoomId());
            chatRoomEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            chatRoomRepository.save(chatRoomEntity);

            if (loginUserId.equals(chatRoomEntity.getUserId1())) {
                chatMessage.setReceiverId(chatRoomEntity.getUserId2());
            } else {
                chatMessage.setReceiverId(chatRoomEntity.getUserId1());
            }

            return chatMessageRepository.save(chatMessage)
                    .doOnSuccess(savedMessage -> {
                        // 메시지가 저장된 후 수신자에게 전송
                        messagingTemplate.convertAndSend("/queue/chat/user/" + savedMessage.getReceiverId(), savedMessage);
                        System.out.println("chatMessage: " + savedMessage);
                    });
        } else {
            System.out.println("No user session found.");
            return Mono.empty(); // 세션이 없으면 빈 Mono 반환
        }
    }
}
