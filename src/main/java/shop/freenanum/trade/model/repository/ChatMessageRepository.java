package shop.freenanum.trade.model.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import shop.freenanum.trade.model.entity.ChatMessageEntity;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessageEntity, String> {
    Flux<ChatMessageEntity> findByChatRoomId(Long id);

    // 특정 채팅방(chatRoomId), 수신자(receiverId), 읽지 않은(read == false) 메시지들을 가져오는 메서드
    Flux<ChatMessageEntity> findByChatRoomIdAndReceiverIdAndReadFalse(Long chatRoomId, Long receiverId);
}
