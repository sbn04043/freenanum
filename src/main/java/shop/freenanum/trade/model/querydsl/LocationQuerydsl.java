package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.model.entity.ProductEntity;

import java.util.List;

public interface LocationQuerydsl {
    List<LocationEntity> getSearchList(String locationName);

    List<LocationEntity> getList();

    LocationEntity getByLocationId(String id);

    boolean existsLocation(String id);

    long getRowCount();
}
