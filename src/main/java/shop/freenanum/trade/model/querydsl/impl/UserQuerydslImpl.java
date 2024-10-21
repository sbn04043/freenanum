package shop.freenanum.trade.model.querydsl.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.querydsl.UserQuerydsl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserQuerydslImpl implements UserQuerydsl {
    private final MongoTemplate mongoTemplate;

    @Override
    public UserEntity findByUsername(String username) {
        Query query = new Query();

        if (username != null) {
            query.addCriteria(Criteria.where("username").is(username));
        } else {
            query.addCriteria(Criteria.where("username").is(null));
        }

        return mongoTemplate.findOne(query, UserEntity.class);
    }

    @Override
    public List<UserEntity> getList() {
        return mongoTemplate.findAll(UserEntity.class);
    }

    @Override
    public UserEntity getById(String id) {
        return mongoTemplate.findById(id, UserEntity.class);
    }

    @Override
    public boolean existsUser(String id) {
        return mongoTemplate.exists(new Query(Criteria.where("id").is(id)), UserEntity.class);
    }

    @Override
    public long getRowCount() {
        return mongoTemplate.count(new Query(), UserEntity.class);
    }

    @Override
    public Optional<UserEntity> getByAddress(ProductEntity product) {
        UserEntity userEntity = mongoTemplate.findOne(
                new Query(Criteria.where("userAddress").is(product.getProductAddress())),
                UserEntity.class
        );
        return Optional.ofNullable(userEntity);
    }

    @Override
    public UserEntity findByUsernameAndPassword(String username, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username).and("password").is(password));
        return mongoTemplate.findOne(query, UserEntity.class);
    }

    @Override
    public Boolean checkUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.count(query, UserEntity.class) > 0;
    }
}
