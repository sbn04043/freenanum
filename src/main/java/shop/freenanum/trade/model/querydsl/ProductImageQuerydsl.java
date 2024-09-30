package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.entity.ProductImgEntity;

import java.util.List;

public interface ProductImageQuerydsl {
    List<ProductImgEntity> findByProductId(Long id);

    String getOneById(Long id);
}
