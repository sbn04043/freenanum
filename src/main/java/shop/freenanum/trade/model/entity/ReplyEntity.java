package shop.freenanum.trade.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reply")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyEntity {
    @Id
    private String id;

    @NotNull
    @Length(min = 1, max = 255)
    private String replyContent;

    @NotNull
    private String userId;

    @NotNull
    private String productId;
}
