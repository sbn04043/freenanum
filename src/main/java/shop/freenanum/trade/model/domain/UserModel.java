package shop.freenanum.trade.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

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

    private Long giveCount;

    private Long receiveCount;

    private Double score;

    private String userAddress;
}
