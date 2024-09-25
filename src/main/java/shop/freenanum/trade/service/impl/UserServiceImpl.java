package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserModel findByEmail(String email) {
        return UserModel.toModel(userRepository.findByEmail(email));
    }

    @Override
    public List<UserEntity> findAll() {
        return List.of();
    }

    @Override
    public UserEntity save(UserEntity user) {
        return null;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
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
