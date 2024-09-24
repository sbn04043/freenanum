package shop.freenanum.trade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.repository.ProductRepository;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("products", productRepository.getHotList().stream().map(productEntity ->
                ProductModel.builder()
                        .id(productEntity.getId())
                        .productTitle(productEntity.getProductTitle())
                        .productAddress(productEntity.getProductAddress())
                        .productStatus(productEntity.getProductStatus())
                        .views(productEntity.getViews())
                        .build()).toList());
        return "products/hotList";
    }

    @GetMapping("/{id}")
    public String showOne(Model model, @PathVariable Long id) {
        model.addAttribute("product", ProductModel.toModel(productRepository.getByProductId(id)));

        return "products/showOne";
    }
}
