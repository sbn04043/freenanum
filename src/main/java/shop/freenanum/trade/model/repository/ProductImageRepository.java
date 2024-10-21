package shop.freenanum.trade.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.querydsl.ProductImageQuerydsl;

@Repository
public interface ProductImageRepository extends ReactiveMongoRepository<ProductImgEntity, String>, ProductImageQuerydsl {
}