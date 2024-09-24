package shop.freenanum.trade.model.querydsl;

import shop.freenanum.trade.model.entity.ProductEntity;

import java.util.List;

public interface ProductQuerydsl {
    List<ProductEntity> getHotList();

    List<ProductEntity> getList();

    ProductEntity getByProductId(Long id);

    boolean existsProduct(Long id);

    long getRowCount();

    List<ProductEntity> getByUserId(Long userId);

    List<ProductEntity> search(int pageNo, String locationInput, String productInput);
}
