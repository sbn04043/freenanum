package shop.freenanum.trade.service;

import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    UserModel findByEmail(String email);

    List<UserEntity> findAll();

    UserEntity save(UserEntity user);

    Optional<UserEntity> findById(Long id);

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);

    Map<?, ?> login(UserModel model);
}
