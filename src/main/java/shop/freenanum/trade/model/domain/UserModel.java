package shop.freenanum.trade.model.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.entity.UserEntity;

import java.util.List;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    private Long id;

    private String username;

    private String password;

    private String name;

    private String nickname;

    private String phone;

    // 프로필
    private String url;

    private Long sellCount;

    private Long buyCount;

    private Double score;

    private String userAddress;

    private String gender;

//    private List<String> roles;

    public static UserModel toModel(UserEntity userEntity) {
        return UserModel.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .phone(userEntity.getPhone())
                .url(userEntity.getUrl())
                .sellCount(userEntity.getSellCount())
                .buyCount(userEntity.getBuyCount())
                .score(userEntity.getScore())
                .userAddress(userEntity.getUserAddress())
                .gender(userEntity.getGender())
                .build();
    }
}
