package shop.freenanum.trade.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.entity.QProductImgEntity;
import shop.freenanum.trade.model.querydsl.ProductImageQuerydsl;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImgEntity, Long>, ProductImageQuerydsl {
}
