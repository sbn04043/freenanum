//package shop.freenanum.trade.model.detail;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import shop.freenanum.trade.model.entity.UserEntity;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.stream.Collectors;
//
//@Getter
//@RequiredArgsConstructor
//@AllArgsConstructor
//public class MyUserDetails implements UserDetails {
//    private UserEntity userEntity;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 사용자 권한을 반환
//        return userEntity.getRoles().stream()
//                .map(role -> (GrantedAuthority) () -> role) // 역할을 GrantedAuthority로 변환
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getPassword() {
//        return userEntity.getPassword(); // 비밀번호 반환
//    }
//
//    @Override
//    public String getUsername() {
//        return userEntity.getUsername(); // 사용자 이름 반환
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // 계정 만료 여부
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true; // 계정 잠금 여부
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true; // 자격 증명 만료 여부
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return userEntity.isEnabled(); // 계정 활성화 여부
//    }
//}
