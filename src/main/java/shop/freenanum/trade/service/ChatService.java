package shop.freenanum.trade.service;

import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.entity.ChatMessageEntity;

import java.util.List;

public interface ChatService {
    public void saveMessage(ChatMessageEntity chatMessageEntity);

    public ChatRoomModel getChatRoom(Long userId1, Long userId2);

    List<ChatRoomModel> getLoginUserChatRooms(Long id);
}
