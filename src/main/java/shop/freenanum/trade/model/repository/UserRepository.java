package shop.freenanum.trade.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.querydsl.UserQuerydsl;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity, String>, UserQuerydsl {
}
