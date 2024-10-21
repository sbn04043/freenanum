package shop.freenanum.trade.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.enumeration.ProductStatus;

@Document(collection = "product")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
    @Id
    private String id;

    private String userId;
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

    public static ProductEntity toUploadEntity(ProductModel productModel) {
        return ProductEntity.builder()
                .userId("1") // 추후에 고치기
                .productTitle(productModel.getProductTitle())
                .productAddress(productModel.getProductAddress())
                .productDescription(productModel.getProductDescription())
                .price(productModel.getPrice())
                .views(0L)
                .build();
    }
}
