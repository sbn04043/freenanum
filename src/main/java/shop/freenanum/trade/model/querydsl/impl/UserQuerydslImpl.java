package shop.freenanum.trade.model.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.QUserEntity;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.querydsl.UserQuerydsl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserQuerydslImpl implements UserQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QUserEntity qUser = QUserEntity.userEntity;

    @Override
    public UserEntity findByUsername(String username) {
        BooleanBuilder builder = new BooleanBuilder();

        // username이 null인지 확인
        if (username != null) {
            builder.and(qUser.username.eq(username));
        } else {
            // username이 null일 경우
            builder.and(qUser.username.isNull());
        }

        return jpaQueryFactory
                .selectFrom(qUser)
                .where(builder)
                .fetchOne();
    }

    @Override
    public List<UserEntity> getList() {
        return jpaQueryFactory.selectFrom(qUser).fetch();
    }

    @Override
    public UserEntity getById(Long id) {
        return jpaQueryFactory.selectFrom(qUser).where(qUser.id.eq(id)).fetchOne();
    }

    @Override
    public boolean existsUser(Long id) {
        return !jpaQueryFactory.selectFrom(qUser).where(qUser.id.eq(id)).fetch().isEmpty();
    }

    @Override
    public long getRowCount() {
        return jpaQueryFactory.select(qUser.id.count()).from(qUser).fetchCount();
    }

    @Override
    public Optional<UserEntity> getByAddress(ProductEntity product) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qUser).where(qUser.userAddress.eq(product.getProductAddress())).fetchFirst());
    }
}
