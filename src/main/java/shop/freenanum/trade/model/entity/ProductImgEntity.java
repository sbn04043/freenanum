package shop.freenanum.trade.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_img")
public class ProductImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long product_id;

    private String product_img;
}
