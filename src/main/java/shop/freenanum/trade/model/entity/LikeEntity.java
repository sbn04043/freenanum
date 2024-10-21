package shop.freenanum.trade.model.entity;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "like")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEntity {
    @Id
    private String id;

    @NotNull
    private String userId;

    @NotNull
    private String productId;
}
