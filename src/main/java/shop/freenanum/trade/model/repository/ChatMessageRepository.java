package shop.freenanum.trade.model.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.ChatMessageEntity;
import shop.freenanum.trade.model.querydsl.ChatMessageQuerydsl;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessageEntity, String>, ChatMessageQuerydsl {
}
