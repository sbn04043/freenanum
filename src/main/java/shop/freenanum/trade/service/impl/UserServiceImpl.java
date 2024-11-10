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
//    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        // 실제 사용자 인증 로직을 구현 (예: DB 조회)
        // 예시: 기본 사용자 "test"와 비밀번호 "password" 사용
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null && userEntity.getPassword().equals(password)) {
            return username;
        }
        throw new RuntimeException("Invalid");
    }

    @Override
    public UserModel findByEmail(String username) {
        return UserModel.toModel(userRepository.findByUsername(username));
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
    public void registerUser(UserModel userModel) {
        if (userRepository.existsByUsername(userModel.getUsername())) {
            throw new IllegalArgumentException("This email is already registered.");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .name(userModel.getName())
                .nickname(userModel.getNickname())
                .phone(userModel.getPhone())
                .gender(userModel.getGender())
                .url(userModel.getUrl())
                .userAddress(userModel.getUserAddress())
                .enabled(false) // 기본값으로 false 설정
                .score((double) 0)
                .buyCount(0L)
                .sellCount(0L)
                .build();

        userRepository.save(userEntity);
    }
}
