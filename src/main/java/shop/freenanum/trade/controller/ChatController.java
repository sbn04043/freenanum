package shop.freenanum.trade.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import shop.freenanum.trade.model.domain.ChatMessageModel;
import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.entity.ChatMessageEntity;
import shop.freenanum.trade.model.entity.ChatRoomEntity;
import shop.freenanum.trade.model.repository.ChatMessageRepository;
import shop.freenanum.trade.model.repository.ChatRoomRepository;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.ChatService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessageModel chatMessageModel) {
        System.out.println("chatMessageModel = " + chatMessageModel);

        ChatRoomModel chatRoomModel = chatService.getChatRoom(chatMessageModel.getSenderId(), chatMessageModel.getReceiverId());

        //redis를 통해 해당 채팅방에 있는지 확인(senderId, receiverId 통해)
        Long chatRoomId = -1L;
        if (chatRoomModel == null) {
            //없으면 새로운 chatRoom을 저장
            ChatRoomEntity newChatRoom = chatRoomRepository.save(
                    ChatRoomEntity.builder()
                            .userId1(chatMessageModel.getSenderId())
                            .nickname1(userRepository.getByUserId(chatMessageModel.getSenderId()).getNickname())
                            .userId2(chatMessageModel.getReceiverId())
                            .nickname2(userRepository.getByUserId(chatMessageModel.getReceiverId()).getNickname())
                            .createdAt(new Timestamp(System.currentTimeMillis())).build());

            //redis에 해당 방을 추가
            List<ChatRoomEntity> chatRoomEntityList = (List<ChatRoomEntity>) redisTemplate.opsForValue().get("user:" + chatMessageModel.getSenderId() + ":chatRooms");
            if (chatRoomEntityList == null) {
                chatRoomEntityList = new ArrayList<>();
            }
            chatRoomEntityList.add(newChatRoom);
            redisTemplate.opsForValue().set("user:" + chatMessageModel.getSenderId() + ":chatRooms", chatRoomEntityList);
            chatRoomModel = ChatRoomModel.toModel(newChatRoom);
        }

        //mongodb에 메시지 저장
        chatMessageModel.setChatRoomId(chatRoomModel.getId());
        chatMessageModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        chatMessageRepository.save(ChatMessageEntity.toEntity(chatMessageModel));

        //reciever에게 메세지 전송
        messagingTemplate.convertAndSendToUser(
                chatMessageModel.getReceiverId().toString(),
                "/queue/messages",
                chatMessageModel
        );
    }
}
