package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.entity.ProductEntity;

import java.util.List;

public interface ProductQuerydsl {
    Long searchCount(String locationInput, String productInput);

    ProductModel getProductWithImgUrl(Long id);

    ProductEntity getProductById(Long id);

    List<ProductEntity> getHotList();

    List<ProductEntity> getList();

    ProductEntity getByProductId(Long id);

    boolean existsProduct(Long id);

    long getRowCount();

    List<ProductEntity> getByUserId(Long userId);

    List<ProductEntity> search(Long pageNo, String locationInput, String productInput);
}
