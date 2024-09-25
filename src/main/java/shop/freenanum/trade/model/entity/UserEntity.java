package shop.freenanum.trade.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import shop.freenanum.trade.model.domain.UserModel;

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
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Length(max = 40)
    private String userName;

    @Length(max = 40)
    private String phone;

    // 프로필
    private String url;

    private Long sellCount;

    private Long buyCount;

    private Double score;

    private String userAddress;

    @Length(max = 20)
    private String gender;

    public static UserEntity toRegisterEntity(UserModel userModel) {
        return UserEntity.builder()
                .email(userModel.getEmail())
                .password(userModel.getPassword())
                .userName(userModel.getUserName())
                .phone(userModel.getPhone())
                .sellCount(0L)
                .buyCount(0L)
                .score(0.0)
                .userAddress(userModel.getUserAddress())
                .gender(userModel.getGender())
                .build();
    }
}
