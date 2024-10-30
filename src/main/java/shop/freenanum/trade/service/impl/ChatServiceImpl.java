package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        chatMessageEntity.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(chatMessageEntity);
    }

    @Override
    public ChatRoomModel getChatRoom(Long userId1, Long userId2) {
        if (chatRoomRepository.isExistByUserId1AndUserId2(userId1, userId2)) {
            return ChatRoomModel.toModel(chatRoomRepository.getOne(userId1, userId2));
        } else {
            return ChatRoomModel.toModel(chatRoomRepository.save(ChatRoomEntity.builder()
                    .userId1(userId1)
                    .nickname1(userRepository.getByUserId(userId1).getNickname())
                    .userId2(userId2)
                    .nickname2(userRepository.getByUserId(userId2).getNickname())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build()));
        }
    }

    @Override
    public List<ChatRoomModel> getLoginUserChatRooms(Long id) {
        return chatRoomRepository.getLoginUserChatRooms(id)
                .stream().map(ChatRoomModel::toModel)
                .toList();

    }
}
