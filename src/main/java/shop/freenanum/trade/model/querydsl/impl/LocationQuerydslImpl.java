package shop.freenanum.trade.model.querydsl.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.model.querydsl.LocationQuerydsl;

import java.util.List;

@RequiredArgsConstructor
public class LocationQuerydslImpl implements LocationQuerydsl {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<LocationEntity> getSearchList(String locationName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("location").regex(locationName));
        query.limit(10);

        return mongoTemplate.find(query, LocationEntity.class);
    }

    @Override
    public List<LocationEntity> getList() {
        return mongoTemplate.findAll(LocationEntity.class);
    }

    @Override
    public LocationEntity getByLocationId(String id) {
        return mongoTemplate.findById(id, LocationEntity.class);
    }

    @Override
    public boolean existsLocation(String id) {
        return mongoTemplate.exists(Query.query(Criteria.where("id").is(id)), LocationEntity.class);
    }

    @Override
    public long getRowCount() {
        return mongoTemplate.count(new Query(), LocationEntity.class);
    }
}
