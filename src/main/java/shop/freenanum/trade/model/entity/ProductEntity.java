package shop.freenanum.trade.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import shop.freenanum.trade.model.enumeration.ProductStatus;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    @NotNull
    private String productTitle;

    @NotNull
    private String productAddress;

    @Length(max = 10000)
    private String productDescription;

    @NotNull
    private ProductStatus productStatus;

    private Long price;

    private Long views;
}
