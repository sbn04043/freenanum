package shop.freenanum.trade.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.model.entity.QLocationEntity;
import shop.freenanum.trade.model.querydsl.LocationQuerydsl;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long>, LocationQuerydsl {
}
