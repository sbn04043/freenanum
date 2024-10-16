package shop.freenanum.trade.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import shop.freenanum.trade.model.domain.UserModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(max = 40)
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Length(max = 40)
    private String name;

    @NotNull
    private String nickname;

    @Length(max = 40)
    private String phone;

    // 프로필
    private String url;

    private Long sellCount;

    private Long buyCount;

    private Double score;

    private String userAddress;

    @Getter
    private boolean enabled;

    @Length(max = 20)
    private String gender;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public static UserEntity toRegisterEntity(UserModel userModel) {
        return UserEntity.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .name(userModel.getName())
                .nickname(userModel.getNickname())
                .phone(userModel.getPhone())
                .sellCount(0L)
                .buyCount(0L)
                .score(0.0)
                .userAddress(userModel.getUserAddress())
                .gender(userModel.getGender())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

}
