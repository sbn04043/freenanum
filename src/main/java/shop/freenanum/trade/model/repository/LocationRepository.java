package shop.freenanum.trade.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.model.querydsl.LocationQuerydsl;

@Repository
public interface LocationRepository extends ReactiveMongoRepository<LocationEntity, String>, LocationQuerydsl {
}
