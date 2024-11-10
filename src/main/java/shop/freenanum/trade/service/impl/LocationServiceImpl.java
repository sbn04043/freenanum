package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.service.LocationService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    @Override
    public List<LocationEntity> findAll() {
        return List.of();
    }

    @Override
    public LocationEntity save(LocationEntity user) {
        return null;
    }

    @Override
    public Optional<LocationEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Map<?, ?> login(UserModel model) {
        return Map.of();
    }
}
