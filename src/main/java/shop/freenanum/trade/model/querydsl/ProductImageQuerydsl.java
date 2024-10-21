package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.entity.ProductImgEntity;

import java.util.List;

public interface ProductImageQuerydsl {
    List<ProductImgEntity> findByProductId(String id);

    String getOneById(String id);
}
