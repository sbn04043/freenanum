package shop.freenanum.trade.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.querydsl.ProductQuerydsl;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, String>, ProductQuerydsl {
}
