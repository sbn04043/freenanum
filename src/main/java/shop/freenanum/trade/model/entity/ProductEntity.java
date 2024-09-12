package shop.freenanum.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
public class ProductEntity {
    protected enum STATUS {VERY_POOR, POOR, FAIR, GOOD, EXCELLENT}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(max = 20)
    private String product_name;

    @NotNull
    private String productAddress;

    @NotNull
    private String product_title;

    private String product_description;

    private String product_image;

    @NotNull
    private STATUS product_status;

    private Long views;
}
