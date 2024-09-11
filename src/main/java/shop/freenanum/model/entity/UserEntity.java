package shop.freenanum.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.autoconfigure.domain.EntityScan;

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
    private int id;

    @NotNull
    @Length(max = 20)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Length(max = 20)
    private String name;

    @Length(max = 15)
    private String phone;

    // 프로필
    private String url;

    private Integer tradeCount;

    private Double score;
}
