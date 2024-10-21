package shop.freenanum.trade.model.domain;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationModel {
    private String id;

    private String locationCode;

    private String locationName;

    private String isExist;
}
