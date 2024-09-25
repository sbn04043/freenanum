package shop.freenanum.trade.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.entity.UserEntity;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    private Long id;

    private String email;

    private String password;

    private String userName;

    private String phone;

    // 프로필
    private String url;

    private Long sellCount;

    private Long buyCount;

    private Double score;

    private String userAddress;

    private String gender;

    public static UserModel toModel(UserEntity userEntity) {
        return UserModel.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .userName(userEntity.getUserName())
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
