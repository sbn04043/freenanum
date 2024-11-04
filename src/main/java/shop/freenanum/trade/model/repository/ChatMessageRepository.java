package shop.freenanum.trade.model.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import shop.freenanum.trade.model.entity.ChatMessageEntity;
import shop.freenanum.trade.model.querydsl.ChatMessageQuerydsl;

import java.util.List;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessageEntity, String>, ChatMessageQuerydsl {
    Flux<ChatMessageEntity> findByChatRoomId(Long id);
}
