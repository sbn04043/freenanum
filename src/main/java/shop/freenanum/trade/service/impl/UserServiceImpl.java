package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    //    private final PasswordEncoder passwordEncoder;
//    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/profile/";
    String directoryPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "profile";

    @Override
    public UserEntity save(UserEntity user, MultipartFile profileImage) {
        if (profileImage != null) {
            String profileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
            File file = new File(Paths.get(directoryPath, profileName).toString());

            try {
                profileImage.transferTo(file);
                user.setUrl(profileName);
                return userRepository.save(user).block();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return userRepository.save(user).block();
        }
    }

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
    public Optional<UserEntity> findById(String id) {
        return userRepository.findById(id).blockOptional();
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String id) {

    }
}
