package shop.freenanum.trade.model.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.QProductEntity;
import shop.freenanum.trade.model.entity.QProductImgEntity;
import shop.freenanum.trade.model.entity.QUserEntity;
import shop.freenanum.trade.model.querydsl.ProductQuerydsl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductQuerydslImpl implements ProductQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QProductEntity qProductEntity = QProductEntity.productEntity;
    private QUserEntity qUserEntity = QUserEntity.userEntity;
    private QProductImgEntity qProductImgEntity = QProductImgEntity.productImgEntity;

    @Override
    public Long searchCount(String locationInput, String productInput) {
        BooleanBuilder builder = new BooleanBuilder();

        if (locationInput != null && !locationInput.isEmpty()) {
            builder.and(qProductEntity.productAddress.contains(locationInput));
        }

        if (productInput != null && !productInput.isEmpty()) {
            builder.and(qProductEntity.productTitle.contains(productInput));
        }

        return Optional.ofNullable(
                jpaQueryFactory
                        .select(qProductEntity.count())
                        .from(qProductEntity)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);
    }

    @Override
    public ProductModel getProductWithImgUrl(Long id) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        ProductModel.class,
                        qProductEntity.id,
                        qProductEntity.userId,
                        qProductEntity.productTitle,
                        qProductEntity.productAddress,
                        qProductEntity.productDescription,
                        qProductEntity.productStatus,
                        qProductEntity.price,
                        qProductEntity.views,
                        qProductImgEntity.productImg // imgUrl로 설정
                ))
                .from(qProductEntity)
                .leftJoin(qProductImgEntity).on(qProductImgEntity.productId.eq(qProductEntity.id)) // 여기서 ID를 비교
                .where(qProductEntity.id.eq(id))
                .limit(1)
                .fetchOne();
    }

    @Override
    public ProductModel getProductWithNickname(Long id) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        ProductModel.class,
                        qProductEntity.id,
                        qProductEntity.userId,
                        qProductEntity.productTitle,
                        qProductEntity.productAddress,
                        qProductEntity.productDescription,
                        qProductEntity.productStatus,
                        qProductEntity.price,
                        qProductEntity.views,
                        qUserEntity.nickname, // nickname
                        qProductImgEntity.productImg // imgUrl로 설정
                ))
                .from(qProductEntity)
                .leftJoin(qUserEntity).on(qProductEntity.userId.eq(qUserEntity.id))
                .leftJoin(qProductImgEntity).on(qProductImgEntity.productId.eq(qProductEntity.id)) // 여기서 ID를 비교
                .where(qProductEntity.id.eq(id))
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<ProductEntity> getHotList() {
        return jpaQueryFactory
                .selectFrom(qProductEntity)
                .orderBy(qProductEntity.views.desc())
                .limit(100)
                .fetch();
    }

    @Override
    public List<ProductEntity> getList() {
        return jpaQueryFactory
                .selectFrom(qProductEntity)
                .fetch();
    }

    @Override
    public ProductEntity getByProductId(Long id) {
        return jpaQueryFactory
                .selectFrom(qProductEntity)
                .where(qProductEntity.id.eq(id))
                .fetchOne();
    }

    @Override
    public boolean existsProduct(Long id) {
        return getByProductId(id) != null;
    }

    @Override
    public long getRowCount() {
        return jpaQueryFactory
                .selectFrom(qProductEntity)
                .fetchCount();
    }

    @Override
    public List<ProductEntity> getByUserId(Long userId) {
        return jpaQueryFactory
                .selectFrom(qProductEntity)
                .where(qProductEntity.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<ProductEntity> search(Long pageNo, String locationInput, String productInput) {
        BooleanBuilder builder = new BooleanBuilder();

        if (locationInput != null && !locationInput.isEmpty()) {
            builder.and(qProductEntity.productAddress.contains(locationInput));
        }

        if (productInput != null && !productInput.isEmpty()) {
            builder.and(qProductEntity.productTitle.contains(productInput));
        }

        return jpaQueryFactory
                .selectFrom(qProductEntity)
                .where(builder)
                .offset((pageNo - 1) * 20L)
                .limit(20L)
                .orderBy(qProductEntity.id.desc())
                .fetch();
    }
}
