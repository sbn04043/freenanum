package shop.freenanum.trade.model.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.model.entity.QLocationEntity;
import shop.freenanum.trade.model.querydsl.LocationQuerydsl;

import java.util.List;

@RequiredArgsConstructor
public class LocationQuerydslImpl implements LocationQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QLocationEntity qLocation = QLocationEntity.locationEntity;

    @Override
    public List<LocationEntity> getSearchList(String locationName) {
        return jpaQueryFactory.selectFrom(qLocation)
                .where(qLocation.locationName.like("%" + locationName + "%"))
                .orderBy(qLocation.locationName.length().asc())
                .limit(10)
                .fetch();

    }

    @Override
    public List<LocationEntity> getList() {
        return List.of();
    }

    @Override
    public LocationEntity getByLocationId(Long id) {
        return null;
    }

    @Override
    public boolean existsLocation(Long id) {
        return false;
    }

    @Override
    public long getRowCount() {
        return 0;
    }
}
