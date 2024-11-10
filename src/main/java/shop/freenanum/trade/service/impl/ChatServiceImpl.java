package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.entity.ChatMessageEntity;
import shop.freenanum.trade.model.entity.ChatRoomEntity;
import shop.freenanum.trade.model.repository.ChatMessageRepository;
import shop.freenanum.trade.model.repository.ChatRoomRepository;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.ChatService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    public void saveMessage(ChatMessageEntity chatMessageEntity) {
        chatMessageEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        chatMessageRepository.save(chatMessageEntity);
    }

    @Override
    public ChatRoomModel getChatRoom(Long userId1, Long userId2) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findByUserIdAndOtherUserId(userId1, userId2);

        if (chatRoomEntity != null) {
            // 채팅방이 존재하는 경우
            return ChatRoomModel.toModel(chatRoomEntity);
        } else {
            // 채팅방이 존재하지 않는 경우 새로 생성
            ChatRoomEntity newChatRoom = ChatRoomEntity.builder()
                    .userId1(userId1)
                    .nickname1(userRepository.getByUserId(userId1).getNickname())
                    .userId2(userId2)
                    .nickname2(userRepository.getByUserId(userId2).getNickname())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            return ChatRoomModel.toModel(chatRoomRepository.save(newChatRoom));
        }
    }


    @Override
    public List<ChatRoomModel> getLoginUserChatRooms(Long id) {
        return chatRoomRepository.getLoginUserChatRooms(id)
                .stream().map(ChatRoomModel::toModel)
                .toList();

    }
}
