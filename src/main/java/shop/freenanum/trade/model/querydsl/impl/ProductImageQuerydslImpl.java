package shop.freenanum.trade.model.querydsl.impl;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.entity.QProductImgEntity;
import shop.freenanum.trade.model.querydsl.ProductImageQuerydsl;

import java.util.List;

@RequiredArgsConstructor
public class ProductImageQuerydslImpl implements ProductImageQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QProductImgEntity qProductImg = QProductImgEntity.productImgEntity;


    @Override
    public List<ProductImgEntity> findByProductId(Long id) {
        return jpaQueryFactory
                .selectFrom(qProductImg)
                .where(qProductImg.productId.eq(id))
                .fetch();
    }

    @Override
    public String getOneById(Long id) {
        return jpaQueryFactory
                .select(qProductImg.productImg)
                .from(qProductImg)
                .where(qProductImg.productId.eq(id))
                .limit(1)
                .fetchOne();
    }
}
