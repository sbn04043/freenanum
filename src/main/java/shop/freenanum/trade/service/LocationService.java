package shop.freenanum.trade.service;

import org.springframework.stereotype.Service;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.model.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LocationService {
    List<LocationEntity> findAll();

    LocationEntity save(LocationEntity user);

    Optional<LocationEntity> findById(Long id);

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);

    Map<?, ?> login(UserModel model);
}
