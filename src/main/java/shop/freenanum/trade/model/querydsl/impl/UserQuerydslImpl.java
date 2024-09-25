package shop.freenanum.trade.model.querydsl.impl;

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

    @Override
    public UserEntity findByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(qUser)
                .where(qUser.email.eq(email))
                .fetchOne();

    }
}
