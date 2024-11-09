package shop.freenanum.trade.model.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.model.domain.ChatRoomModel;
import shop.freenanum.trade.model.entity.ChatRoomEntity;
import shop.freenanum.trade.model.entity.QChatRoomEntity;
import shop.freenanum.trade.model.entity.QLocationEntity;
import shop.freenanum.trade.model.querydsl.ChatRoomQuerydsl;

import java.util.List;

@RequiredArgsConstructor
public class ChatRoomQuerydslImpl implements ChatRoomQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QChatRoomEntity qChatRoom = QChatRoomEntity.chatRoomEntity;

    @Override
    public boolean isExistByUserId1AndUserId2(Long userId1, Long userId2) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.or(qChatRoom.userId1.eq(userId1).and(qChatRoom.userId2.eq(userId2)));
        builder.or(qChatRoom.userId2.eq(userId1)).and(qChatRoom.userId1.eq(userId2));

        return jpaQueryFactory.selectFrom(qChatRoom).where(builder).fetchOne() != null;
    }

    @Override
    public ChatRoomEntity findByUserIdAndOtherUserId(Long userId1, Long userId2) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.or(qChatRoom.userId1.eq(userId1).and(qChatRoom.userId2.eq(userId2)))
                .or(qChatRoom.userId2.eq(userId1).and(qChatRoom.userId1.eq(userId2)));

        // 조건에 맞는 ChatRoomEntity를 바로 반환
        return jpaQueryFactory.selectFrom(qChatRoom)
                .where(builder)
                .fetchOne();
    }

    @Override
    public List<ChatRoomEntity> getLoginUserChatRooms(Long id) {
        return jpaQueryFactory
                .selectFrom(qChatRoom)
                .where(qChatRoom.userId1.eq(id).or(qChatRoom.userId2.eq(id)))
                .orderBy(qChatRoom.updatedAt.desc())
                .fetch();

    }

    @Override
    public ChatRoomEntity getByChatRoomId(Long chatRoomId) {
        return jpaQueryFactory
                .selectFrom(qChatRoom)
                .where(qChatRoom.id.eq(chatRoomId))
                .fetchOne();
    }
}
