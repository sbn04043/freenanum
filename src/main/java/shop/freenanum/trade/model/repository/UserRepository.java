package shop.freenanum.trade.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.querydsl.UserQuerydsl;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserQuerydsl {
}
