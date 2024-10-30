package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.entity.ChatRoomEntity;

import java.util.List;

public interface ChatRoomQuerydsl {
    boolean isExistByUserId1AndUserId2(Long userId1, Long userId2);

    ChatRoomEntity getOne(Long userId1, Long userId2);

    List<ChatRoomEntity> getLoginUserChatRooms(Long id);
}
