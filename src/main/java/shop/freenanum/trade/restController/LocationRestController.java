package shop.freenanum.trade.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.freenanum.trade.model.domain.LocationModel;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.LocationEntity;
import shop.freenanum.trade.model.repository.LocationRepository;
import shop.freenanum.trade.service.LocationService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationRestController {
    private final LocationService locationService;
    private final LocationRepository locationRepository;

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchByLocationName(@RequestParam String locationName) {
        return ResponseEntity.ok(locationRepository.getSearchList(locationName)
                .stream()
                .map(LocationEntity::getLocationName)
                .toList());
    }

    public List<LocationModel> findAll() {
        return List.of();
    }

    public LocationEntity save(LocationEntity user) {
        return null;
    }

    public Optional<LocationEntity> findById(Long id) {
        return Optional.empty();
    }

    public boolean existsById(Long id) {
        return false;
    }

    public long count() {
        return 0;
    }

    public void deleteById(Long id) {

    }

    public Map<?, ?> login(UserModel model) {
        return Map.of();
    }
}
