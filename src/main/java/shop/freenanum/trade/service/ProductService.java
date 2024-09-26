package shop.freenanum.trade.service;

import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> findAll();

    UserEntity save(ProductEntity productEntity);

    Optional<ProductEntity> findById(Long id);

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);

    Map<?, ?> login(UserModel model);

    void productCrawling();
}
