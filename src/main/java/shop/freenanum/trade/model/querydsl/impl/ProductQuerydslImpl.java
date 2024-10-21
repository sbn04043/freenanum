package shop.freenanum.trade.model.querydsl.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.querydsl.ProductQuerydsl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductQuerydslImpl implements ProductQuerydsl {
    private final MongoTemplate mongoTemplate;

    @Override
    public Long searchCount(String locationInput, String productInput) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (locationInput != null && !locationInput.isEmpty()) {
            criteria.and("productAddress").regex(locationInput);
        }

        if (productInput != null && !productInput.isEmpty()) {
            criteria.and("productTitle").regex(productInput);
        }

        query.addCriteria(criteria);
        return mongoTemplate.count(query, ProductEntity.class);
    }

    @Override
    public ProductModel getProductWithImgUrl(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        ProductEntity productEntity = mongoTemplate.findOne(query, ProductEntity.class);

        if (productEntity != null) {
            // 이미지 URL 조회
            List<ProductImgEntity> productImages = mongoTemplate.find(
                    new Query(Criteria.where("productId").is(productEntity.getId())),
                    ProductImgEntity.class
            );

            String imgUrl = productImages.isEmpty() ? null : productImages.get(0).getProductImg();

            return ProductModel.builder()
                    .id(productEntity.getId())
                    .userId(productEntity.getUserId())
                    .productTitle(productEntity.getProductTitle())
                    .productAddress(productEntity.getProductAddress())
                    .productDescription(productEntity.getProductDescription())
                    .productStatus(productEntity.getProductStatus())
                    .price(productEntity.getPrice())
                    .views(productEntity.getViews())
                    .imgUrl(imgUrl) // imgUrl로 설정
                    .build();
        }

        return null;
    }

    @Override
    public ProductModel getProductWithNickname(String id) {
        // 유사하게, nickname과 이미지를 가져오는 쿼리를 작성합니다.
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        ProductEntity productEntity = mongoTemplate.findOne(query, ProductEntity.class);

        if (productEntity != null) {
            // 유저 닉네임 조회
            UserEntity userEntity = mongoTemplate.findOne(
                    new Query(Criteria.where("id").is(productEntity.getUserId())),
                    UserEntity.class
            );

            // 이미지 URL 조회
            List<ProductImgEntity> productImages = mongoTemplate.find(
                    new Query(Criteria.where("productId").is(productEntity.getId())),
                    ProductImgEntity.class
            );

            String imgUrl = productImages.isEmpty() ? null : productImages.get(0).getProductImg();

            return ProductModel.builder()
                    .id(productEntity.getId())
                    .userId(productEntity.getUserId())
                    .productTitle(productEntity.getProductTitle())
                    .productAddress(productEntity.getProductAddress())
                    .productDescription(productEntity.getProductDescription())
                    .productStatus(productEntity.getProductStatus())
                    .price(productEntity.getPrice())
                    .views(productEntity.getViews())
                    .nickname(userEntity != null ? userEntity.getNickname() : null) // nickname
                    .imgUrl(imgUrl) // imgUrl로 설정
                    .build();
        }

        return null;
    }

    @Override
    public List<ProductEntity> getHotList() {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("views"))).limit(100);
        return mongoTemplate.find(query, ProductEntity.class);
    }

    @Override
    public List<ProductEntity> getList() {
        return mongoTemplate.findAll(ProductEntity.class);
    }

    @Override
    public ProductEntity getByProductId(String id) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), ProductEntity.class);
    }

    @Override
    public boolean existsProduct(String id) {
        return getByProductId(id) != null;
    }

    @Override
    public long getRowCount() {
        return mongoTemplate.count(new Query(), ProductEntity.class);
    }

    @Override
    public List<ProductEntity> getByUserId(String userId) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), ProductEntity.class);
    }

    @Override
    public List<ProductEntity> search(Long pageNo, String locationInput, String productInput) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (locationInput != null && !locationInput.isEmpty()) {
            criteria.and("productAddress").regex(locationInput);
        }

        if (productInput != null && !productInput.isEmpty()) {
            criteria.and("productTitle").regex(productInput);
        }

        query.addCriteria(criteria)
                .skip((pageNo - 1) * 20)
                .limit(20);

        return mongoTemplate.find(query, ProductEntity.class);
    }
}
