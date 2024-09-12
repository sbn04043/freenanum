package shop.freenanum.trade.model.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.UserEntity;

@Repository
public interface UserRepogitory extends JpaRepository<UserEntity, Long> {
}
