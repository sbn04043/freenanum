package shop.freenanum.trade.model.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.QProductEntity;
import shop.freenanum.trade.model.querydsl.ProductQuerydsl;

import java.util.List;

@RequiredArgsConstructor
public class ProductQuerydslImpl implements ProductQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QProductEntity qProductEntity = QProductEntity.productEntity;

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
    public ProductEntity getById(Long id) {
        return jpaQueryFactory
                .selectFrom(qProductEntity)
                .where(qProductEntity.id.eq(id))
                .fetchOne();
    }

    @Override
    public boolean existsProduct(Long id) {
        return getById(id) != null;
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
}
