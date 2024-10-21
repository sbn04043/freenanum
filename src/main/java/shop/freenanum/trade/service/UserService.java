package shop.freenanum.trade.service;

import org.springframework.web.multipart.MultipartFile;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    UserEntity save(UserEntity user, MultipartFile profileImage);

    String login(String username, String password);

    UserModel findByEmail(String email);

    List<UserEntity> findAll();

    UserEntity save(UserEntity user);

    Optional<UserEntity> findById(String id);

    boolean existsById(String id);

    long count();

    void deleteById(String id);
}
