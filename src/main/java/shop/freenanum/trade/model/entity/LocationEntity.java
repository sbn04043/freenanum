package shop.freenanum.trade.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "location")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationEntity {
    @Id
    private String id;

    private String locationCode;

    private String locationName;

    private String isExist;
}
