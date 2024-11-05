package shop.freenanum.trade.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.entity.ProductEntity;
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

    private Long userId;

    private String productTitle;

    private String productAddress;

    private String productDescription;

    private ProductStatus productStatus;

    private Long price;

    private Long views;

    private String nickname;

    private String imgUrl;

    public static ProductModel toModel(ProductEntity productEntity) {
        return ProductModel.builder()
                .id(productEntity.getId())
                .userId(productEntity.getUserId())
                .productTitle(productEntity.getProductTitle())
                .productAddress(productEntity.getProductAddress())
                .productDescription(productEntity.getProductDescription())
                .productStatus(productEntity.getProductStatus())
                .price(productEntity.getPrice())
                .views(productEntity.getViews())
                .build();
    }

    public static ProductModel toModel(ProductEntity productEntity, String userNickname) {
        return ProductModel.builder()
                .id(productEntity.getId())
                .productAddress(productEntity.getProductAddress())
                .productTitle(productEntity.getProductTitle())
                .productDescription(productEntity.getProductDescription())
                .productStatus(productEntity.getProductStatus())
                .price(productEntity.getPrice())
                .views(productEntity.getViews())
                .nickname(userNickname)
                .build();
    }

//    public static ProductModel toModel(ProductEntity productEntity, String imgUrl) {}
}
