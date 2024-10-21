package shop.freenanum.trade.model.querydsl.impl;

import com.querydsl.core.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.querydsl.ProductImageQuerydsl;

import java.util.List;

@RequiredArgsConstructor
public class ProductImageQuerydslImpl implements ProductImageQuerydsl {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<ProductImgEntity> findByProductId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(id));
        return mongoTemplate.find(query, ProductImgEntity.class);
    }

    @Override
    public String getOneById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(id));
        query.limit(1);

        ProductImgEntity productImgEntity = mongoTemplate.findOne(query, ProductImgEntity.class);
        return productImgEntity != null ? productImgEntity.getProductImg() : null; // 반환할 이미지 경로 또는 null
    }
}
