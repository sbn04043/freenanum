package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import shop.freenanum.trade.model.detail.MyUserDetails;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.repository.UserRepository;

@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보 조회
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // UserDetails 객체 생성
        return new MyUserDetails(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword());
    }
}
