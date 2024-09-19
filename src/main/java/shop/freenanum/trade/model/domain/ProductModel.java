package shop.freenanum.trade.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.enumeration.ProductStatus;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductModel {
    private Long id;

    private String productAddress;

    private String productTitle;

    private String productDescription;

    private String productImage;

    private ProductStatus productStatus;

    private Long views;
}
