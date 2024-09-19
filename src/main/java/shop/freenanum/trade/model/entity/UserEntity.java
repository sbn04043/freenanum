package shop.freenanum.trade.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    private String userImg;

    private Long giveCount;

    private Long receiveCount;

    private Double score;

    private String userAddress;

    @Length(max = 20)
    private String gender;
}
