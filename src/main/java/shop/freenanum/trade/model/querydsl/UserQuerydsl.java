package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserQuerydsl {
    UserEntity findByUsername(String username);

    List<UserEntity> getList();

    UserEntity getById(Long id);

    boolean existsUser(Long id);

    long getRowCount();

    Optional<UserEntity> getByAddress(ProductEntity product);

}
