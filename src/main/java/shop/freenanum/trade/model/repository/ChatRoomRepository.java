package shop.freenanum.trade.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.ChatRoomEntity;
import shop.freenanum.trade.model.querydsl.ChatRoomQuerydsl;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>, ChatRoomQuerydsl {
}
