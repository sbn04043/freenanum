package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        // 실제 사용자 인증 로직을 구현 (예: DB 조회)
        // 예시: 기본 사용자 "test"와 비밀번호 "password" 사용
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null && passwordEncoder.matches(password, userEntity.getPassword())) {
            return email;
        }
        throw new RuntimeException("Invalid");
    }

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
}
