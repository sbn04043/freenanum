package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.entity.ProductEntity;

import java.util.List;

public interface ProductQuerydsl {
    Long searchCount(String locationInput, String productInput);

    ProductModel getProductWithImgUrl(String id);

    ProductModel getProductWithNickname(String id);

    List<ProductEntity> getHotList();

    List<ProductEntity> getList();

    ProductEntity getByProductId(String id);

    boolean existsProduct(String id);

    long getRowCount();

    List<ProductEntity> getByUserId(String userId);

    List<ProductEntity> search(Long pageNo, String locationInput, String productInput);
}
