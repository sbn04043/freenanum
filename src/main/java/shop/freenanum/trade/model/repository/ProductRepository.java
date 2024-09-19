package shop.freenanum.trade.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.querydsl.ProductQuerydsl;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductQuerydsl {
}
